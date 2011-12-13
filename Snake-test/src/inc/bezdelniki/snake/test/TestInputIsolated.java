package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.SystemParametersService;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.UserActionService;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestInputIsolated {
	private Injector _testInjectorInstance;
	private IInputService _mockedInputService;
	
	private class TestInputBindingsConfiguration extends AbstractModule {
		@Override
		protected void configure()
		{
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(ISystemParametersService.class).to(SystemParametersService.class).in(Singleton.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(ISnakeService.class).to(SnakeService.class);
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
	public void testIfCorrectUserActionIsCreatedAccordingTouch() throws CloneNotSupportedException
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		Snake snake = snakeService.createSnake();
		
		WorldPosition toTheLeft = (WorldPosition) snake.headPosition.clone();
		toTheLeft.tileX--;
		
		WorldPosition toTheUpUpLeft = (WorldPosition) snake.headPosition.clone();
		toTheUpUpLeft.tileX--;
		toTheUpUpLeft.tileY--;
		toTheUpUpLeft.tileY--;
		
		WorldPosition toTheUp = (WorldPosition) snake.headPosition.clone();
		toTheUp.tileY--;
		
		WorldPosition toTheUpRightRight = (WorldPosition) snake.headPosition.clone();
		toTheUpRightRight.tileY--;
		toTheUpRightRight.tileX++;
		toTheUpRightRight.tileX++;

		WorldPosition toTheRight = (WorldPosition) snake.headPosition.clone();
		toTheRight.tileX++;

		WorldPosition toTheDownDownRight = (WorldPosition) snake.headPosition.clone();
		toTheDownDownRight.tileY++;
		toTheDownDownRight.tileY++;
		toTheDownDownRight.tileX++;
		
		WorldPosition toTheDown = (WorldPosition) snake.headPosition.clone();
		toTheDown.tileY++;
		
		WorldPosition toTheDownLeftLeft = (WorldPosition) snake.headPosition.clone();
		toTheDownLeftLeft.tileX--;
		toTheDownLeftLeft.tileX--;
		toTheDownLeftLeft.tileY++;
		
		IDeviceService deviceService = SnakeInjector.getInjectorInstance().getInstance(IDeviceService.class);
	
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheLeft));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheUpUpLeft));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheUp));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheUpRightRight));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheRight));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheDownDownRight));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheDown));
		expect(_mockedInputService.GetTouchCoords()).andReturn(deviceService.WorldPositionToDeviceCoords(toTheDownLeftLeft));
		replay(_mockedInputService);
		
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.LEFT);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.UP);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.UP);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.RIGHT);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.RIGHT);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.DOWN);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.DOWN);
		assertTrue(userActionService.createSnakeMovementChangeAccordingTouch(snake, _mockedInputService.GetTouchCoords()).newDirection == Direction.LEFT);
	}
}
