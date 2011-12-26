package inc.bezdelniki.snakegame.device;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.runtimeparameters.dto.LayoutParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public class DeviceService implements IDeviceService
{
	private IAppSettingsService _appSettingsService;
	private ISystemParamsService _systemParametersService;

	@Inject
	public DeviceService(ISystemParamsService systemParametersService, IAppSettingsService appSettingsService)
	{
		_systemParametersService = systemParametersService;
		_appSettingsService = appSettingsService;
	}

	@Override
	public DeviceCoords WorldPositionToDeviceCoords(WorldPosition position, LayoutParams layoutParams)
	{
		DeviceDeltas deltas = getDeltas();
		SystemParams systemParams = _systemParametersService.getSystemParams();

		int tileSize = getTileSize(layoutParams);
		DeviceCoords deviceCoords =
				new DeviceCoords(
						(layoutParams.gameBoxPaddingLeft + position.tileX * tileSize)
								* deltas.deltaDeviceXForWorldX
								+ (systemParams.width - layoutParams.gameBoxPaddingLeft - tileSize - position.tileY * tileSize)
								* deltas.deltaDeviceXForWorldY,
						systemParams.height - layoutParams.gameBoxPaddingTop - tileSize
								- (position.tileY * deltas.deltaDeviceYForWorldY * tileSize + position.tileX * deltas.deltaDeviceYForWorldX * tileSize));

		return deviceCoords;
	}

	@Override
	public WorldPosition DeviceCoordsToWorldPosition(DeviceCoords coords, LayoutParams layoutParams)
	{
		SystemParams systemParameters = _systemParametersService.getSystemParams();
		int tileSize = getTileSize(layoutParams);

		DeviceCoords zeroCoords = WorldPositionToDeviceCoords(new WorldPosition(0, 0), layoutParams);
		DeviceCoords toTheRightCoords = WorldPositionToDeviceCoords(new WorldPosition(1, 0), layoutParams);
		DeviceCoords downCoords = WorldPositionToDeviceCoords(new WorldPosition(0, 1), layoutParams);

		WorldPosition result = new WorldPosition(0, 0);
		if (zeroCoords.x == toTheRightCoords.x)
		{
			if (zeroCoords.y > toTheRightCoords.y)
			{
				result.tileX = (systemParameters.height - layoutParams.gameBoxPaddingTop - coords.y - 1) / tileSize;
			}
			else
			{
				result.tileX = (coords.y - layoutParams.gameBoxPaddingTop) / tileSize;
			}
		}
		else
		{
			if (zeroCoords.x > toTheRightCoords.x)
			{
				result.tileX = (systemParameters.width - layoutParams.gameBoxPaddingLeft - coords.x - 1) / tileSize;
			}
			else
			{
				result.tileX = (coords.x - layoutParams.gameBoxPaddingLeft) / tileSize;
			}
		}

		if (zeroCoords.y == downCoords.y)
		{
			if (zeroCoords.x > downCoords.x)
			{
				result.tileY = (systemParameters.width - layoutParams.gameBoxPaddingLeft - coords.x - 1) / tileSize;
			}
			else
			{
				result.tileY = (coords.x - layoutParams.gameBoxPaddingLeft) / tileSize;
			}
		}
		else
		{
			if (zeroCoords.y > downCoords.y)
			{
				result.tileY = (systemParameters.height - layoutParams.gameBoxPaddingTop - coords.y - 1) / tileSize;
			}
			else
			{
				result.tileY = (coords.y - layoutParams.gameBoxPaddingTop) / tileSize;
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
	public int getTileSize(LayoutParams layoutParams)
	{
		AppSettings appSettings = _appSettingsService.getAppSettings();
		SystemParams systemParameters = _systemParametersService.getSystemParams();

		return Math.min(
				Math.max(
						systemParameters.height - layoutParams.gameBoxPaddingTop - layoutParams.gameBoxPaddingBottom,
						systemParameters.width - layoutParams.gameBoxPaddingLeft - layoutParams.gameBoxPaddingRight)
						/ appSettings.tilesHorizontally,
				Math.min(
						systemParameters.height - layoutParams.gameBoxPaddingTop - layoutParams.gameBoxPaddingBottom,
						systemParameters.width - layoutParams.gameBoxPaddingLeft - layoutParams.gameBoxPaddingRight)
						/ appSettings.tilesVertically);
	}
}
