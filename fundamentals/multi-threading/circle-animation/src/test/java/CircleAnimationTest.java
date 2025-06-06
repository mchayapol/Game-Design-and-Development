import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircleAnimationTest {
    private CircleAnimation circleAnimation;

    @BeforeEach
    public void setUp() {
        circleAnimation = new CircleAnimation();
    }

    @Test
    public void testAnimationStarts() {
        circleAnimation.startAnimation();
        assertTrue(circleAnimation.isAnimationRunning(), "Animation should be running after start.");
    }

    @Test
    public void testCirclesMove() throws InterruptedException {
        circleAnimation.startAnimation();
        Thread.sleep(1000); // Allow some time for circles to move

        for (Circle circle : circleAnimation.getCircles()) {
            assertTrue(circle.getY() > 0, "Circle should have moved down from the top.");
        }
    }
}