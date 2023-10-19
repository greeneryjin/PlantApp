package com.example.demo.headerconfig;

import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    /**
     * 비동기를 사용하기 위해 스레드풀 생성
     * */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    //기본 스레드 개수
        executor.setMaxPoolSize(20);    //최대 스레드 개수
        executor.setQueueCapacity(50);  //최대 스레드 개수 초과시 수용가능한 스레드 큐(내부에 스레드)
        executor.setThreadNamePrefix("Executor-"); //생성되는 스레드 접두사
        executor.initialize();
        return executor;
    }
}
