package com.yc.threadpool;

import java.io.IOException;

//任务接口
public interface Taskable {
    public Object doTask() throws IOException;
}
