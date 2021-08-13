package com.yc.threadpool;

import java.util.Vector;

public class ThreadPoolManger {
    private int maxThread;    //
    private Vector vector;    // Vector类可以实现可增长的对象数组
    private MyNotify notify;

    public void setMaxThread(int threadCount) {
        this.maxThread = threadCount;
    }

    public ThreadPoolManger(int threadCount, MyNotify notify ){
        this.notify = notify;
        this.setMaxThread( threadCount );

        System.out.println("线程池开始运行了...");

        vector = new Vector();
        for (int i = 1; i <=threadCount ; i++) {
            SimpleThread thread = new SimpleThread(i,this.notify);
            vector.addElement( thread );   //将指定的组件添加到此向量的末尾，将其大小增加 1
            thread.start();
        }
    }

    public void process( Taskable task){
        int i;
        for (i = 0; i <vector.size() ; i++) {
            SimpleThread currentThread = (SimpleThread) vector.elementAt( i );  //返回指定索引处的组件
            if ( !currentThread.isRunning()){
                System.out.println("Thread" +(i+1) +"开始执行新任务了");
                currentThread.setTask(task);
                currentThread.setRunning( true );
                return;
            }
        }
        System.out.println("==============");
        System.out.println("线程池中没有空闲的线程");
        System.out.println("==============");
        if ( i==vector.size() ){
            int temp = maxThread;
            this.setMaxThread(maxThread+10);
            for (int j=temp+1;j<= maxThread; j++){
                SimpleThread thread = new SimpleThread(j,this.notify);
                vector.addElement( thread );
                thread.start();
            }
            //创建完之后需要重新执行 process
            this.process( task );
        }
    }
}
