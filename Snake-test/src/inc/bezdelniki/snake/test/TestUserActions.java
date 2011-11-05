package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import java.util.List;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.IUserActionService;

import org.junit.Test;

public class TestUserActions {
	@Test
	public void testIfSnakeChangesMovingDirectionOnUserAction()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		int snakeHeadPositionX = snake.headPosition.tileX;
		int snakeHeadPositionY = snake.headPosition.tileY;
		
		assertTrue(snake.direction == Direction.RIGHT);
		
		snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		assertTrue(snakeHeadPositionX + 1 == snake.headPosition.tileX);
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		assertTrue(snake.direction == Direction.DOWN);
		
		snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		assertTrue(snakeHeadPositionY + 1 == snake.headPosition.tileY);
	}
	
	@Test
	public void testIfSnakeMovementChangesAreCleanupedProperly()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		for (int i = 0; i < snake.newLength / 2 + 1; i++)
			snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		for (int i = 0; i < snake.newLength; i++)
			snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		
		// this one triggers cleanup when needed
		snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		
		assertTrue(gameWorldService.getGameWorld().movementChangesInEffect.size() == 0);
	}
	
	@Test
	public void testIfEndOfGameComesWhenMovingSnakeIntoItself()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		assertFalse(snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect));
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		assertFalse(snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect));
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		assertFalse(snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect));

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.UP));
		assertTrue(snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect));
	}
	
	@Test
	public void testIfSnakesTrailReflectsUserActions()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		snakeService.moveSnake(snake, gameWorldService.getGameWorld().movementChangesInEffect);
				
		List<WorldPosition> trail = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		assertTrue(trail.get(0).tileX == trail.get(3).tileX
				&& trail.get(0).tileY != trail.get(3).tileY);
	}
}
