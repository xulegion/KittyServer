package com.yc.threadpool;

import java.io.IOException;

public class SimpleThread extends Thread {
    private boolean runningFlag;   //运行的状态 false
    private Taskable task;     // 要执行的操作
    private int threadNumber;   // 线程的编译
    private MyNotify myNotify;  // 通知接口

    // 标志 runningFlag 用来
    public boolean isRunning(){
        return this.runningFlag;
    }

    public synchronized void setRunning( boolean flag ){
        this.runningFlag =flag;  //设置当前线程为 true, 表示当前线程已被占用
        if ( flag ) {
            this.notifyAll();  // 通知其他线程
        }
    }
    public Taskable getTask(){
        return task;
    }

    public void setTask(Taskable task){
        this.task = task;
    }
    // 提示那个线程工作
    public SimpleThread(int threadNumber, MyNotify notify){
        runningFlag = false;
        this.threadNumber = threadNumber;
        System.out.println("Thread" + threadNumber + "started.");
        this.myNotify = notify;
    }

    @Override
    public synchronized void run(){
        try {
            while ( true ){
                if ( !runningFlag ){   //无线循环
                    this.wait();  //判断标志位是否为 true,如果 runningFlag=false 等调用
                } else {
                    System.out.println("线程"+threadNumber+"开始执行任务");
                    Object returnValue = null;
                    try {
                        returnValue = this.task.doTask();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if( myNotify!=null ){
                        myNotify.notifyResult( returnValue );
                    }
                    //sleep(5000);
                    System.out.println("线程" + threadNumber + "重新准备...");
                    setRunning( false );
                }
            }
        } catch (InterruptedException e){
            System.out.println("INterrupt");
        }
    }
}
