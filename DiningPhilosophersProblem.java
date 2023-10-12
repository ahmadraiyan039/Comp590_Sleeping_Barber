import java.util.concurrent.Semaphore;

class DiningPhilosophers {
    private Semaphore[] utensils; //Semaphore for the utensils
    private Semaphore diningSemaphore; //Semaphore to limit the number

    public DiningPhilosophers(int numPhilosophers) {
        forks = new Semaphore[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            utensils[i] = new Semaphore(1); //Each fork is available at the beginning
        }
        diningSemaphore = new Semaphore(numPhilosophers - 1); //Limiting philosophers
    }

    //Start dining for individual philosophers
    public void startDining(int philosopherId) {
        try {
            while (true) {
                startThinking(philosopherId);
                takeUtensils(philosopherId);
                startEating(philosopherId);
                putUtensils(philosopherId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //Simulates the philosophers thinking
    private void startThinking(int philosopherId) throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is thinking.");
        Thread.sleep(1000); // Simulating thinking
    }

    //Simulate individual philosopher picking up utensils
    private void takeUtensils(int philosopherId) throws InterruptedException {
        diningSemaphore.acquire();
        utensils[philosopherId].acquire(); //Picking up left utensil
        utensils[(philosopherId + 1) % forks.length].acquire(); //picking up right utensil
    }

    //Philosopher eating
    private void startEating(int philosopherId) throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is eating.");
        Thread.sleep(1000); // Simulating eating
    }

    //Putting back utensils
    private void putUtensils(int philosopherId) {
        utensils[philosopherId].release();
        utensils[(philosopherId + 1) % forks.length].release(); //Releasing utensils
        diningSemaphore.release();
    }
}

public class DiningPhilosophersProblem {
    public static void main(String[] args) {
        int numPhilosophers = 5; // Adjust the number of philosophers as needed
        DiningPhilosophers diningPhilosophers = new DiningPhilosophers(numPhilosophers);

        for (int i = 0; i < numPhilosophers; i++) {
            final int philosopherId = i;
            Thread philosopherThread = new Thread(() -> diningPhilosophers.startDining(philosopherId));
            philosopherThread.start();
        }
    }
}

