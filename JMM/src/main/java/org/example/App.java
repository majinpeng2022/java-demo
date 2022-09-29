package org.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JMM JAVA内存模型可见性验证 - volatile
 * 1. 保证可见性
 * 2. 保证有序性
 * 3. 不保证原子性
 */
public class App {
    public static void main(String[] args) {
//        seeOKVolatile();
        MyData myData = new MyData();
        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myData.addPlusPlus();
                    myData.addAtomic();
                }
            }, String.valueOf(i)).start();

        }
        // 需要等待上面20个线程都计算完成，再取最终的计算结果值
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println("num = " + myData.num);
        System.out.println("atomicInteger = " + myData.atomicInteger);
    }

    public static void seeOKVolatile() {
        MyData myData = new MyData();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " \t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addNum(10);
            System.out.println(Thread.currentThread().getName() + " update num = " + myData.getNum());

        }, "sub thread").start();
        while (myData.getNum() == 0) ;
        System.out.println(Thread.currentThread().getName() + " get num = " + myData.getNum());
    }
}

class MyData {
    volatile int num = 0;
    AtomicInteger atomicInteger = new AtomicInteger();

    public int getNum() {
        return num;
    }

    public void addNum(int value) {
        num = num + value;
    }

    public void addPlusPlus() {
        num++;
    }

    public void addAtomic() {
        atomicInteger.getAndIncrement();

    }


}
