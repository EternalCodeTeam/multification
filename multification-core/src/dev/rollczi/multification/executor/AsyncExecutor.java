package dev.rollczi.multification.executor;

@FunctionalInterface
public interface AsyncExecutor {

    void execute(Runnable runnable);

}
