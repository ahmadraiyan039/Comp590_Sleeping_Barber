import java.util.concurrent.Semaphore;

class DiningPhilosophers {
    private Semaphore[] forks;
    private Semaphore diningSemaphore;

    public DiningPhilosophers(int numPhilosophers) {
        forks = new Semaphore[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Semaphore(1);
        }
        diningSemaphore = new Semaphore(numPhilosophers - 1);
    }

    public void startDining(int philosopherId) {
        try {
            while (true) {
                think(philosopherId);
                takeForks(philosopherId);
                eat(philosopherId);
                putForks(philosopherId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think(int philosopherId) throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is thinking.");
        Thread.sleep(1000); // Simulating thinking
    }

    private void takeForks(int philosopherId) throws InterruptedException {
        diningSemaphore.acquire();
        forks[philosopherId].acquire();
        forks[(philosopherId + 1) % forks.length].acquire();
    }

    private void eat(int philosopherId) throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is eating.");
        Thread.sleep(1000); // Simulating eating
    }

    private void putForks(int philosopherId) {
        forks[philosopherId].release();
        forks[(philosopherId + 1) % forks.length].release();
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

