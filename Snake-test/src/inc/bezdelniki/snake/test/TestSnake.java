package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;
import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.snake.ISnakeService;

import org.junit.Test;

public class TestSnake {
	@Test
	public void testSnakeIsCreatedAndCleanupedProperly()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.RemoveSnake(world);
		snakeService.CreateSnake(world);
		assertTrue(world.getSnake().currLength > 0);
		
		snakeService.RemoveSnake(world);
		assertTrue(world.getSnake() == null);
	}
}
