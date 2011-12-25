package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestSnakeIsolated
{
	private Injector _testInjectorInstance;
	private ISnakeService _mockedSnakeService;

	private class TestSnakeBindingsConfiguration extends AbstractModule
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
			bind(ILyingItemService.class).to(LyingItemService.class);
			bind(IGameWorldService.class).to(GameWorldService.class).in(Singleton.class);

			bind(ISnakeService.class).toInstance(_mockedSnakeService);
		}
	}

	public TestSnakeIsolated()
	{
		_mockedSnakeService = createNiceMock(ISnakeService.class);
		_testInjectorInstance = Guice.createInjector(new TestSnakeBindingsConfiguration());
	}

	@Test
	public void testSnakeIsCreatedProperly()
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		AppSettings appSettings = appSettingsService.getAppSettings();

		Snake snake = snakeService.createSnake();
		assertTrue(snake != null);
		assertEquals(snake.headPosition, new WorldPosition(appSettings.initialHeadPositionX, appSettings.initialHeadPositionY));
	}

	@Test
	public void testSnakeIsGrowingWhenAsked()
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();

		int length = snake.newLength;
		snakeService.growSnake(snake);
		assertTrue(length < snake.newLength);
	}

	@Test
	public void testSnakesLengthChangesAfterGrowingWhenMoving() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();

		int length = snake.currLength;
		snakeService.growSnake(snake);
		snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
		assertTrue(length < snake.currLength);
	}

	@Test
	public void testSnakeIsMoving() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();

		WorldPosition oldPos = null;
		try
		{
			oldPos = (WorldPosition) snake.headPosition.clone();
		}
		catch (CloneNotSupportedException e)
		{
		}
		snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());

		assertTrue(!snake.headPosition.equals(oldPos));
	}

	@Test
	public void testIfGameWorldSnakeMoveCallsSnakeMoveUnderTheHood() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException, UnknownLyingItemTypeException
	{
		Snake snake = new Snake();
		snake.headPosition = new WorldPosition(0, 0);
		expect(_mockedSnakeService.createSnake()).andReturn(snake);	
		replay(_mockedSnakeService);
		
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		gameWorldService.initGameWorld();
		GameWorld gameWorld = gameWorldService.getGameWorld();

		reset(_mockedSnakeService);
		_mockedSnakeService.moveSnake(gameWorld.snake, gameWorld.movementChangesInEffect);
		replay(_mockedSnakeService);

		gameWorldService.moveSnake();
		verify(_mockedSnakeService);
	}

	@Test(expected = SnakeMovementResultedEndOfGameException.class)
	public void testIfEndOfGameComesWhenMovingSnakeIntoWall() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();
		AppSettings appSettings = appSettingsService.getAppSettings();

		for (int i = 0; i < Math.max(appSettings.tilesHorizontally, appSettings.tilesVertically); i++)
		{
			snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
		}

		fail("Should not happen");
	}

	@Test
	public void testIfSnakesTrailCoordListContainsSameCountAsSnakeLength() throws CloneNotSupportedException
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();
		AppSettings appSettings = appSettingsService.getAppSettings();

		try
		{
			for (int i = 0; i < Math.max(appSettings.tilesHorizontally, appSettings.tilesVertically); i++)
			{
				snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
				assertTrue(snake.currLength == snakeService.getSnakesTrail(snake, new ArrayList<SnakeMovementChange>()).size());
			}
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
		}
	}

	@Test
	public void testIfAfterEndOfGameSnakeDoesNotMove() throws CloneNotSupportedException
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();
		AppSettings appSettings = appSettingsService.getAppSettings();

		try
		{
			for (int i = 0; i < Math.max(appSettings.tilesHorizontally, appSettings.tilesVertically); i++)
			{
				snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
			}
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
			WorldPosition headPosition = (WorldPosition) snake.headPosition.clone();

			try
			{
				snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
			}
			catch (SnakeMovementResultedEndOfGameException ex)
			{
				assertTrue(headPosition.equals(snake.headPosition));
				return;
			}
		}

		fail("Should not happen");
	}
	
	@Test
	public void testIfAfterEndOfGameSnakeDoesNotResize() throws CloneNotSupportedException
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();
		AppSettings appSettings = appSettingsService.getAppSettings();

		try
		{
			for (int i = 0; i < Math.max(appSettings.tilesHorizontally, appSettings.tilesVertically); i++)
			{
				snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
			}
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
			int length = snake.currLength;

			try
			{
				snakeService.growSnake(snake);
				snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
			}
			catch (SnakeMovementResultedEndOfGameException ex)
			{
				assertTrue(length == snake.currLength);
				return;
			}
		}

		fail("Should not happen");
	}

	@Test
	public void testIfSnakeStopsAtTheTop() throws CloneNotSupportedException
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ISnakeService snakeService = new SnakeService(appSettingsService, presentationService);

		Snake snake = snakeService.createSnake();
		AppSettings appSettings = appSettingsService.getAppSettings();

		try
		{
			snake.direction = Direction.UP;
			for (int i = 0; i < Math.max(appSettings.tilesHorizontally, appSettings.tilesVertically); i++)
			{
				snakeService.moveSnake(snake, new ArrayList<SnakeMovementChange>());
			}
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
			assertTrue(snake.headPosition.tileY == 0);
			return;
		}

		fail("Should not happen");
	}
}
