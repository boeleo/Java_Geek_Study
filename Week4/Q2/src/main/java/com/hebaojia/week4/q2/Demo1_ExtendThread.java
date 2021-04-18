package com.hebaojia.week4.q2;

import java.util.concurrent.CountDownLatch;

public class Demo1_ExtendThread extends Thread {
	
	private CountDownLatch latch;

	public Demo1_ExtendThread(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		int result = sum(); //这是得到的返回值
		System.out.println("Demo1_ExtendThread 异步计算结果为："+result);
		System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
		this.latch.countDown();
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
