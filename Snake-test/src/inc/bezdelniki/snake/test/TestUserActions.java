package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;
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
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		Snake snake = world.getSnake();
		
		int snakeHeadPositionX = snake.headPosition.tileX;
		int snakeHeadPositionY = snake.headPosition.tileY;
		
		userActionService.applyUserActionChangingSnakeMovement(Direction.RIGHT, world);
		assertTrue(snake.direction == Direction.RIGHT);
		
		snakeService.moveSnake(world);
		assertTrue(snakeHeadPositionX + 1 == snakeHeadPositionX);
		
		userActionService.applyUserActionChangingSnakeMovement(Direction.DOWN, world);
		assertTrue(snake.direction == Direction.DOWN);
		
		snakeService.moveSnake(world);
		assertTrue(snakeHeadPositionY + 1 == snakeHeadPositionY);
	}
	
	@Test
	public void testIfSnakeMovementChangesAreCleanupedProperly()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		
		userActionService.applyUserActionChangingSnakeMovement(Direction.RIGHT, world);
		for (int i = 0; i < world.getSnake().newLength / 2 + 1; i++)
			snakeService.moveSnake(world);
		
		userActionService.applyUserActionChangingSnakeMovement(Direction.DOWN, world);
		snakeService.moveSnake(world);
		
		userActionService.applyUserActionChangingSnakeMovement(Direction.LEFT, world);
		for (int i = 0; i < world.getSnake().newLength - 1; i++)
			snakeService.moveSnake(world);
		
		assertTrue(world.getMovementChangesInEffect().size() == 0);
	}
	
	@Test
	public void testIfEndOfGameComesWhenMovingSnakeIntoItself()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		
		userActionService.applyUserActionChangingSnakeMovement(Direction.RIGHT, world);
		snakeService.moveSnake(world);
		userActionService.applyUserActionChangingSnakeMovement(Direction.DOWN, world);
		snakeService.moveSnake(world);
		userActionService.applyUserActionChangingSnakeMovement(Direction.LEFT, world);
		snakeService.moveSnake(world);
		userActionService.applyUserActionChangingSnakeMovement(Direction.UP, world);
		
		assertTrue(snakeService.moveSnake(world));
	}
	
	@Test
	public void testIfSnakesTrailReflectsUserActions()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		snakeService.createSnake(world);
		
		userActionService.applyUserActionChangingSnakeMovement(Direction.RIGHT, world);
		snakeService.moveSnake(world);
		userActionService.applyUserActionChangingSnakeMovement(Direction.DOWN, world);
		snakeService.moveSnake(world);
		userActionService.applyUserActionChangingSnakeMovement(Direction.LEFT, world);
		snakeService.moveSnake(world);
		
		List<WorldPosition> trail = snakeService.generateSnakesTrail(world);
		assertTrue(trail.get(0).tileX == trail.get(2).tileX
				&& trail.get(0).tileY != trail.get(2).tileY);
	}
}
