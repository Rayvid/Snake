package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;
import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;

import org.junit.Test;

public class TestTimeIsolated {
	@Test
	public void testIfTimerIsAccurateEnough() throws InterruptedException
	{
		ITimeService service = new TimeService();
		
		Thread.sleep(1);
		long start = service.getNanoStamp();
		Thread.sleep(1);
		long intermediate = service.getNanoStamp();
		assertTrue(intermediate - start  > 900000 && intermediate - start < 1500000);
	}
	
	@Test
	public void testIfMovementOccursWhenEnoughTimePassed()
	{
		fail();
	}
}
