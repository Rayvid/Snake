package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.font.FontService;
import inc.bezdelniki.snakegame.font.IFontService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.LayoutParams;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestDeviceIsolated
{
	private Injector _testInjectorInstance;
	private IDeviceService _mockedDeviceService;

	private class TestDeviceBindingsConfiguration extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IRuntimeParamsService.class).to(RuntimeParamsService.class);
			bind(IFontService.class).to(FontService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(IDeviceService.class).toInstance(_mockedDeviceService);
		}
	}

	public TestDeviceIsolated()
	{
		_mockedDeviceService = createNiceMock(IDeviceService.class);
		_testInjectorInstance = Guice.createInjector(new TestDeviceBindingsConfiguration());
	}

	@Test
	public void testIfWorldPositionToDevicePositionReturnsMeaningfullResults()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(systemParamsService, appSettingsService, deviceService);

		Snake snake = snakeService.createSnake();
		WorldPosition toTheRight = (WorldPosition) snake.headPosition.clone();
		toTheRight.tileX++;
		WorldPosition toTheLeft = (WorldPosition) snake.headPosition.clone();
		toTheLeft.tileX--;
		WorldPosition up = (WorldPosition) snake.headPosition.clone();
		up.tileY--;
		WorldPosition down = (WorldPosition) snake.headPosition.clone();
		down.tileY++;

		systemParamsService.newResolutionWereSet(480, 315);
		SystemParams systemParameters = systemParamsService.getSystemParams();
		RuntimeParams runtimeParams = runtimeParamsService.createParamsForNewGame();

		DeviceCoords headCoords = deviceService.WorldPositionToDeviceCoords(snake.headPosition, runtimeParams.layoutParams);
		DeviceCoords toTheRightCoords = deviceService.WorldPositionToDeviceCoords(toTheRight, runtimeParams.layoutParams);
		DeviceCoords toTheLeftCoords = deviceService.WorldPositionToDeviceCoords(toTheLeft, runtimeParams.layoutParams);
		DeviceCoords upCoords = deviceService.WorldPositionToDeviceCoords(up, runtimeParams.layoutParams);
		DeviceCoords downCoords = deviceService.WorldPositionToDeviceCoords(down, runtimeParams.layoutParams);

		int tileSize = deviceService.getTileSize(runtimeParams.layoutParams);

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
				&& (Math.abs(toTheRightCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - toTheLeftCoords.x) == tileSize || Math
						.abs(toTheRightCoords.y - headCoords.y) == tileSize && Math.abs(headCoords.y - toTheLeftCoords.y) == tileSize));
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
				&& (Math.abs(upCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - downCoords.x) == tileSize || Math.abs(upCoords.y - headCoords.y) == tileSize
						&& Math.abs(headCoords.y - downCoords.y) == tileSize));

		systemParamsService.newResolutionWereSet(315, 480);
		systemParameters = systemParamsService.getSystemParams();

		runtimeParams = runtimeParamsService.createParamsForNewGame();

		headCoords = deviceService.WorldPositionToDeviceCoords(snake.headPosition, runtimeParams.layoutParams);
		toTheRightCoords = deviceService.WorldPositionToDeviceCoords(toTheRight, runtimeParams.layoutParams);
		toTheLeftCoords = deviceService.WorldPositionToDeviceCoords(toTheLeft, runtimeParams.layoutParams);
		upCoords = deviceService.WorldPositionToDeviceCoords(up, runtimeParams.layoutParams);
		downCoords = deviceService.WorldPositionToDeviceCoords(down, runtimeParams.layoutParams);

		tileSize = deviceService.getTileSize(runtimeParams.layoutParams);

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
				&& (Math.abs(toTheRightCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - toTheLeftCoords.x) == tileSize || Math
						.abs(toTheRightCoords.y - headCoords.y) == tileSize && Math.abs(headCoords.y - toTheLeftCoords.y) == tileSize));
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
				&& (Math.abs(upCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - downCoords.x) == tileSize || Math.abs(upCoords.y - headCoords.y) == tileSize
						&& Math.abs(headCoords.y - downCoords.y) == tileSize));
	}

	@Test
	public void testIfWorldPositionToDevicePositionAndBackReturnsSameResults()
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService);
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(systemParamsService, appSettingsService, deviceService);

		Snake snake = snakeService.createSnake();
		WorldPosition toTheRight = (WorldPosition) snake.headPosition.clone();
		toTheRight.tileX++;
		WorldPosition toTheLeft = (WorldPosition) snake.headPosition.clone();
		toTheLeft.tileX--;
		WorldPosition up = (WorldPosition) snake.headPosition.clone();
		up.tileY--;
		WorldPosition down = (WorldPosition) snake.headPosition.clone();
		down.tileY++;

		systemParamsService.newResolutionWereSet(480, 315);
		RuntimeParams runtimeParams = runtimeParamsService.createParamsForNewGame();

		assertTrue(toTheRight.equals(deviceService.DeviceCoordsToWorldPosition(
				deviceService.WorldPositionToDeviceCoords(toTheRight, runtimeParams.layoutParams), runtimeParams.layoutParams)));
		assertTrue(toTheLeft.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(toTheLeft, runtimeParams.layoutParams),
				runtimeParams.layoutParams)));
		assertTrue(up.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(up, runtimeParams.layoutParams),
				runtimeParams.layoutParams)));
		assertTrue(down.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(down, runtimeParams.layoutParams),
				runtimeParams.layoutParams)));

		systemParamsService.newResolutionWereSet(315, 480);
		runtimeParams = runtimeParamsService.createParamsForNewGame();

		assertTrue(toTheRight.equals(deviceService.DeviceCoordsToWorldPosition(
				deviceService.WorldPositionToDeviceCoords(toTheRight, runtimeParams.layoutParams), runtimeParams.layoutParams)));
		assertTrue(toTheLeft.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(toTheLeft, runtimeParams.layoutParams),
				runtimeParams.layoutParams)));
		assertTrue(up.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(up, runtimeParams.layoutParams),
				runtimeParams.layoutParams)));
		assertTrue(down.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(down, runtimeParams.layoutParams),
				runtimeParams.layoutParams)));
	}

	@Test
	public void testIfBiggerDimensionHasBeenChoosenAsHorizontal()
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		ISystemParamsService systemParametersService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IDeviceService deviceService = new DeviceService(systemParametersService, appSettingsService);

		systemParametersService.newResolutionWereSet(480, 315);
		DeviceDeltas deltas = deviceService.getDeltas();
		assertTrue(deltas.deltaDeviceYForWorldX == 0 && deltas.deltaDeviceXForWorldX != 0 && deltas.deltaDeviceXForWorldY == 0
				&& deltas.deltaDeviceYForWorldY != 0);

		systemParametersService.newResolutionWereSet(315, 480);
		deltas = deviceService.getDeltas();
		assertTrue(deltas.deltaDeviceYForWorldX != 0 && deltas.deltaDeviceXForWorldX == 0 && deltas.deltaDeviceXForWorldY != 0
				&& deltas.deltaDeviceYForWorldY == 0);
	}

	@Test
	public void testIfTileSizeIsOptimal()
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		ISystemParamsService systemParametersService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IDeviceService deviceService = new DeviceService(systemParametersService, appSettingsService);

		LayoutParams zeroPaddingLayout = new LayoutParams();
		int tileSize = deviceService.getTileSize(zeroPaddingLayout);
		SystemParams systemParameters = systemParametersService.getSystemParams();
		AppSettings appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize * appSettings.tilesHorizontally < tileSize
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0
				&& systemParameters.width - tileSize * appSettings.tilesVertically >= 0
				|| systemParameters.width - tileSize * appSettings.tilesHorizontally < tileSize
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0
				&& systemParameters.height - tileSize * appSettings.tilesVertically >= 0
				|| systemParameters.height - tileSize * appSettings.tilesVertically < tileSize
				&& systemParameters.height - tileSize * appSettings.tilesVertically >= 0
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0
				|| systemParameters.width - tileSize * appSettings.tilesVertically < tileSize
				&& systemParameters.width - tileSize * appSettings.tilesVertically >= 0
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0);

		systemParametersService.newResolutionWereSet(200, 100);
		tileSize = deviceService.getTileSize(zeroPaddingLayout);
		systemParameters = systemParametersService.getSystemParams();
		appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize * appSettings.tilesHorizontally < tileSize
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0
				&& systemParameters.width - tileSize * appSettings.tilesVertically >= 0
				|| systemParameters.width - tileSize * appSettings.tilesHorizontally < tileSize
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0
				&& systemParameters.height - tileSize * appSettings.tilesVertically >= 0
				|| systemParameters.height - tileSize * appSettings.tilesVertically < tileSize
				&& systemParameters.height - tileSize * appSettings.tilesVertically >= 0
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0
				|| systemParameters.width - tileSize * appSettings.tilesVertically < tileSize
				&& systemParameters.width - tileSize * appSettings.tilesVertically >= 0
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0);

		systemParametersService.newResolutionWereSet(1000, 2000);
		tileSize = deviceService.getTileSize(zeroPaddingLayout);
		systemParameters = systemParametersService.getSystemParams();
		appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize * appSettings.tilesHorizontally < tileSize
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0
				&& systemParameters.width - tileSize * appSettings.tilesVertically >= 0
				|| systemParameters.width - tileSize * appSettings.tilesHorizontally < tileSize
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0
				&& systemParameters.height - tileSize * appSettings.tilesVertically >= 0
				|| systemParameters.height - tileSize * appSettings.tilesVertically < tileSize
				&& systemParameters.height - tileSize * appSettings.tilesVertically >= 0
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0
				|| systemParameters.width - tileSize * appSettings.tilesVertically < tileSize
				&& systemParameters.width - tileSize * appSettings.tilesVertically >= 0
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0);
	}

	@Test
	public void testIfWeGetSameResolutionConvertingTouchCoordsToDeviceCoords()
	{
		ISystemParamsService systemParametersService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = new DeviceService(systemParametersService, appSettingsService);

		systemParametersService.newResolutionWereSet(480, 315);

		SystemParams systemParameters = systemParametersService.getSystemParams();
		TouchCoords upperLeftT = new TouchCoords(0, 0);
		TouchCoords upperRightT = new TouchCoords(systemParameters.width, 0);
		TouchCoords bottomLeftT = new TouchCoords(0, systemParameters.height);
		TouchCoords bottomRightT = new TouchCoords(systemParameters.width, systemParameters.height);

		DeviceCoords upperLeftD = deviceService.TouchCoordsToDeviceCoords(upperLeftT);
		DeviceCoords upperRightD = deviceService.TouchCoordsToDeviceCoords(upperRightT);
		DeviceCoords bottomLeftD = deviceService.TouchCoordsToDeviceCoords(bottomLeftT);
		DeviceCoords bottomRightD = deviceService.TouchCoordsToDeviceCoords(bottomRightT);

		assertTrue(upperLeftT.x + upperRightT.x + bottomLeftT.x + bottomRightT.x == upperLeftD.x + upperRightD.x + bottomLeftD.x + bottomRightD.x);
		assertTrue(upperLeftT.y + upperRightT.y + bottomLeftT.y + bottomRightT.y == upperLeftD.y + upperRightD.y + bottomLeftD.y + bottomRightD.y);

		systemParametersService.newResolutionWereSet(315, 480);

		systemParameters = systemParametersService.getSystemParams();
		upperLeftT = new TouchCoords(0, 0);
		upperRightT = new TouchCoords(systemParameters.width, 0);
		bottomLeftT = new TouchCoords(0, systemParameters.height);
		bottomRightT = new TouchCoords(systemParameters.width, systemParameters.height);

		upperLeftD = deviceService.TouchCoordsToDeviceCoords(upperLeftT);
		upperRightD = deviceService.TouchCoordsToDeviceCoords(upperRightT);
		bottomLeftD = deviceService.TouchCoordsToDeviceCoords(bottomLeftT);
		bottomRightD = deviceService.TouchCoordsToDeviceCoords(bottomRightT);

		assertTrue(upperLeftT.x + upperRightT.x + bottomLeftT.x + bottomRightT.x == upperLeftD.x + upperRightD.x + bottomLeftD.x + bottomRightD.x);
		assertTrue(upperLeftT.y + upperRightT.y + bottomLeftT.y + bottomRightT.y == upperLeftD.y + upperRightD.y + bottomLeftD.y + bottomRightD.y);
	}

	@Test
	public void testIfWeGetSameResultConvertingDeviceCoordsToTouchCoordsAndBack()
	{
		ISystemParamsService systemParametersService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = new DeviceService(systemParametersService, appSettingsService);

		systemParametersService.newResolutionWereSet(480, 315);

		SystemParams systemParameters = systemParametersService.getSystemParams();
		DeviceCoords upperLeftD = new DeviceCoords(0, 0);
		DeviceCoords upperRightD = new DeviceCoords(systemParameters.width, 0);
		DeviceCoords bottomLeftD = new DeviceCoords(0, systemParameters.height);
		DeviceCoords bottomRightD = new DeviceCoords(systemParameters.width, systemParameters.height);

		TouchCoords upperLeftT = deviceService.DeviceCoordsToTouchCoords(upperLeftD);
		TouchCoords upperRightT = deviceService.DeviceCoordsToTouchCoords(upperRightD);
		TouchCoords bottomLeftT = deviceService.DeviceCoordsToTouchCoords(bottomLeftD);
		TouchCoords bottomRightT = deviceService.DeviceCoordsToTouchCoords(bottomRightD);

		DeviceCoords upperLeftDD = deviceService.TouchCoordsToDeviceCoords(upperLeftT);
		DeviceCoords upperRightDD = deviceService.TouchCoordsToDeviceCoords(upperRightT);
		DeviceCoords bottomLeftDD = deviceService.TouchCoordsToDeviceCoords(bottomLeftT);
		DeviceCoords bottomRightDD = deviceService.TouchCoordsToDeviceCoords(bottomRightT);

		assertTrue(upperLeftD.x == upperLeftDD.x && upperLeftD.y == upperLeftDD.y);
		assertTrue(upperRightD.x == upperRightDD.x && upperRightD.y == upperRightDD.y);
		assertTrue(bottomLeftD.x == bottomLeftDD.x && bottomLeftD.y == bottomLeftDD.y);
		assertTrue(bottomRightD.x == bottomRightDD.x && bottomRightD.y == bottomRightDD.y);

		systemParametersService.newResolutionWereSet(315, 480);

		systemParameters = systemParametersService.getSystemParams();
		upperLeftD = new DeviceCoords(0, 0);
		upperRightD = new DeviceCoords(systemParameters.width, 0);
		bottomLeftD = new DeviceCoords(0, systemParameters.height);
		bottomRightD = new DeviceCoords(systemParameters.width, systemParameters.height);

		upperLeftT = deviceService.DeviceCoordsToTouchCoords(upperLeftD);
		upperRightT = deviceService.DeviceCoordsToTouchCoords(upperRightD);
		bottomLeftT = deviceService.DeviceCoordsToTouchCoords(bottomLeftD);
		bottomRightT = deviceService.DeviceCoordsToTouchCoords(bottomRightD);

		upperLeftDD = deviceService.TouchCoordsToDeviceCoords(upperLeftT);
		upperRightDD = deviceService.TouchCoordsToDeviceCoords(upperRightT);
		bottomLeftDD = deviceService.TouchCoordsToDeviceCoords(bottomLeftT);
		bottomRightDD = deviceService.TouchCoordsToDeviceCoords(bottomRightT);

		assertTrue(upperLeftD.x == upperLeftDD.x && upperLeftD.y == upperLeftDD.y);
		assertTrue(upperRightD.x == upperRightDD.x && upperRightD.y == upperRightDD.y);
		assertTrue(bottomLeftD.x == bottomLeftDD.x && bottomLeftD.y == bottomLeftDD.y);
		assertTrue(bottomRightD.x == bottomRightDD.x && bottomRightD.y == bottomRightDD.y);
	}

	@Test
	public void testIfCoordsOutsideStillConvertsToCorrectWorldPosition()
	{
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(systemParamsService, appSettingsService, deviceService);

		systemParamsService.newResolutionWereSet(480, 315);

		DeviceCoords deviceCoordsOutsideNegative = new DeviceCoords(-100, -100);
		DeviceCoords deviceCoordsOutsidePositive = new DeviceCoords(systemParamsService.getSystemParams().width + 100,
				systemParamsService.getSystemParams().height + 100);

		RuntimeParams runtimeParams = runtimeParamsService.createParamsForNewGame();

		WorldPosition negative = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsideNegative, runtimeParams.layoutParams);
		WorldPosition positive = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsidePositive, runtimeParams.layoutParams);
		DeviceCoords negativeD = deviceService.WorldPositionToDeviceCoords(negative, runtimeParams.layoutParams);
		DeviceCoords positiveD = deviceService.WorldPositionToDeviceCoords(positive, runtimeParams.layoutParams);
		assertTrue(negativeD.x <= deviceService.getTileSize(runtimeParams.layoutParams)
				&& negativeD.y <= deviceService.getTileSize(runtimeParams.layoutParams));
		assertTrue(positiveD.x >= 480 - deviceService.getTileSize(runtimeParams.layoutParams)
				&& positiveD.y >= 315 - deviceService.getTileSize(runtimeParams.layoutParams));

		systemParamsService.newResolutionWereSet(315, 480);

		deviceCoordsOutsideNegative = new DeviceCoords(-100, -100);
		deviceCoordsOutsidePositive = new DeviceCoords(systemParamsService.getSystemParams().width + 100,
				systemParamsService.getSystemParams().height + 100);

		runtimeParams = runtimeParamsService.createParamsForNewGame();

		negative = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsideNegative, runtimeParams.layoutParams);
		positive = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsidePositive, runtimeParams.layoutParams);
		negativeD = deviceService.WorldPositionToDeviceCoords(negative, runtimeParams.layoutParams);
		positiveD = deviceService.WorldPositionToDeviceCoords(positive, runtimeParams.layoutParams);
		assertTrue(negativeD.x <= deviceService.getTileSize(runtimeParams.layoutParams)
				&& negativeD.y <= deviceService.getTileSize(runtimeParams.layoutParams));
		assertTrue(positiveD.x >= 315 - deviceService.getTileSize(runtimeParams.layoutParams)
				&& positiveD.y >= 480 - deviceService.getTileSize(runtimeParams.layoutParams));
	}

	@Test
	public void testIfLayoutPaddingsPlusContentEqualsResolution()
	{
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(systemParamsService, appSettingsService, deviceService);

		AppSettings appSettings = appSettingsService.getAppSettings();

		systemParamsService.newResolutionWereSet(480, 315);

		SystemParams systemParams = systemParamsService.getSystemParams();
		RuntimeParams runtimeParams = runtimeParamsService.createParamsForNewGame();
		int tileSize = deviceService.getTileSize(runtimeParams.layoutParams);

		DeviceCoords topLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, 0), runtimeParams.layoutParams);
		DeviceCoords topRight = deviceService.WorldPositionToDeviceCoords(new WorldPosition(appSettings.tilesHorizontally - 1, 0), runtimeParams.layoutParams);
		DeviceCoords bottomLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, appSettings.tilesVertically - 1), runtimeParams.layoutParams);
		DeviceCoords bottomRight = deviceService.WorldPositionToDeviceCoords(
				new WorldPosition(appSettings.tilesHorizontally - 1, appSettings.tilesVertically - 1),
				runtimeParams.layoutParams);

		assertTrue(topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingLeft * 2
				+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingRight) * 2
				|| topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingRight * 2
						+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingLeft) * 2
				|| topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingTop * 2
						+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingBottom) * 2
				|| topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingBottom * 2
						+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingTop) * 2);

		assertTrue(topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingLeft * 2
				+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingRight) * 2
				|| topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingRight * 2
						+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingLeft) * 2
				|| topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingTop * 2
						+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingBottom) * 2
				|| topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingBottom * 2
						+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingTop) * 2);

		systemParamsService.newResolutionWereSet(315, 480);

		systemParams = systemParamsService.getSystemParams();
		runtimeParams = runtimeParamsService.createParamsForNewGame();
		tileSize = deviceService.getTileSize(runtimeParams.layoutParams);

		topLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, 0), runtimeParams.layoutParams);
		topRight = deviceService.WorldPositionToDeviceCoords(new WorldPosition(appSettings.tilesHorizontally - 1, 0), runtimeParams.layoutParams);
		bottomLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, appSettings.tilesVertically - 1), runtimeParams.layoutParams);
		bottomRight = deviceService.WorldPositionToDeviceCoords(
				new WorldPosition(appSettings.tilesHorizontally - 1, appSettings.tilesVertically - 1),
				runtimeParams.layoutParams);

		assertTrue(topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingLeft * 2
				+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingRight) * 2
				|| topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingRight * 2
						+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingLeft) * 2
				|| topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingTop * 2
						+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingBottom) * 2
				|| topLeft.x + topRight.x + bottomLeft.x + bottomRight.x == runtimeParams.layoutParams.gameBoxPaddingBottom * 2
						+ (systemParams.width - tileSize - runtimeParams.layoutParams.gameBoxPaddingTop) * 2);

		assertTrue(topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingLeft * 2
				+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingRight) * 2
				|| topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingRight * 2
						+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingLeft) * 2
				|| topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingTop * 2
						+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingBottom) * 2
				|| topLeft.y + topRight.y + bottomLeft.y + bottomRight.y == runtimeParams.layoutParams.gameBoxPaddingBottom * 2
						+ (systemParams.height - tileSize - runtimeParams.layoutParams.gameBoxPaddingTop) * 2);
	}
}
