import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationPanel extends JPanel {
    private Habitat habitat;
    private boolean showTime = false;
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public SimulationPanel() {
        habitat = new Habitat(500, 500);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    startSimulation();
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    stopSimulation();
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    toggleTimeDisplay();
                }
            }
        });
        setFocusable(true);
    }

    private void startSimulation() {
        habitat.startSimulation();
        executor.submit(() -> {
            Random random = new Random();
            while (habitat.isSimulating()) {
                if (random.nextInt(100) < 10) {
                    // Генерируем разработчика с вероятностью 10%
                    habitat.addEntity(new Developer());
                }
                if (random.nextInt(100) < 5 && habitat.getEntities().size() < 100) {
                    // Генерируем менеджера с вероятностью 5%
                    habitat.addEntity(new Manager());
                }
                long timeElapsed = System.currentTimeMillis() - habitat.getSimulationTime();
                habitat.update(timeElapsed);
                repaint();
                try {
                    Thread.sleep(1000); // Обновление раз в 1 секунду
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void stopSimulation() {
        habitat.stopSimulation();
        executor.shutdownNow();
        repaint();
    }

    private void toggleTimeDisplay() {
        showTime = !showTime;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showTime) {
            g.setColor(Color.BLACK);
            g.drawString("Simulation Time: " + habitat.getSimulationTime() / 1000 + "s", 20, 20);
        }

        for (IBehaviour entity : habitat.getEntities()) {
            if (entity instanceof Developer) {
                ((Developer) entity).render(g);
            } else if (entity instanceof Manager) {
                ((Manager) entity).render(g);
            }
        }

        if (!habitat.isSimulating()) {
            g.setColor(Color.BLACK);
            g.drawString("Simulation Finished!", 20, 40);
        }
    }
}
