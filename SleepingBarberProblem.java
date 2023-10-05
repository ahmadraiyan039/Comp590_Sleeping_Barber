import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BarberShop {
    private int totalChairs;
    private Queue<Customer> waitingRoom;
    private Lock lock;
    private Condition barberCondition;
    private Condition customerCondition;

    public BarberShop(int totalChairs) {
        this.totalChairs = totalChairs;
        this.waitingRoom = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.barberCondition = lock.newCondition();
        this.customerCondition = lock.newCondition();
    }

    public void barber() {
        while (true) {
            lock.lock();
            try {
                while (waitingRoom.isEmpty()) {
                    System.out.println("Barber is sleeping.");
                    barberCondition.await();
                }

                Customer customer = waitingRoom.poll();
                System.out.println("Barber is cutting hair for Customer " + customer.getId());
                Thread.sleep(100); // Simulate hair cutting
                System.out.println("Barber finished cutting hair for Customer " + customer.getId());
                customerCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void customer(Customer customer) {
        lock.lock();
        try {
            if (waitingRoom.size() < totalChairs) {
                waitingRoom.add(customer);
                System.out.println("Customer " + customer.getId() + " arrived and is waiting.");
                barberCondition.signal();
                customerCondition.await();
                System.out.println("Customer " + customer.getId() + " finished getting a haircut.");
            } else {
                System.out.println("Customer " + customer.getId() + " left because the waiting room is full.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Customer implements Runnable {
    private static int idCounter = 1;
    private int id;
    private BarberShop shop;

    public Customer(BarberShop shop) {
        this.id = idCounter++;
        this.shop = shop;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        shop.customer(this);
    }
}

public class SleepingBarberProblem {
    public static void main(String[] args) {
        BarberShop shop = new BarberShop(3); // Change the number of chairs as needed
        Thread barberThread = new Thread(shop::barber);
        barberThread.start();

        for (int i = 0; i < 10; i++) {
            Thread customerThread = new Thread(new Customer(shop));
            customerThread.start();
            try {
                Thread.sleep(200); // Simulate random customer arrival times
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
