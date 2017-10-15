package com.example.rvs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.*;

@Service
public class RestThreadPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestThreadPool.class);

    private final int THREADS = 4;
    private final long KEEP_ALIVE_TIME = 3L;
    private final int QUEUE_CAPACITY = 100;
    private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    private final Executor executor = new ThreadPoolExecutor(THREADS, THREADS, KEEP_ALIVE_TIME, TimeUnit.SECONDS, queue);

    <T> DeferredResult<T> execute(Callable<T> callable) {

        DeferredResult<T> result = new DeferredResult<>();
        try {
            executor.execute(() -> {
                try {
                    result.setResult(callable.call());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RejectedExecutionException e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new RestException("Server unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return result;

    }

}
