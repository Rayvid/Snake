package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.SystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestDeviceServiceIsolated {
	private Injector _testInjectorInstance;
	private IDeviceService _mockedDeviceService;

	private class TestDeviceBindingsConfiguration extends AbstractModule {
		@Override
		protected void configure() {
			bind(ISystemParametersService.class).to(SystemParametersService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(IDeviceService.class).toInstance(_mockedDeviceService);
		}
	}

	public TestDeviceServiceIsolated() {
		_mockedDeviceService = createNiceMock(IDeviceService.class);
		_testInjectorInstance = Guice
				.createInjector(new TestDeviceBindingsConfiguration());
	}

	@Test
	public void testIfWorldPositionTransformationReturnsMeaningfullResults()
			throws CloneNotSupportedException {
		ISnakeService snakeService = _testInjectorInstance
				.getInstance(ISnakeService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance
				.getInstance(IAppSettingsService.class);
		ISystemParametersService systemParametersService = _testInjectorInstance
				.getInstance(ISystemParametersService.class);
		IDeviceService deviceService = new DeviceService(
				systemParametersService, appSettingsService);

		Snake snake = snakeService.createSnake();
		WorldPosition toTheRight = (WorldPosition) snake.headPosition.clone();
		toTheRight.tileX++;
		WorldPosition toTheLeft = (WorldPosition) snake.headPosition.clone();
		toTheLeft.tileX--;
		WorldPosition up = (WorldPosition) snake.headPosition.clone();
		up.tileY--;
		WorldPosition down = (WorldPosition) snake.headPosition.clone();
		down.tileY++;

		systemParametersService.newResolutionWereSet(400, 200);
		SystemParameters systemParameters = systemParametersService
				.getSystemParameters();

		DeviceCoords headCoords = deviceService
				.WorldCoordsToDeviceCoords(snake.headPosition);
		DeviceCoords toTheRightCoords = deviceService
				.WorldCoordsToDeviceCoords(toTheRight);
		DeviceCoords toTheLeftCoords = deviceService
				.WorldCoordsToDeviceCoords(toTheLeft);
		DeviceCoords upCoords = deviceService.WorldCoordsToDeviceCoords(up);
		DeviceCoords downCoords = deviceService.WorldCoordsToDeviceCoords(down);

		int tileSize = deviceService.getTileSize();

		assertTrue(toTheRightCoords.x > -systemParameters.width
				&& toTheRightCoords.x < systemParameters.width
				&& headCoords.x > -systemParameters.width
				&& headCoords.x < systemParameters.width
				&& toTheLeftCoords.x > -systemParameters.width
				&& toTheLeftCoords.x < systemParameters.width
				&& toTheRightCoords.y > -systemParameters.height
				&& toTheRightCoords.y < systemParameters.height
				&& headCoords.y > -systemParameters.height
				&& headCoords.y < systemParameters.height
				&& toTheLeftCoords.y > -systemParameters.height
				&& toTheLeftCoords.y < systemParameters.height
				&& (Math.abs(toTheRightCoords.x - headCoords.x) == tileSize
						&& Math.abs(headCoords.x - toTheLeftCoords.x) == tileSize || Math
						.abs(toTheRightCoords.y - headCoords.y) == tileSize
						&& Math.abs(headCoords.y - toTheLeftCoords.y) == tileSize));
		assertTrue(upCoords.x > -systemParameters.width
				&& upCoords.x < systemParameters.width
				&& headCoords.x > -systemParameters.width
				&& headCoords.x < systemParameters.width
				&& downCoords.x > -systemParameters.width
				&& downCoords.x < systemParameters.width
				&& upCoords.y > -systemParameters.height
				&& upCoords.y < systemParameters.height
				&& headCoords.y > -systemParameters.height
				&& headCoords.y < systemParameters.height
				&& downCoords.y > -systemParameters.height
				&& downCoords.y < systemParameters.height
				&& (Math.abs(upCoords.x - headCoords.x) == tileSize
						&& Math.abs(headCoords.x - downCoords.x) == tileSize || Math
						.abs(upCoords.y - headCoords.y) == tileSize
						&& Math.abs(headCoords.y - downCoords.y) == tileSize));

		systemParametersService.newResolutionWereSet(200, 400);
		systemParameters = systemParametersService.getSystemParameters();

		headCoords = deviceService
				.WorldCoordsToDeviceCoords(snake.headPosition);
		toTheRightCoords = deviceService.WorldCoordsToDeviceCoords(toTheRight);
		toTheLeftCoords = deviceService.WorldCoordsToDeviceCoords(toTheLeft);
		upCoords = deviceService.WorldCoordsToDeviceCoords(up);
		downCoords = deviceService.WorldCoordsToDeviceCoords(down);

		tileSize = deviceService.getTileSize();

		assertTrue(toTheRightCoords.x > -systemParameters.width
				&& toTheRightCoords.x < systemParameters.width
				&& headCoords.x > -systemParameters.width
				&& headCoords.x < systemParameters.width
				&& toTheLeftCoords.x > -systemParameters.width
				&& toTheLeftCoords.x < systemParameters.width
				&& toTheRightCoords.y > -systemParameters.height
				&& toTheRightCoords.y < systemParameters.height
				&& headCoords.y > -systemParameters.height
				&& headCoords.y < systemParameters.height
				&& toTheLeftCoords.y > -systemParameters.height
				&& toTheLeftCoords.y < systemParameters.height
				&& (Math.abs(toTheRightCoords.x - headCoords.x) == tileSize
						&& Math.abs(headCoords.x - toTheLeftCoords.x) == tileSize || Math
						.abs(toTheRightCoords.y - headCoords.y) == tileSize
						&& Math.abs(headCoords.y - toTheLeftCoords.y) == tileSize));
		assertTrue(upCoords.x > -systemParameters.width
				&& upCoords.x < systemParameters.width
				&& headCoords.x > -systemParameters.width
				&& headCoords.x < systemParameters.width
				&& downCoords.x > -systemParameters.width
				&& downCoords.x < systemParameters.width
				&& upCoords.y > -systemParameters.height
				&& upCoords.y < systemParameters.height
				&& headCoords.y > -systemParameters.height
				&& headCoords.y < systemParameters.height
				&& downCoords.y > -systemParameters.height
				&& downCoords.y < systemParameters.height
				&& (Math.abs(upCoords.x - headCoords.x) == tileSize
						&& Math.abs(headCoords.x - downCoords.x) == tileSize || Math
						.abs(upCoords.y - headCoords.y) == tileSize
						&& Math.abs(headCoords.y - downCoords.y) == tileSize));
	}

	@Test
	public void testIfBiggerDimensionHasBeenChoosenAsHorizontal() {
		IAppSettingsService appSettingsService = _testInjectorInstance
				.getInstance(IAppSettingsService.class);
		ISystemParametersService systemParametersService = _testInjectorInstance
				.getInstance(ISystemParametersService.class);
		IDeviceService deviceService = new DeviceService(
				systemParametersService, appSettingsService);

		systemParametersService.newResolutionWereSet(480, 320);
		DeviceDeltas deltas = deviceService.getDeltas();
		assertTrue(deltas.deltaYForWorldX == 0 && deltas.deltaXForWorldX != 0
				&& deltas.deltaXForWorldY == 0 && deltas.deltaYForWorldY != 0);

		systemParametersService.newResolutionWereSet(320, 480);
		deltas = deviceService.getDeltas();
		assertTrue(deltas.deltaYForWorldX != 0 && deltas.deltaXForWorldX == 0
				&& deltas.deltaXForWorldY != 0 && deltas.deltaYForWorldY == 0);
	}

	@Test
	public void testIfTileSizeIsOptimal() {
		IAppSettingsService appSettingsService = _testInjectorInstance
				.getInstance(IAppSettingsService.class);
		ISystemParametersService systemParametersService = _testInjectorInstance
				.getInstance(ISystemParametersService.class);
		IDeviceService deviceService = new DeviceService(
				systemParametersService, appSettingsService);

		int tileSize = deviceService.getTileSize();
		SystemParameters systemParameters = systemParametersService
				.getSystemParameters();
		AppSettings appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize
				* appSettings.tilesHorizontally < tileSize
				&& systemParameters.height - tileSize
						* appSettings.tilesHorizontally >= 0
				&& systemParameters.width - tileSize
						* appSettings.tilesVertically >= 0
				|| systemParameters.width - tileSize
						* appSettings.tilesHorizontally < tileSize
				&& systemParameters.width - tileSize
						* appSettings.tilesHorizontally >= 0
				&& systemParameters.height - tileSize
						* appSettings.tilesVertically >= 0
				|| systemParameters.height - tileSize
						* appSettings.tilesVertically < tileSize
				&& systemParameters.height - tileSize
						* appSettings.tilesVertically >= 0
				&& systemParameters.width - tileSize
						* appSettings.tilesHorizontally >= 0
				|| systemParameters.width - tileSize
						* appSettings.tilesVertically < tileSize
				&& systemParameters.width - tileSize
						* appSettings.tilesVertically >= 0
				&& systemParameters.height - tileSize
						* appSettings.tilesHorizontally >= 0);

		systemParametersService.newResolutionWereSet(200, 100);
		tileSize = deviceService.getTileSize();
		systemParameters = systemParametersService.getSystemParameters();
		appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize
				* appSettings.tilesHorizontally < tileSize
				&& systemParameters.height - tileSize
						* appSettings.tilesHorizontally >= 0
				&& systemParameters.width - tileSize
						* appSettings.tilesVertically >= 0
				|| systemParameters.width - tileSize
						* appSettings.tilesHorizontally < tileSize
				&& systemParameters.width - tileSize
						* appSettings.tilesHorizontally >= 0
				&& systemParameters.height - tileSize
						* appSettings.tilesVertically >= 0
				|| systemParameters.height - tileSize
						* appSettings.tilesVertically < tileSize
				&& systemParameters.height - tileSize
						* appSettings.tilesVertically >= 0
				&& systemParameters.width - tileSize
						* appSettings.tilesHorizontally >= 0
				|| systemParameters.width - tileSize
						* appSettings.tilesVertically < tileSize
				&& systemParameters.width - tileSize
						* appSettings.tilesVertically >= 0
				&& systemParameters.height - tileSize
						* appSettings.tilesHorizontally >= 0);

		systemParametersService.newResolutionWereSet(1000, 2000);
		tileSize = deviceService.getTileSize();
		systemParameters = systemParametersService.getSystemParameters();
		appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize
				* appSettings.tilesHorizontally < tileSize
				&& systemParameters.height - tileSize
						* appSettings.tilesHorizontally >= 0
				&& systemParameters.width - tileSize
						* appSettings.tilesVertically >= 0
				|| systemParameters.width - tileSize
						* appSettings.tilesHorizontally < tileSize
				&& systemParameters.width - tileSize
						* appSettings.tilesHorizontally >= 0
				&& systemParameters.height - tileSize
						* appSettings.tilesVertically >= 0
				|| systemParameters.height - tileSize
						* appSettings.tilesVertically < tileSize
				&& systemParameters.height - tileSize
						* appSettings.tilesVertically >= 0
				&& systemParameters.width - tileSize
						* appSettings.tilesHorizontally >= 0
				|| systemParameters.width - tileSize
						* appSettings.tilesVertically < tileSize
				&& systemParameters.width - tileSize
						* appSettings.tilesVertically >= 0
				&& systemParameters.height - tileSize
						* appSettings.tilesHorizontally >= 0);
	}
}
