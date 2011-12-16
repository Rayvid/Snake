package inc.bezdelniki.snakegame.device;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

public class DeviceService implements IDeviceService
{
	private IAppSettingsService _appSettingsService;
	private ISystemParametersService _systemParametersService;

	@Inject
	public DeviceService(ISystemParametersService systemParametersService, IAppSettingsService appSettingsService)
	{
		_systemParametersService = systemParametersService;
		_appSettingsService = appSettingsService;
	}

	@Override
	public DeviceCoords WorldPositionToDeviceCoords(WorldPosition position)
	{
		DeviceDeltas deltas = getDeltas();

		DeviceCoords deviceCoords =
				new DeviceCoords(
						position.tileX * deltas.deltaDeviceXForWorldX * getTileSize() + position.tileY * deltas.deltaDeviceXForWorldY * getTileSize(),
						_systemParametersService.getSystemParameters().height
								- (position.tileX * deltas.deltaDeviceYForWorldX * getTileSize()
										+ position.tileY * deltas.deltaDeviceYForWorldY * getTileSize()));

		return deviceCoords;
	}

	@Override
	public WorldPosition DeviceCoordsToWorldPosition(DeviceCoords coords)
	{
		AppSettings appSettings = _appSettingsService.getAppSettings();

		int x = appSettings.tilesHorizontally / 2;
		int y = appSettings.tilesVertically / 2;
		while (true)
		{
			WorldPosition currWorldPosition = new WorldPosition(x, y);

			DeviceCoords currCoords = WorldPositionToDeviceCoords(currWorldPosition);
			DeviceCoords rightCoords = WorldPositionToDeviceCoords(new WorldPosition(x + 1, y));
			DeviceCoords downCoords = WorldPositionToDeviceCoords(new WorldPosition(x, y + 1));

			if (coords.x >= Math.min(Math.min(currCoords.x, rightCoords.x), Math.min(currCoords.x, downCoords.x))
					&& coords.x < Math.max(Math.max(currCoords.x, rightCoords.x), Math.max(currCoords.x, downCoords.x)))
			{
				if (coords.y > Math.min(Math.min(currCoords.y, rightCoords.y), Math.min(currCoords.y, downCoords.y))
						&& coords.y <= Math.max(Math.max(currCoords.y, rightCoords.y), Math.max(currCoords.y, downCoords.y)))
				{
					break;
				}
			}
			else
			{
				if (coords.x > Math.min(currCoords.x, rightCoords.x))
				{
					if (rightCoords.x != currCoords.x)
					{
						if (rightCoords.x > currCoords.x)
						{
							x++;
						}
						else
						{
							x--;
						}
					}
					else
					{
						if (downCoords.x > currCoords.x)
						{
							y++;
						}
						else
						{
							y--;
						}
					}
				}
				else
				{
					if (rightCoords.x != currCoords.x)
					{
						if (rightCoords.x > currCoords.x)
						{
							x--;
						}
						else
						{
							x++;
						}
					}
					else
					{
						if (downCoords.x > currCoords.x)
						{
							y--;
						}
						else
						{
							y++;
						}
					}
				}
			}

			if (coords.y <= Math.min(Math.min(currCoords.y, rightCoords.y), Math.min(currCoords.y, downCoords.y))
					|| coords.y > Math.max(Math.max(currCoords.y, rightCoords.y), Math.max(currCoords.y, downCoords.y)))
			{
				if (coords.y > Math.min(currCoords.y, rightCoords.y))
				{
					if (rightCoords.y != currCoords.y)
					{
						if (rightCoords.y > currCoords.y)
						{
							x++;
						}
						else
						{
							x--;
						}
					}
					else
					{
						if (downCoords.y > currCoords.y)
						{
							y++;
						}
						else
						{
							y--;
						}
					}
				}
				else
				{
					if (rightCoords.y != currCoords.y)
					{
						if (rightCoords.y > currCoords.y)
						{
							x--;
						}
						else
						{
							x++;
						}
					}
					else
					{
						if (downCoords.y > currCoords.y)
						{
							y--;
						}
						else
						{
							y++;
						}
					}
				}
			}
		}

		return new WorldPosition(x, y);
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
