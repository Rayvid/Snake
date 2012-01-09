package inc.bezdelniki.snakegame.device;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public class DeviceService implements IDeviceService
{
	private IAppSettingsService _appSettingsService;
	private ISystemParamsService _systemParametersService;
	private RuntimeParams _runtimeParams;

	@Inject
	public DeviceService(
			ISystemParamsService systemParametersService,
			IAppSettingsService appSettingsService,
			RuntimeParams runtimeParams)
	{
		_systemParametersService = systemParametersService;
		_appSettingsService = appSettingsService;
		_runtimeParams = runtimeParams;
	}

	@Override
	public DeviceCoords WorldPositionToDeviceCoords(WorldPosition position)
	{
		DeviceDeltas deltas = getDeltas();
		SystemParams systemParams = _systemParametersService.getSystemParams();

		int tileSize = getTileSize();
		DeviceCoords deviceCoords =
				new DeviceCoords(
						(_runtimeParams.layoutParams.gameBoxPaddingLeft + position.tileX * tileSize)
								* deltas.deltaDeviceXForWorldX
								+ (systemParams.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - tileSize - position.tileY * tileSize)
								* deltas.deltaDeviceXForWorldY,
						systemParams.height - _runtimeParams.layoutParams.gameBoxPaddingTop - tileSize
								- (position.tileY * deltas.deltaDeviceYForWorldY * tileSize + position.tileX * deltas.deltaDeviceYForWorldX * tileSize));

		return deviceCoords;
	}

	@Override
	public WorldPosition DeviceCoordsToWorldPosition(DeviceCoords coords)
	{
		SystemParams systemParameters = _systemParametersService.getSystemParams();
		int tileSize = getTileSize();

		DeviceCoords zeroCoords = WorldPositionToDeviceCoords(new WorldPosition(0, 0));
		DeviceCoords toTheRightCoords = WorldPositionToDeviceCoords(new WorldPosition(1, 0));
		DeviceCoords downCoords = WorldPositionToDeviceCoords(new WorldPosition(0, 1));

		WorldPosition result = new WorldPosition(0, 0);
		if (zeroCoords.x == toTheRightCoords.x)
		{
			if (zeroCoords.y > toTheRightCoords.y)
			{
				result.tileX = (systemParameters.height - _runtimeParams.layoutParams.gameBoxPaddingTop - coords.y - 1) / tileSize;
			}
			else
			{
				result.tileX = (coords.y - _runtimeParams.layoutParams.gameBoxPaddingTop) / tileSize;
			}
		}
		else
		{
			if (zeroCoords.x > toTheRightCoords.x)
			{
				result.tileX = (systemParameters.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - coords.x - 1) / tileSize;
			}
			else
			{
				result.tileX = (coords.x - _runtimeParams.layoutParams.gameBoxPaddingLeft) / tileSize;
			}
		}

		if (zeroCoords.y == downCoords.y)
		{
			if (zeroCoords.x > downCoords.x)
			{
				result.tileY = (systemParameters.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - coords.x - 1) / tileSize;
			}
			else
			{
				result.tileY = (coords.x - _runtimeParams.layoutParams.gameBoxPaddingLeft) / tileSize;
			}
		}
		else
		{
			if (zeroCoords.y > downCoords.y)
			{
				result.tileY = (systemParameters.height - _runtimeParams.layoutParams.gameBoxPaddingTop - coords.y - 1) / tileSize;
			}
			else
			{
				result.tileY = (coords.y - _runtimeParams.layoutParams.gameBoxPaddingTop) / tileSize;
			}
		}

		return result;
	}

	@Override
	public DeviceCoords TouchCoordsToDeviceCoords(TouchCoords touchCoords)
	{
		return new DeviceCoords(
				touchCoords.x,
				_systemParametersService.getSystemParams().height - touchCoords.y);
	}

	@Override
	public TouchCoords DeviceCoordsToTouchCoords(DeviceCoords deviceCoords)
	{
		return new TouchCoords(
				deviceCoords.x,
				_systemParametersService.getSystemParams().height - deviceCoords.y);
	}

	@Override
	public DeviceDeltas getDeltas()
	{
		SystemParams systemParameters = _systemParametersService.getSystemParams();
		DeviceDeltas deltas = new DeviceDeltas();

		if (systemParameters.width >= systemParameters.height)
		{
			deltas.deltaDeviceXForWorldX = 1;
			deltas.deltaDeviceYForWorldX = 0;
			deltas.deltaDeviceXForWorldY = 0;
			deltas.deltaDeviceYForWorldY = 1;
		}
		else
		{
			deltas.deltaDeviceXForWorldX = 0;
			deltas.deltaDeviceYForWorldX = 1;
			deltas.deltaDeviceXForWorldY = 1;
			deltas.deltaDeviceYForWorldY = 0;
		}

		return deltas;
	}

	@Override
	public int getTileSize()
	{
		AppSettings appSettings = _appSettingsService.getAppSettings();
		SystemParams systemParameters = _systemParametersService.getSystemParams();

		return Math.min(
				Math.max(
						systemParameters.height - _runtimeParams.layoutParams.gameBoxPaddingTop - _runtimeParams.layoutParams.gameBoxPaddingBottom,
						systemParameters.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - _runtimeParams.layoutParams.gameBoxPaddingRight)
						/ appSettings.tilesHorizontally,
				Math.min(
						systemParameters.height - _runtimeParams.layoutParams.gameBoxPaddingTop - _runtimeParams.layoutParams.gameBoxPaddingBottom,
						systemParameters.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - _runtimeParams.layoutParams.gameBoxPaddingRight)
						/ appSettings.tilesVertically);
	}
}
