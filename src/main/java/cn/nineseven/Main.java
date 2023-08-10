package cn.nineseven;


import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<String> task = new FutureTask<>(() -> "hello");
        executor.submit(task);
        System.out.println(task.get());
        executor.shutdown();
    }
}
