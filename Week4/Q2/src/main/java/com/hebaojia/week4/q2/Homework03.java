package com.hebaojia.week4.q2;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Homework03 {
    public static void main(String[] args) {
        /*
	        long start = System.currentTimeMillis();
	
	        // 在这里创建一个线程或线程池，
	        // 异步执行 下面方法
	    
	        int result = sum(); //这是得到的返回值
	        
	        // 确保  拿到result 并输出
	        System.out.println("同步计算结果为："+result);
	        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
	        
	        // 然后退出main线程
        */
        
        final CountDownLatch latch = new CountDownLatch(6);
        
        // 方法1 复写Thread run方法
        Demo1_ExtendThread demo1 = new Demo1_ExtendThread(latch);
        demo1.start();
        
        // 方法2 实现Runnable
        Thread demo2 = new Thread(new RunnableImpl("Demo2_RunnableImpl", latch));
        demo2.start();
        
        // 方法3 直接调用法
        new Thread(new Runnable() {
			public void run() {
				long start = System.currentTimeMillis();
		        int result = sum(); //这是得到的返回值
		        System.out.println("Demo3_Directly 异步计算结果为："+result);
		        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
		        latch.countDown();
			}
        }).start();
        
        // 方法4 实现Callable
        Callable<Integer> tc = new CallableImpl("Demo4_CallableImpl", latch);
        FutureTask<Integer> task = new FutureTask<Integer>(tc);
        new Thread(task).start();
        try {
			System.out.println("异步计算结果为：" + task.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // 方法5和6 使用线程池(创建线程池有4种方法)
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(new RunnableImpl("Demo5_ThreadPool_Runnable", latch));
        Future<Integer> result = pool.submit(new CallableImpl("Demo6_ThreadPool_CallableImpl", latch));
        try {
			System.out.println("异步计算结果为：" + result.get());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        pool.shutdown();
        
        try {
			latch.await();
			System.out.println("CountDown: " + latch.getCount());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // 退出main线程
        System.out.println("退出main线程");
        
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
