package com.yumaolin.util.Test;

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

public class BlockingQueueTest {
    public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
	System.out.println("Enter base directory (e.g. D:\\Java\\jdk1.8.0_102\\src\\java): ");
	String dictory = sc.nextLine();
	System.out.println("Enter keyword (e.g. volatile): ");
	String keyword=sc.nextLine();
	ExecutorService pool = Executors.newCachedThreadPool();
	
	final int FILE_QUEUE_SIZE=10;//文件阻塞队列大小
	final int SEARCH_THREADS=100;//搜索线程
	BlockingQueue<File> blocking = new ArrayBlockingQueue<File>(FILE_QUEUE_SIZE);
	FileEnumerationTask enumerator = new FileEnumerationTask(blocking,new File(dictory));
	//new Thread(enumerator).start();
	pool.submit(enumerator);
	for(int i=1;i<SEARCH_THREADS;i++){
	    //new Thread(new SearchTask(blocking, keyword)).start();
	    pool.submit(new SearchTask(blocking, keyword));
	}
	MatchCounter counter = new MatchCounter(new File(dictory), keyword,pool);
	//MatchCounter counter = new MatchCounter(new File(dictory), keyword);
	//FutureTask<Integer> task = new FutureTask<>(counter);
	//Thread t = new Thread(task);
	//t.start();
	Future<Integer> task =pool.submit(counter);
	
	try{
	    System.out.println(task.get()+" matching files.");
	}catch(Exception e){
	    e.printStackTrace();
	}
	pool.shutdown();
	int largestPoolSize = ((ThreadPoolExecutor)pool).getLargestPoolSize();
	System.out.println("largest Pool Size :"+largestPoolSize);
    }
    
}
    class FileEnumerationTask implements Runnable{
    	public  static File DUMMY = new File("");//虚拟对象放置阻塞队列最后
    	private BlockingQueue<File> blocking;
    	private File startingDictory;
    	
    	public FileEnumerationTask(BlockingQueue<File> blocking,File startingDictory){
    	    this.blocking = blocking;
    	    this.startingDictory = startingDictory;
    	}
    	
    	@Override
    	public void run() {
    	    try{
    		enumerate(startingDictory);
    		blocking.put(DUMMY);
    	    }catch(InterruptedException e){
    		
    	    }
    	}
    	private void enumerate(File dictory) throws InterruptedException{
    	       File[] files = dictory.listFiles();
    	       for(File file : files){
    		   if(file.isDirectory()){
    		       enumerate(file);
    		   }else{
    		       blocking.put(file);
    		   }
    	       }
    	}
    }
    class SearchTask implements Runnable{
      private BlockingQueue<File> queue;
      private String keyword;
      
      public SearchTask(BlockingQueue<File> queue,String keyword){
    	  this.queue = queue;
    	  this.keyword = keyword;
      }
      @Override
      public void run() {
    	try{
    	    boolean done = false;
    	    while(!done){
    		File file = queue.take();
    		if(file==FileEnumerationTask.DUMMY){//线程搜索到虚拟对象,将其放回 并终止搜索
    		    queue.put(file);
    		    done=true;
    		}else{
    		    search(file);
    		}
    	    }
    	    
    	}catch(IOException e){
    	    e.printStackTrace();
    	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
      }
      private void search(File file) throws IOException{
	  try(Scanner sc = new Scanner(file)){
	      int lineNumber =0;
	      while(sc.hasNextLine()){
		  lineNumber++;
		  String line = sc.nextLine();
		  if(line.contains(keyword)){
		      System.out.printf("%s:%d:%s%n",file.getPath(),lineNumber,line);
		  }
	      }
	  }
      }
    } 
    
    class MatchCounter implements Callable<Integer>{
	private File directory;
	private String keyword;
	private ExecutorService pool;
	private int count;

	public MatchCounter(File directory,String keyword,ExecutorService pool){
	    this.directory = directory;
	    this.keyword = keyword;
	    this.pool = pool;
	}
	
	@Override
	public Integer call() throws Exception {
	    count = 0;
	    try{
		File[] files = directory.listFiles();
		List<Future<Integer>> results = new ArrayList<>();
		
		for(File file : files){
		    if(file.isDirectory()){
			//MatchCounter counter = new MatchCounter(file,keyword);
			//FutureTask<Integer> task = new FutureTask<>(counter);
			//results.add(task);
			//Thread t = new Thread(task);
			//t.start();
			MatchCounter counter = new MatchCounter(file,keyword,pool);
			Future<Integer> task = pool.submit(counter);
			results.add(task);
		    }else{
			if(search(file)){
			    count++;
			}
		    }
		}
		for(Future<Integer> result : results ){
		    try{
			count+=result.get();
		    }catch(ExecutionException e){
			e.printStackTrace();
		    }
		}
		
	    }catch(InterruptedException e){
		e.printStackTrace();
	    }
	    return count;
	}
	private boolean search(File file){
	    try{
		try(Scanner sc = new Scanner(file)){
		    boolean found = false;
		    while(!found && sc.hasNextLine()){
			String line = sc.nextLine();
			if(line.contains(keyword)){
			    found = true;
			}
		    }
		    return found;
		}
	    }catch(IOException E){
		return false;
	    }
	}
    }
    
