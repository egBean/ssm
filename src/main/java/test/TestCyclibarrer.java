package test;

import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;

/**
 *
 */
public class TestCyclibarrer {

    private static CyclicBarrier barrier = new CyclicBarrier(2,() ->{
        System.out.println("hah");
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    });
    public static void main(String[] args) {


        new Thread(()->{
            System.out.println("barrierreleased之前");
            System.out.println("1"+Thread.currentThread().getName());
            try {
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("barrierreleased之后");
        }).start();
        new Thread(()->{
            System.out.println("barrierreleased2之前");
            try {

                System.out.println("2"+Thread.currentThread().getName());
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("barrierreleased2之后");
        }).start();




    }

    public static void hanoi(int n,String start,String middle,String aid){
        if(n==1){
            System.out.println("将"+n+"从"+start+"移动到"+aid);
            return ;
        }
        hanoi(n-1,start,aid,middle);
        System.out.println("将"+n+"从"+start+"移动到"+aid);
        hanoi(n-1,middle,start,aid);


    }
}
