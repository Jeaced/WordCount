public class RunnableExecutor implements RunnableExecutorInterface {
    Thread[] threads;

    @Override
    public void run(Runnable[] runnables) {
        threads = new Thread[runnables.length];
        for (int i = 0; i < runnables.length; i++) {
            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }
    }

    @Override
    public void join() {
        if (threads != null) {
            try {
                for (int i = 0; i < threads.length; i++) {
                    threads[i].join();
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted exception was caught. Stopping the program");
                System.exit(1);
            }
        }
    }
}
