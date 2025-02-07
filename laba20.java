import java.util.concurrent.Exchanger;

public class laba20 {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        Thread thread1 = new Thread(() -> {
            try {
                String data = "Сообщение от Потока 1";
                System.out.println("Поток 1 отправляет: " + data);
                String received = exchanger.exchange(data);
                System.out.println("Поток 1 получил: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                String data = "Сообщение от Потока 2";
                System.out.println("Поток 2 отправляет: " + data);
                String received = exchanger.exchange(data);
                System.out.println("Поток 2 получил: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
    }
}
