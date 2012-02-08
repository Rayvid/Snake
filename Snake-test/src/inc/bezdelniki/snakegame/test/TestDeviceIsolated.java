package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestDeviceIsolated
{
	private Injector _testInjectorInstance;
	private IDeviceService _mockedDeviceService;

	public TestDeviceIsolated()
	{
		_mockedDeviceService = createNiceMock(IDeviceService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IDeviceService.class,
						_mockedDeviceService,
						IDeviceService.class));
	}

	@Test
	public void testIfWorldPositionToDevicePositionReturnsMeaningfullResults()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		Snake snake = snakeService.create();
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

		DeviceCoords headCoords = deviceService.WorldPositionToDeviceCoords(snake.headPosition);
		DeviceCoords toTheRightCoords = deviceService.WorldPositionToDeviceCoords(toTheRight);
		DeviceCoords toTheLeftCoords = deviceService.WorldPositionToDeviceCoords(toTheLeft);
		DeviceCoords upCoords = deviceService.WorldPositionToDeviceCoords(up);
		DeviceCoords downCoords = deviceService.WorldPositionToDeviceCoords(down);

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

		headCoords = deviceService.WorldPositionToDeviceCoords(snake.headPosition);
		toTheRightCoords = deviceService.WorldPositionToDeviceCoords(toTheRight);
		toTheLeftCoords = deviceService.WorldPositionToDeviceCoords(toTheLeft);
		upCoords = deviceService.WorldPositionToDeviceCoords(up);
		downCoords = deviceService.WorldPositionToDeviceCoords(down);

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

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		Snake snake = snakeService.create();
		WorldPosition toTheRight = (WorldPosition) snake.headPosition.clone();
		toTheRight.tileX++;
		WorldPosition toTheLeft = (WorldPosition) snake.headPosition.clone();
		toTheLeft.tileX--;
		WorldPosition up = (WorldPosition) snake.headPosition.clone();
		up.tileY--;
		WorldPosition down = (WorldPosition) snake.headPosition.clone();
		down.tileY++;

		systemParamsService.newResolutionWereSet(480, 315);

		assertTrue(toTheRight.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(toTheRight))));
		assertTrue(toTheLeft.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(toTheLeft))));
		assertTrue(up.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(up))));
		assertTrue(down.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(down))));

		systemParamsService.newResolutionWereSet(315, 480);

		assertTrue(toTheRight.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(toTheRight))));
		assertTrue(toTheLeft.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(toTheLeft))));
		assertTrue(up.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(up))));
		assertTrue(down.equals(deviceService.DeviceCoordsToWorldPosition(deviceService.WorldPositionToDeviceCoords(down))));
	}

	@Test
	public void testIfBiggerDimensionHasBeenChoosenAsHorizontal()
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		systemParamsService.newResolutionWereSet(480, 315);
		DeviceDeltas deltas = deviceService.getDeltas();
		assertTrue(deltas.deltaDeviceYForWorldX == 0 && deltas.deltaDeviceXForWorldX != 0 && deltas.deltaDeviceXForWorldY == 0
				&& deltas.deltaDeviceYForWorldY != 0);

		systemParamsService.newResolutionWereSet(315, 480);
		deltas = deviceService.getDeltas();
		assertTrue(deltas.deltaDeviceYForWorldX != 0 && deltas.deltaDeviceXForWorldX == 0 && deltas.deltaDeviceXForWorldY != 0
				&& deltas.deltaDeviceYForWorldY == 0);
	}

	@Test
	public void testIfTileSizeIsOptimal()
	{
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		systemParamsService.newResolutionWereSet(315, 540);
		int tileSize = deviceService.getTileSize();
		SystemParams systemParameters = systemParamsService.getSystemParams();
		AppSettings appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize * appSettings.tilesHorizontally
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally
				>= runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop
				&& systemParameters.width - tileSize * appSettings.tilesVertically
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				&& systemParameters.width - tileSize * appSettings.tilesVertically
				>= runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				|| systemParameters.width - tileSize * appSettings.tilesHorizontally
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally
				>= runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				&& systemParameters.height - tileSize * appSettings.tilesVertically
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop
				&& systemParameters.height - tileSize * appSettings.tilesVertically
				>= runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop);

		systemParamsService.newResolutionWereSet(540, 315);
		tileSize = deviceService.getTileSize();
		systemParameters = systemParamsService.getSystemParams();
		appSettings = appSettingsService.getAppSettings();

		assertTrue(systemParameters.height - tileSize * appSettings.tilesHorizontally
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop
				&& systemParameters.height - tileSize * appSettings.tilesHorizontally
				>= runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop
				&& systemParameters.width - tileSize * appSettings.tilesVertically
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				&& systemParameters.width - tileSize * appSettings.tilesVertically
				>= runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				|| systemParameters.width - tileSize * appSettings.tilesHorizontally
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				&& systemParameters.width - tileSize * appSettings.tilesHorizontally
				>= runtimeParams.layoutParams.gameBoxPaddingLeft + runtimeParams.layoutParams.gameBoxPaddingRight
				&& systemParameters.height - tileSize * appSettings.tilesVertically
				< tileSize + runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop
				&& systemParameters.height - tileSize * appSettings.tilesVertically
				>= runtimeParams.layoutParams.gameBoxPaddingBottom + runtimeParams.layoutParams.gameBoxPaddingTop);
	}

	@Test
	public void testIfWeGetSameResolutionConvertingTouchCoordsToDeviceCoords()
	{
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		systemParamsService.newResolutionWereSet(480, 315);

		SystemParams systemParameters = systemParamsService.getSystemParams();
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

		systemParamsService.newResolutionWereSet(315, 480);

		systemParameters = systemParamsService.getSystemParams();
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
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		systemParamsService.newResolutionWereSet(480, 315);

		SystemParams systemParameters = systemParamsService.getSystemParams();
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

		systemParamsService.newResolutionWereSet(315, 480);

		systemParameters = systemParamsService.getSystemParams();
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

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		systemParamsService.newResolutionWereSet(480, 315);

		DeviceCoords deviceCoordsOutsideNegative = new DeviceCoords(-100, -100);
		DeviceCoords deviceCoordsOutsidePositive = new DeviceCoords(systemParamsService.getSystemParams().width + 100,
				systemParamsService.getSystemParams().height + 100);

		WorldPosition negative = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsideNegative);
		WorldPosition positive = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsidePositive);
		DeviceCoords negativeD = deviceService.WorldPositionToDeviceCoords(negative);
		DeviceCoords positiveD = deviceService.WorldPositionToDeviceCoords(positive);
		assertTrue(negativeD.x <= deviceService.getTileSize()
				&& negativeD.y <= deviceService.getTileSize());
		assertTrue(positiveD.x >= 480 - deviceService.getTileSize()
				&& positiveD.y >= 315 - deviceService.getTileSize());

		systemParamsService.newResolutionWereSet(315, 480);

		deviceCoordsOutsideNegative = new DeviceCoords(-100, -100);
		deviceCoordsOutsidePositive = new DeviceCoords(systemParamsService.getSystemParams().width + 100,
				systemParamsService.getSystemParams().height + 100);

		negative = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsideNegative);
		positive = deviceService.DeviceCoordsToWorldPosition(deviceCoordsOutsidePositive);
		negativeD = deviceService.WorldPositionToDeviceCoords(negative);
		positiveD = deviceService.WorldPositionToDeviceCoords(positive);
		assertTrue(negativeD.x <= deviceService.getTileSize()
				&& negativeD.y <= deviceService.getTileSize());
		assertTrue(positiveD.x >= 315 - deviceService.getTileSize()
				&& positiveD.y >= 480 - deviceService.getTileSize());
	}

	@Test
	public void testIfLayoutPaddingsPlusContentEqualsResolution()
	{
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);

		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		_mockedDeviceService.getTileSize();
		expectLastCall().andDelegateTo(deviceService).anyTimes();
		replay(_mockedDeviceService);

		AppSettings appSettings = appSettingsService.getAppSettings();

		systemParamsService.newResolutionWereSet(480, 315);

		SystemParams systemParams = systemParamsService.getSystemParams();
		int tileSize = deviceService.getTileSize();

		DeviceCoords topLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, 0));
		DeviceCoords topRight = deviceService.WorldPositionToDeviceCoords(new WorldPosition(appSettings.tilesHorizontally - 1, 0));
		DeviceCoords bottomLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, appSettings.tilesVertically - 1));
		DeviceCoords bottomRight = deviceService.WorldPositionToDeviceCoords(new WorldPosition(appSettings.tilesHorizontally - 1,
				appSettings.tilesVertically - 1));

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
		tileSize = deviceService.getTileSize();

		topLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, 0));
		topRight = deviceService.WorldPositionToDeviceCoords(new WorldPosition(appSettings.tilesHorizontally - 1, 0));
		bottomLeft = deviceService.WorldPositionToDeviceCoords(new WorldPosition(0, appSettings.tilesVertically - 1));
		bottomRight = deviceService.WorldPositionToDeviceCoords(new WorldPosition(appSettings.tilesHorizontally - 1, appSettings.tilesVertically - 1));

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
