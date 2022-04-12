package com.tz.mall.product.thread;

import com.sun.xml.internal.ws.util.CompletedFuture;
import com.tz.mall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;


public class ThreadTest {
//   创建一个容量为10的线程池
    public static ExecutorService executor=Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CompletableFuture.runAsync(()->{
//            System.out.println("当前线程："+Thread.currentThread().getId());
//            int i=10/2;
//            System.out.println("运行结果："+i);
//        },executor);
        /** 能感知到异常，但不直接处理 exceptionally*/
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).whenComplete((res,exception)->{
//            System.out.println("异步执行完成，结果是："+res+"异常是："+exception);
//        }).exceptionally(t->{
////            感知到异常后，返回一个默认值
//            return 10;
//        });

        /** 能感知到异常，可以直接处理 handle */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 4;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).handle((res,thr)->{
//            if (res!=null){
//                return res*2;
//            }
//            if (thr!=null){
//                return 0;
//            }
//            return 0;
//        });
        /** 串行调用
         * 1.thenRun 不能获取到上一步的执行结果
         * 2.thenAcceptAsync,能接收上一步的结果，但是不能改变返回值
         * 3.thenApplyAsync,能接收上一步的结果，能改变返回值
         * */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 4;
//            System.out.println("运行结果：" + i);
//            return i ;
//        }, executor).thenApplyAsync((res) -> {
//            System.out.println("线程二启动了，上次的返回结果：" + res);
//            return res * 2;
//        }, executor);
//        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            int i = 10 / 4;
//            System.out.println("任务1运行结果：" + i);
//            return i ;
//        }, executor);
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(3000);
//                System.out.println("任务2线程：" + Thread.currentThread().getId());
//
//                System.out.println("任务2运行完成：");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return "Hello" ;
//        }, executor);
/**~~~~~~~~~~~~~~~~~~~~~~~~~~~两个任务都需要完成，才能执行下一个任务*/
        /**runAfterBothAsync,无法接收参数*/
//        future01.runAfterBothAsync(future02,()->{
//            System.out.println("任务三线程");
//        },executor);
//        Integer i = future.get();
//        System.out.println("System is end    "+i);
        /**thenAcceptBothAsync,可以接收参数*/
//        future01.thenAcceptBothAsync(future02,(f1,f2)->{
//            System.out.println("任务三线程,f1结果："+f1+"  f2结果："+f2);
//        },executor);
        /**thenCombineAsync,可以接收参数，可以返回结果*/
//        CompletableFuture<String> s = future01.thenCombineAsync(future02, (f1, f2) -> {
//            return f2 + "  " + f1;
//        }, executor);

/**~~~~~~~~~~~~~~~~~~~~~~两个任务只要完成一个，就可以执行下一个任务*/
        /** runAfterEitherAsync,不接收参数*/
//        future01.runAfterEitherAsync(future02,()->{
//            System.out.println("任务三线程");
//        },executor);
//        System.out.println("返回结果: "+s.get());
        /** acceptEitherAsync,接收参数, 但没有返回结果*/
//        future01.acceptEitherAsync(future02,(f1)->{
//            System.out.println("任务三线程,f1结果："+f1);
//        },executor);
//        CompletableFuture<Object> future = future01.applyToEitherAsync(future02, (res) -> {
//
//            String ss = "任务三线程,结果之一：" + res;
//            System.out.println(ss);
//            return ss;
//        }, executor);
/**~~~~~~~~~~~~~~~~~~~~~~多任务组合，都完成了，才可以可以执行下一个任务*/
        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品图片信息");
            return "hello.jpg";
        }, executor);
        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品属性信息");
            return "黑色，256G";
        }, executor);
        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("查询商品描述信息");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "IPhone ";
        }, executor);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
        allOf.get();
        System.out.println(futureImg.get()+"=>"+futureAttr.get()+"=>"+futureDesc.get());
    }


    public static void thread(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("System is starting");
//        Thread01 thread01 = new Thread01();
//        thread01.start();
//        Runnalble01 runnalble01 = new Runnalble01();
//        new Thread(runnalble01).start();
//
//        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
//        new Thread(futureTask).start();
//        Integer integer = futureTask.get();
        Executors.newFixedThreadPool(200);
        executor.execute(new Runnalble01());
        System.out.println("System is end    ");
    }
    public static class Thread01 extends Thread{
        @Override
        public void run(){
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i=10/2;
            System.out.println("运行结果："+i);

        }
    }


    public static class Runnalble01 implements Runnable{

        @Override
        public void run() {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i=10/2;
            System.out.println("运行结果："+i);

        }
    }

    public static class Callable01 implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i=10/2;
            System.out.println("运行结果："+i);
            return i;
        }
    }

}
