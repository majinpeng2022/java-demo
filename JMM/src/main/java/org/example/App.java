package org.example;

import java.util.concurrent.TimeUnit;

/**
 * JMM JAVA内存模型可见性验证 - volatile
 */
public class App {
    public static void main(String[] args) {
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
    private volatile int num = 0;

    public int getNum() {
        return num;
    }

    public void addNum(int value) {
        num = num + value;
    }
}
