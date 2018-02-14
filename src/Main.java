import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/*
Лямбды есть в StringProcessor.java
 */
public class Main {
    public static void main(String[] args) {
        int resourcesAmount;
        int DEFAULT_RESOURCE_AMOUNT = 5;
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many files you would like to parse? (Enter an integer)");
        try {
            resourcesAmount = Integer.parseInt(scanner.nextLine());
        } catch (RuntimeException e) {
            System.out.println("You didn't enter an integer. We will use default value for number of files(5)");
            resourcesAmount = DEFAULT_RESOURCE_AMOUNT;
        }

        System.out.println(String.format("Enter %d filenames(One at a time)", resourcesAmount));

        String[] filenames = new String[resourcesAmount];
        String[] resources = new String[resourcesAmount];
        try {
            for (int i = 0; i < resourcesAmount; i++) {
                filenames[i] = scanner.nextLine();
            }
        } catch (RuntimeException e) {
            System.out.println("Something went wrong during filename reading. Stopping the program");
            scanner.close();
            System.exit(1);
        }

        //File reading
        Runnable[] runnables = new Runnable[resourcesAmount];
        for (int i = 0; i < resourcesAmount; i++) {
            runnables[i] = new FileReader(resources, i, filenames[i]);
        }
        RunnableExecutorInterface executor = new RunnableExecutor();
        executor.run(runnables);
        executor.join();

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        AtomicBoolean stop = new AtomicBoolean();
        stop.set(false);

        //String processing
        for (int i = 0; i < resourcesAmount; i++) {
            runnables[i] = new StringProcessor(resources[i], map, stop);
        }
        executor.run(runnables);
        executor.join();

        if (stop.get() == false) {
            System.out.println("Final result");
            for (String key : map.keySet()) {
                System.out.println(key + " " + map.get(key));
            }
        } else {
            System.out.println("English characters were found. Program was stopped.");
        }
    }
}
