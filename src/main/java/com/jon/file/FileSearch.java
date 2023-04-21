package com.jon.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class FileSearch {
    private static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        System.out.println("Enter base directory (e.g. D:\\Java\\jdk1.8.0_102\\src\\java): ");
        String dictory = sc.nextLine();
        System.out.println("Enter keyword (e.g. volatile): ");
        String keyword = sc.nextLine();
        ExecutorService pool = Executors.newCachedThreadPool();

        final int FILE_QUEUE_SIZE = 10;// 文件阻塞队列大小
        final int SEARCH_THREADS = 100;// 搜索线程
        BlockingQueue<File> blocking = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);
        FileEnumerationTask enumerator = new FileEnumerationTask(blocking, new File(dictory));
        pool.submit(enumerator);
        for (int i = 1; i < SEARCH_THREADS; i++) {
            pool.submit(new SearchTask(blocking, keyword));
        }
        MatchCounter counter = new MatchCounter(new File(dictory), keyword, pool);
        Future<Integer> task = pool.submit(counter);
        try {
            System.out.println(task.get() + " matching files.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        pool.shutdown();
        int largestPoolSize = ((ThreadPoolExecutor)pool).getLargestPoolSize();
        System.out.println("largest Pool Size :" + largestPoolSize);
    }

}

class FileEnumerationTask implements Runnable {
    // 虚拟对象放置阻塞队列最后
    public static File DUMMY = new File("");
    private BlockingQueue<File> blocking;
    private File startingDictory;

    public FileEnumerationTask(BlockingQueue<File> blocking, File startingDictory) {
        this.blocking = blocking;
        this.startingDictory = startingDictory;
    }

    @Override
    public void run() {
        try {
            enumerate(startingDictory);
            blocking.put(DUMMY);
        } catch (InterruptedException ignored) {

        }
    }

    private void enumerate(File dir) throws InterruptedException {
        File[] files = dir.listFiles();
        if(files != null){
            for (File file : files) {
                if (file.isDirectory()) {
                    enumerate(file);
                } else {
                    blocking.put(file);
                }
            }
        }
    }
}

class SearchTask implements Runnable {
    private final BlockingQueue<File> queue;
    private final String keyword;

    public SearchTask(BlockingQueue<File> queue, String keyword) {
        this.queue = queue;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        try {
            boolean done = false;
            while (!done) {
                File file = queue.take();
                // 线程搜索到虚拟对象,将其放回 并终止搜索
                if (file == FileEnumerationTask.DUMMY) {
                    queue.put(file);
                    done = true;
                } else {
                    search(file);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void search(File file) throws IOException {
        try (Scanner sc = new Scanner(file)) {
            int lineNumber = 0;
            while (sc.hasNextLine()) {
                lineNumber++;
                String line = sc.nextLine();
                if (line.contains(keyword)) {
                    System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber, line);
                }
            }
        }
    }
}

class MatchCounter implements Callable<Integer> {
    private final File directory;
    private final String keyword;
    private final ExecutorService pool;

    public MatchCounter(File directory, String keyword, ExecutorService pool) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool = pool;
    }

    @Override
    public Integer call() {
        int count = 0;
        try {
            File[] files = directory.listFiles();
            List<Future<Integer>> results = new ArrayList<>();

            for (File file : files) {
                if (file.isDirectory()) {
                    MatchCounter counter = new MatchCounter(file, keyword, pool);
                    Future<Integer> task = pool.submit(counter);
                    results.add(task);
                } else {
                    if (search(file)) {
                        count++;
                    }
                }
            }
            for (Future<Integer> result : results) {
                try {
                    count += result.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean search(File file) {
        try {
            try (Scanner sc = new Scanner(file)) {
                boolean found = false;
                while (!found && sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.contains(keyword)) {
                        found = true;
                    }
                }
                return found;
            }
        } catch (IOException E) {
            return false;
        }
    }
}
