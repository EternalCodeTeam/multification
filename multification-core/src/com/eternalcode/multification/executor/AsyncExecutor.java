package com.eternalcode.multification.executor;

@FunctionalInterface
public interface AsyncExecutor {

    void execute(Runnable runnable);

}
