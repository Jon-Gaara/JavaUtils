package com.jon.thread;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Bank implements Serializable {
    private final double[] accounts;
    private Lock bankLock;
    private Condition sufficientFunds;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock readLock = rwl.readLock();
    private Lock writeLock = rwl.writeLock();
    private static final int aaa = 111;
    private static final long bbbb = 22222;
    private static final double cccc = 33333d;
    private static final float dddd = 123123123f;

    public Bank(int n, double initialBalance) {
        accounts = new double[n];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = initialBalance;
        }
        bankLock = new ReentrantLock();
        sufficientFunds = bankLock.newCondition();
    }

    public void transfer(int from, int to, double amount) throws InterruptedException {
        bankLock.lock();
        try {
            while (accounts[from] < amount) {
                sufficientFunds.await();
            }
            System.out.println(Thread.currentThread());
            accounts[from] -= amount;
            System.out.printf(" %10.2f from %d to %d", amount, from, to);
            accounts[to] += amount;
            System.out.printf(" Total Balance: %10.2f%n", getTotalBalance());
            sufficientFunds.signalAll();
        } finally {
            bankLock.unlock();
        }
    }

    public synchronized void transfer2(int from, int to, double amount) throws InterruptedException {
        while (accounts[from] < amount) {
            wait();
        }
        System.out.println(Thread.currentThread());
        accounts[from] -= amount;
        System.out.printf(" %10.2f from %d to %d", amount, from, to);
        accounts[to] += amount;
        System.out.printf(" Total Balance: %10.2f%n", getTotalBalance2());
        notifyAll();
    }

    public void transfer3(int from, int to, double amount) throws InterruptedException {
        writeLock.lock();
        try {
            while (accounts[from] < amount) {
                writeLock.wait();
            }
            System.out.println(Thread.currentThread());
            accounts[from] -= amount;
            System.out.printf(" %10.2f from %d to %d", amount, from, to);
            accounts[to] += amount;
            System.out.printf(" Total Balance: %10.2f%n", getTotalBalance3());
        } finally {
            writeLock.unlock();
        }
        writeLock.notifyAll();
    }

    private double getTotalBalance() {
        bankLock.lock();
        try {
            double sum = 0;
            for (double d : accounts) {
                sum += d;
            }
            return sum;
        } finally {
            bankLock.unlock();
        }
    }

    private synchronized double getTotalBalance2() {
        double sum = 0;
        for (double d : accounts) {
            sum += d;
        }
        return sum;
    }

    private double getTotalBalance3() {
        readLock.lock();
        try {
            double sum = 0;
            for (double d : accounts) {
                sum += d;
            }
            return sum;
        } finally {
            readLock.unlock();
        }
    }

    public int Size() {
        return accounts.length;
    }

    public static void main(String[] args) {
        int from = 9;
        int to = 1;
        double amount = 1000;
        int n = 10;
        Bank bank = new Bank(n, amount * 10);
        for (int i = 0; i < n; i++) {
            Thread t = new Thread(() -> {
                try {
                    bank.transfer(from, to, amount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }
}
