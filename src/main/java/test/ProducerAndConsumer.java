package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerAndConsumer {

    private List<Object> list = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    private Condition consumer=lock.newCondition();
    private Condition producer=lock.newCondition();

    private int size;

    private int capacity=10;


    public void put(Object obj) throws InterruptedException {
        lock.lock();
        try{
            while(capacity==size){

                producer.await();
            }
            list.add(obj);
            size++;
            consumer.signalAll();
        }finally {
            lock.unlock();
        }

    }

    public synchronized Object get() throws InterruptedException {

        lock.lock();
        try{
            while(size==0){

                consumer.await();
            }
            producer.signalAll();
            size--;
            return list.remove(0);
        }finally {
            lock.unlock();
        }




    }

    public static void main(String[] args) {
        ProducerAndConsumer pac = new ProducerAndConsumer();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("生產者生產了一件商品");
                    try {
                        pac.put(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        pac.get();
                        System.out.println("消費者者消費了一件商品");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}