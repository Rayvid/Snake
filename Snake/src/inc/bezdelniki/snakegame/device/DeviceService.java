package inc.bezdelniki.snakegame.device;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

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
	public DeviceCoords WorldPositionToDeviceCoords(WorldPosition position)
	{
		DeviceDeltas deltas = getDeltas();

		int tileSize = getTileSize();
		DeviceCoords deviceCoords =
				new DeviceCoords(
						position.tileX * deltas.deltaDeviceXForWorldX * tileSize
							+ (_systemParametersService.getSystemParameters().width - tileSize - position.tileY * deltas.deltaDeviceXForWorldY * tileSize) * deltas.deltaDeviceXForWorldY,
						_systemParametersService.getSystemParameters().height - tileSize
							- (position.tileY * deltas.deltaDeviceYForWorldY * tileSize + position.tileX * deltas.deltaDeviceYForWorldX * tileSize));

		return deviceCoords;
	}

	@Override
	public WorldPosition DeviceCoordsToWorldPosition(DeviceCoords coords)
	{
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();
		int tileSize = getTileSize();
		
		DeviceCoords zeroCoords = WorldPositionToDeviceCoords(new WorldPosition(0, 0));
		DeviceCoords toTheRightCoords = WorldPositionToDeviceCoords(new WorldPosition(1, 0));
		DeviceCoords downCoords = WorldPositionToDeviceCoords(new WorldPosition(0, 1));
		
		WorldPosition result = new WorldPosition(0, 0);
		if (zeroCoords.x == toTheRightCoords.x)
		{
			if (zeroCoords.y > toTheRightCoords.y)
			{
				result.tileX = (systemParameters.height - coords.y - 1) / tileSize;
			}
			else
			{
				result.tileX = coords.y / tileSize;
			}
		}
		else
		{
			if (zeroCoords.x > toTheRightCoords.x)
			{
				result.tileX = (systemParameters.width - coords.x - 1) / tileSize;
			}
			else
			{
				result.tileX = coords.x / tileSize;
			}
		}
		
		if (zeroCoords.y == downCoords.y)
		{
			if (zeroCoords.x > downCoords.x)
			{
				result.tileY = (systemParameters.width - coords.x - 1) / tileSize;
			}
			else
			{
				result.tileY = coords.x / tileSize;
			}
		}
		else
		{
			if (zeroCoords.y > downCoords.y)
			{
				result.tileY = (systemParameters.height - coords.y - 1) / tileSize;
			}
			else
			{
				result.tileY = coords.y / tileSize;
			}
		}

		return result;
	}

	@Override
	public DeviceCoords TouchCoordsToDeviceCoords(TouchCoords touchCoords)
	{
		return new DeviceCoords(
				touchCoords.x,
				_systemParametersService.getSystemParameters().height - touchCoords.y);
	}

	@Override
	public TouchCoords DeviceCoordsToTouchCoords(DeviceCoords deviceCoords)
	{
		return new TouchCoords(
				deviceCoords.x,
				_systemParametersService.getSystemParameters().height - deviceCoords.y);
	}

	@Override
	public DeviceDeltas getDeltas()
	{
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();
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
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();

		return Math.min(Math.max(systemParameters.height, systemParameters.width) / appSettings.tilesHorizontally,
				Math.min(systemParameters.height, systemParameters.width) / appSettings.tilesVertically);
	}
}
