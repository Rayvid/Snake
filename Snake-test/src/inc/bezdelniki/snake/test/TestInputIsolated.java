package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.UserActionService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestInputIsolated
{
	private Injector _testInjectorInstance;
	private IInputService _mockedInputService;

	private class TestInputBindingsConfiguration extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IRuntimeParamsService.class).to(RuntimeParamsService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(ITimeService.class).to(TimeService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(ILyingItemService.class).to(LyingItemService.class);
			bind(IGameWorldService.class).to(GameWorldService.class).in(Singleton.class);
			bind(IUserActionService.class).to(UserActionService.class);

			bind(IInputService.class).toInstance(_mockedInputService);
		}
	}

	public TestInputIsolated()
	{
		_mockedInputService = createMock(IInputService.class);
		_testInjectorInstance = Guice.createInjector(new TestInputBindingsConfiguration());
	}

	@Test
	public void testIfCorrectUserActionIsCreatedAccordingTouch() throws CloneNotSupportedException, SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		RuntimeParams runtimeParams = gameWorldService.getRuntimeParams();

		WorldPosition toTheUp = (WorldPosition) snake.headPosition.clone();
		toTheUp.tileY--;

		WorldPosition toTheUpLeft = (WorldPosition) toTheUp.clone();
		toTheUpLeft.tileX--;

		WorldPosition toTheUpRight = (WorldPosition) toTheUpLeft.clone();
		toTheUpRight.tileY--;

		WorldPosition toTheRightRight = (WorldPosition) toTheUpRight.clone();
		toTheRightRight.tileX++;
		toTheRightRight.tileX++;

		WorldPosition toTheDown = (WorldPosition) toTheRightRight.clone();
		toTheDown.tileY++;

		WorldPosition toTheDownRight = (WorldPosition) toTheDown.clone();
		toTheDownRight.tileX++;

		WorldPosition toTheDownLeft = (WorldPosition) toTheDownRight.clone();
		toTheDownLeft.tileY++;

		WorldPosition toTheLeft = (WorldPosition) toTheDownLeft.clone();
		toTheLeft.tileX--;

		IDeviceService deviceService = _testInjectorInstance.getInstance(IDeviceService.class);

		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheUp, runtimeParams.layoutParams)));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheUpLeft, runtimeParams.layoutParams)));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheUpRight, runtimeParams.layoutParams)));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheRightRight, runtimeParams.layoutParams)));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheDown, runtimeParams.layoutParams)));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheDownRight, runtimeParams.layoutParams)));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheDownLeft, runtimeParams.layoutParams)));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.DeviceCoordsToTouchCoords(deviceService.WorldPositionToDeviceCoords(toTheLeft, runtimeParams.layoutParams)));
		replay(_mockedInputService);

		IUserActionService userActionService = _testInjectorInstance.getInstance(IUserActionService.class);
		SnakeMovementChange movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.UP);
		gameWorldService.applySnakeMovementChange(movementChange);
		gameWorldService.moveSnake();

		movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.LEFT);
		gameWorldService.applySnakeMovementChange(movementChange);
		gameWorldService.moveSnake();

		movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.UP);
		gameWorldService.applySnakeMovementChange(movementChange);
		gameWorldService.moveSnake();

		movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.RIGHT);
		gameWorldService.applySnakeMovementChange(movementChange);
		gameWorldService.moveSnake();
		gameWorldService.moveSnake();

		movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.DOWN);
		gameWorldService.applySnakeMovementChange(movementChange);
		gameWorldService.moveSnake();

		movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.RIGHT);
		gameWorldService.applySnakeMovementChange(movementChange);
		gameWorldService.moveSnake();

		movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.DOWN);
		gameWorldService.applySnakeMovementChange(movementChange);
		gameWorldService.moveSnake();

		movementChange = userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords(), runtimeParams.layoutParams);
		assertTrue(movementChange.newDirection == Direction.LEFT);
		gameWorldService.moveSnake();

		verify(_mockedInputService);
	}
}
