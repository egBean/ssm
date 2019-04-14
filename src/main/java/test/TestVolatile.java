package test;

import java.util.concurrent.TimeUnit;

public class TestVolatile {

    private static boolean flag=true;


    public static void main(String[] args) throws Exception{

        new Thread(()->{
            while(flag){
                /*try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                //System.out.println("zzzz");


            }
        }).start();

        TimeUnit.MILLISECONDS.sleep(2000);
        System.out.println("flag修改为false");
        flag=false;
        System.out.println("flag修改为false完成");

    }
}
