package com.jon.thread;

import org.junit.Test;

public class BankTest {

    @Test
    public void transfer3WorksWithCondition() throws Exception {
        // 修复前 Lock.wait()/notifyAll() 每次都会抛 IllegalMonitorStateException
        Bank bank = new Bank(2, 1000);
        bank.transfer3(0, 1, 100);
        bank.transfer3(1, 0, 100);
    }
}
