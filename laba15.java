public class laba15 {

    public static void main(String[] args) {

        Thread thread1 = new Thread(new Task(), "Thread 1");
        Thread thread2 = new Thread(new Task(), "Thread 2");
        Thread thread3 = new Thread(new Task(), "Thread 3");
        Thread thread4 = new Thread(new Task(), "Thread 4");


        thread1.setPriority(Thread.MIN_PRIORITY); // 1
        thread2.setPriority(Thread.NORM_PRIORITY); // 5
        thread3.setPriority(Thread.MAX_PRIORITY); // 10
        thread4.setPriority(Thread.NORM_PRIORITY); // 5


        long startTime = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + (endTime - startTime) + " ms");
    }

    static class Task implements Runnable { // интерфейс Runnable
        @Override // метод run() переопределяет метод из интерфейса Runnable
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000000; i++) {
                Math.sqrt(i);
            }
            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " finished in " + (endTime - startTime) + " ms");
        }
    }
}

