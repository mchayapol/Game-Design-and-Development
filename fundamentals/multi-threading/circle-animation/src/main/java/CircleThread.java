
public class CircleThread implements Runnable {

    private Circle circle;

    public CircleThread(Circle circle) {
        this.circle = circle;
    }

    @Override
    public void run() {
        for (int i = 0; i < 500; i++) { // Move the circle down 100 times
            try {
                circle.moveDown();  // Game object performs action.
                Thread.sleep(50); // Control the speed of the circle movement
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
