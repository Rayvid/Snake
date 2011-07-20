package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;
import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;

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
	public void testSnakesLengthChangesAfterGrowingWhenMoving()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		
		int length = world.getSnake().currLength;
		snakeService.growSnake(world);
		snakeService.moveSnake(world);
		
		assertTrue(length < world.getSnake().currLength);
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
	public void testIfEndOfGameComesWhenMovingSnakeIntoWall()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		
		boolean doesEndOfGameHappened = false;
		for (int i = 0;	i < Math.max(world.getGameWorldWidth(),	world.getGameWorldHeight()); i++) {
			doesEndOfGameHappened = snakeService.moveSnake(world);
			if (doesEndOfGameHappened) { break; }
		}
		
		assertTrue(doesEndOfGameHappened);
	}
	
	@Test
	public void testIfSnakesTrailCoordListContainsSameCountAsSnakeLength()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		Snake snake = world.getSnake();
		
		boolean doesEndOfGameHappened = false;
		for (int i = 0;	i < Math.max(world.getGameWorldWidth(),	world.getGameWorldHeight()); i++) {
			doesEndOfGameHappened = snakeService.moveSnake(world);
			
			assertTrue(snake.currLength == snakeService.generateSnakesTrail(world).size());
			
			if (doesEndOfGameHappened) { break; }
		}
	}
}
