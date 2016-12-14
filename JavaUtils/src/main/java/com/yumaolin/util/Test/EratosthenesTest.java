package com.yumaolin.util.Test;

import java.util.BitSet;

import org.junit.Test;

public class EratosthenesTest {

    @Test
    public void Eratosthenes() {
	int n = 2000000;
	long start = System.currentTimeMillis();
	BitSet b = new BitSet(n + 1);
	int count = 0;
	int i;
	for (i = 2; i <= n; i++) b.set(i);
	i = 2;
	while (i * i <= n) {//sqrt(n),思考为什么是到这个数后面的数就都确定了,在往上加的话,相对于i的另一个数就是比i小的数,计算重复
	    if (b.get(i)) {// 如果该位是质数
		count++;
		int k = 2 * i;
		while (k <= n) {
		    b.clear(k);
		    k += i;// k是i的倍数,将第k位移除
		}
	    }
	    i++;
	}
	while (i <= n) {// 计算sqrt(n)后面的数
	    if (b.get(i))
		count++;
	    i++;
	}
	long end = System.currentTimeMillis();
	System.out.println(count + "primes");
	System.out.println((end - start) + "milliseconds");
    }
}
