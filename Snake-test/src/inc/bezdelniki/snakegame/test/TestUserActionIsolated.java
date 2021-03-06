package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.UserActionService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChangeAction;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestUserActionIsolated
{
	private Injector _testInjectorInstance;
	private IUserActionService _mockedUserActionService;

	public TestUserActionIsolated()
	{
		_mockedUserActionService = createMock(IUserActionService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IUserActionService.class,
						_mockedUserActionService,
						IUserActionService.class));
	}

	@Test
	public void testIfSnakeChangesMovingDirectionOnUserAction() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;

		WorldPosition snakeHeadPosition = (WorldPosition) snake.headPosition.clone();

		gameWorldService.moveSnake();
		assertTrue(snakeHeadPosition.tileX + 1 == snake.headPosition.tileX);

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();

		assertTrue(snake.direction == Direction.DOWN);
		assertTrue(snakeHeadPosition.tileY + 1 == snake.headPosition.tileY);
	}

	@Test
	public void testIfSnakeMovementChangesAreCleanupedProperly() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		ISnakeService snakeService = _testInjectorInstance.getInstance(SnakeService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;

		for (int i = 0; i < snake.newLength / 2 + 1; i++)
			gameWorldService.moveSnake();

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		for (int i = 0; i < snake.newLength; i++)
			gameWorldService.moveSnake();

		List<SnakeMovementChangeAction> movementChangesInEffect = gameWorldService.getGameWorld().movementChangesInEffect;
		// this triggers cleanup when needed
		snakeService.getSnakesTrail(snake, movementChangesInEffect);
		//
		assertTrue(movementChangesInEffect.size() == 0);
	}

	@Test(expected = SnakeMovementResultedEndOfGameException.class)
	public void testIfEndOfGameComesWhenMovingSnakeIntoItself() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		try
		{
			gameWorldService.moveSnake();
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
			fail();
		}

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.UP));
		try
		{
			gameWorldService.moveSnake();
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
			fail();
		}

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		try
		{
			gameWorldService.moveSnake();
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
			fail();
		}

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
	}

	@Test
	public void testIfSnakesTrailReflectsUserActions() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		ISnakeService snakeService = _testInjectorInstance.getInstance(SnakeService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.moveSnake();

		List<WorldPosition> trail = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		assertTrue(trail.get(0).tileX == trail.get(3).tileX && trail.get(0).tileY != trail.get(3).tileY);
	}

	@Test
	public void testIfSnakesTrailStaysTheSameAfterMultipleCalls() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		ISnakeService snakeService = _testInjectorInstance.getInstance(SnakeService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

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
		for (int i = 0; i < trail1.size(); i++)
		{
			assertTrue(trail1.get(i).equals(trail2.get(i)));
		}
		trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++)
		{
			assertTrue(trail1.get(i).equals(trail2.get(i)));
		}
	}

	@Test
	public void testIfDirectionIsChangedOnlyAfterMovement() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

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
	public void testIfAppliedUserActionDoesntImpactTrailUntilMoved() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		ISnakeService snakeService = _testInjectorInstance.getInstance(SnakeService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();

		List<WorldPosition> trail1 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		List<WorldPosition> trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++)
		{
			assertTrue(trail1.get(i).equals(trail2.get(i)));
		}
	}

	@Test
	public void testIfUserActionOppositeSnakesDirectionIsIgnored() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;

		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		WorldPosition prevHeadPosition = (WorldPosition) snake.headPosition.clone();
		gameWorldService.moveSnake();

		assertTrue(prevHeadPosition.tileX + 1 == snake.headPosition.tileX);
		assertTrue(snake.direction == Direction.RIGHT);
	}

	@Test
	public void testIfMultipleUserActionsBetweenMovementsAreProcessedProperly() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(GameWorldService.class);
		ISnakeService snakeService = _testInjectorInstance.getInstance(SnakeService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(DeviceService.class);
		IUserActionService userActionService = new UserActionService(deviceService);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;

		List<WorldPosition> trail1 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.moveSnake();

		List<WorldPosition> trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++)
		{
			assertTrue(trail1.get(i).equals(trail2.get(i + 1)));
		}
		assertTrue(trail2.get(0).tileY == trail1.get(0).tileY + 1);

		trail1 = trail2;
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.DOWN));
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.LEFT));
		gameWorldService.moveSnake();

		trail2 = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (int i = 0; i < trail1.size(); i++)
		{
			assertTrue(trail1.get(i).equals(trail2.get(i + 1)));
		}
		assertTrue(trail2.get(0).tileX == trail1.get(0).tileX - 1);
	}

	@Test
	public void testIfCreatePauseControlCreatesPauseAction()
	{
		IControlService controlService = _testInjectorInstance.getInstance(IControlService.class);

		expect(_mockedUserActionService.createPauseAction()).andReturn(null);
		replay(_mockedUserActionService);

		controlService.CreatePauseControl();
		verify(_mockedUserActionService);
	}

	@Test
	public void testIfCreateArrowPadControlCreatesFourSnakeMovementChangeActions()
	{
		IControlService controlService = _testInjectorInstance.getInstance(IControlService.class);
		
		expect(_mockedUserActionService.createSnakeMovementChange(isA(Snake.class), isA(Direction.class))).andReturn(null);
		expect(_mockedUserActionService.createSnakeMovementChange(isA(Snake.class), isA(Direction.class))).andReturn(null);
		expect(_mockedUserActionService.createSnakeMovementChange(isA(Snake.class), isA(Direction.class))).andReturn(null);
		expect(_mockedUserActionService.createSnakeMovementChange(isA(Snake.class), isA(Direction.class))).andReturn(null);
		replay(_mockedUserActionService);
		
		controlService.CreateArrowPadControl();
		verify(_mockedUserActionService);
	}
}
