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
		
		snakeService.createSnake(world);
		assertTrue(world.getSnake().currLength > 0);
		
		snakeService.removeSnake(world);
		assertTrue(world.getSnake() == null);
	}
	
	@Test
	public void testSnakeIsGrowing()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		int length = world.getSnake().newLength;
		snakeService.growSnake(world);
		
		assertTrue(length < world.getSnake().newLength);
	}
	
	@Test
	public void testSnakeIsMoving()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		int oldPosX = world.getSnake().headPosition.tileX;
		int oldPosY = world.getSnake().headPosition.tileY;
		snakeService.moveSnake(world);
		assertTrue(world.getSnake().headPosition.tileX != oldPosX || world.getSnake().headPosition.tileY != oldPosY);
	}
	
	@Test
	public void testIfEndOfGameComes()
	{
		fail();
	}
}
