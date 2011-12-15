package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import java.util.List;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.IUserActionService;

import org.junit.Test;

public class TestUserActions {
	@Test
	public void testIfSnakeChangesMovingDirectionOnUserAction() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		int snakeHeadPositionX = snake.headPosition.tileX;
		int snakeHeadPositionY = snake.headPosition.tileY;
		
		assertTrue(snake.direction == Direction.RIGHT);
		
		gameWorldService.moveSnake();
		assertTrue(snakeHeadPositionX + 1 == snake.headPosition.tileX);
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		
		assertTrue(snake.direction == Direction.DOWN);
		assertTrue(snakeHeadPositionY + 1 == snake.headPosition.tileY);
	}
	
	@Test
	public void testIfSnakeMovementChangesAreCleanupedProperly() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		for (int i = 0; i < snake.newLength / 2 + 1; i++)
			gameWorldService.moveSnake();
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		for (int i = 0; i < snake.newLength; i++)
			gameWorldService.moveSnake();
		
		// this one triggers cleanup when needed
		snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		
		assertTrue(gameWorldService.getGameWorld().movementChangesInEffect.size() == 0);
	}
	
	@Test(expected=SnakeMovementResultedEndOfGameException.class)
	public void testIfEndOfGameComesWhenMovingSnakeIntoItself() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		try	{
			gameWorldService.moveSnake();
		} catch (SnakeMovementResultedEndOfGameException e) {
			fail();
		}
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.UP));
		try {
			gameWorldService.moveSnake();
		} catch (SnakeMovementResultedEndOfGameException e) {
			fail();
		}
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		try {
			gameWorldService.moveSnake();
		} catch (SnakeMovementResultedEndOfGameException e) {
			fail();
		}

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
	}
	
	@Test
	public void testIfSnakesTrailReflectsUserActions() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.moveSnake();
				
		List<WorldPosition> trail = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		assertTrue(trail.get(0).tileX == trail.get(3).tileX
				&& trail.get(0).tileY != trail.get(3).tileY);
	}
	
	@Test
	public void testIfSnakesTrailStaysTheSameAfterMultipleCalls() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.moveSnake();
				
		List<WorldPosition> trail1 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		List<WorldPosition> trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++) {
			assertTrue(trail1.get(i).equals(trail2.get(i)));
		}
		trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++) {
			assertTrue(trail1.get(i).equals(trail2.get(i)));
		}
	}
	
	@Test
	public void testIfDirectionIsChangedOnlyAfterMovement() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		
		Direction direction = snake.direction;
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		assertTrue(direction.equals(snake.direction));
		gameWorldService.moveSnake();
		assertFalse(direction.equals(snake.direction));
	}
	
	@Test
	public void testIfAppliedUserActionBeforeMovementDoesntImpactTrail() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		
		List<WorldPosition> trail1 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		List<WorldPosition> trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++) {
			assertTrue(trail1.get(i).equals(trail2.get(i)));
		}
	}
	
	@Test
	public void testIfUserActionOppositeSnakesDirectionIsIgnored()
	{
		fail();
	}
	
	@Test
	public void testIfMultipleUserActionsBetweenMovementsAreProcessedProperly() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();
		
		List<WorldPosition> trail1 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		
		List<WorldPosition> trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++) {
			assertTrue(trail1.get(i).equals(trail2.get(i + 1)));
		}
		
		trail1 = trail2;
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.moveSnake();
		
		trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++) {
			assertTrue(trail1.get(i).equals(trail2.get(i + 1)));
		}
	}
}
