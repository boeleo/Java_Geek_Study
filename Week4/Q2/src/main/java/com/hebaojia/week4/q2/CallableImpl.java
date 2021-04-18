package com.hebaojia.week4.q2;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class CallableImpl implements Callable<Integer> {

	private String name;
	private CountDownLatch latch;

	public CallableImpl(String name, CountDownLatch latch) {
		this.name = name;
		this.latch = latch;
	}

	public Integer call() throws Exception {
		long start = System.currentTimeMillis();
        int result = sum(); //这是得到的返回值
        System.out.println(this.name + " 使用时间："+ (System.currentTimeMillis()-start) + " ms");
        this.latch.countDown();
        return result;
	}
	
	private static int sum() {
        return fibo(36);
    }
    
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

}
