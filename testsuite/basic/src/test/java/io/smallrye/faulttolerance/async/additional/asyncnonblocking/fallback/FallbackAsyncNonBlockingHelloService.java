package io.smallrye.faulttolerance.async.additional.asyncnonblocking.fallback;

import static io.smallrye.faulttolerance.core.util.CompletionStages.failedFuture;
import static java.util.concurrent.CompletableFuture.completedFuture;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.faulttolerance.Fallback;

import io.smallrye.faulttolerance.api.AsynchronousNonBlocking;

@ApplicationScoped
public class FallbackAsyncNonBlockingHelloService {
    private volatile Thread helloThread;
    private volatile StackTraceElement[] helloStackTrace;

    private volatile Thread fallbackThread;

    @AsynchronousNonBlocking
    @Fallback(fallbackMethod = "fallback")
    public CompletionStage<String> hello() {
        helloThread = Thread.currentThread();
        helloStackTrace = new Throwable().getStackTrace();
        return failedFuture(new RuntimeException());
    }

    public CompletionStage<String> fallback() {
        fallbackThread = Thread.currentThread();
        return completedFuture("hello");
    }

    Thread getHelloThread() {
        return helloThread;
    }

    StackTraceElement[] getHelloStackTrace() {
        return helloStackTrace;
    }

    Thread getFallbackThread() {
        return fallbackThread;
    }
}
