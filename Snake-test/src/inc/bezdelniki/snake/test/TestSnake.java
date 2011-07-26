package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import org.junit.Test;

public class TestSnake {
	@Test
	public void testSnakeIsCreatedProperly()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		
		Snake snake = snakeService.createSnake();
		assertTrue(snake != null);
	}
	
	@Test
	public void testSnakeIsGrowing()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		
		Snake snake = snakeService.createSnake();
		int length = snake.newLength;
		snakeService.growSnake(snake);
		
		assertTrue(length < snake.newLength);
	}
	
	@Test
	public void testSnakesLengthChangesAfterGrowingWhenMoving()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		
		Snake snake = snakeService.createSnake();
		int length = snake.currLength;
		snakeService.growSnake(snake);
		snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
		
		assertTrue(length < snake.currLength);
	}
	
	@Test
	public void testSnakeIsMoving()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		
		Snake snake = snakeService.createSnake();
		
		WorldPosition oldPos = null;
		try {
			oldPos = (WorldPosition)snake.headPosition.clone();
		} catch (CloneNotSupportedException e) { }
		snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
		
		assertTrue(!snake.headPosition.equals(oldPos));
	}
	
	@Test
	public void testIfEndOfGameComesWhenMovingSnakeIntoWall()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IAppSettingsService appSettingsService = SnakeInjector.getInjectorInstance().getInstance(IAppSettingsService.class);
		
		Snake snake = snakeService.createSnake();
		AppSettings appSettings = appSettingsService.getAppSettings();
		
		boolean doesEndOfGameHappened = false;
		for (int i = 0;	i < Math.max(appSettings.tilesHorizontally, appSettings.tilesVertically); i++) {
			doesEndOfGameHappened = snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
			if (doesEndOfGameHappened) { break; }
		}
		
		assertTrue(doesEndOfGameHappened);
	}
	
	@Test
	public void testIfSnakesTrailCoordListContainsSameCountAsSnakeLength()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IAppSettingsService appSettingsService = SnakeInjector.getInjectorInstance().getInstance(IAppSettingsService.class);
		
		Snake snake = snakeService.createSnake();
		AppSettings appSettings = appSettingsService.getAppSettings();
		
		boolean doesEndOfGameHappened = false;
		for (int i = 0;	i < Math.max(appSettings.tilesHorizontally, appSettings.tilesVertically); i++) {
			doesEndOfGameHappened = snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
			
			assertTrue(snake.currLength == snakeService.getSnakesTrail(snake, new ArrayList<SnakeMovementChange>()).size());
			
			if (doesEndOfGameHappened) { break; }
		}
	}
}
