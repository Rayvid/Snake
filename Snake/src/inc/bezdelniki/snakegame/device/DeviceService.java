package inc.bezdelniki.snakegame.device;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

public class DeviceService implements IDeviceService {
	private IAppSettingsService _appSettingsService;
	private ISystemParametersService _systemParametersService;
	
	@Inject
	public DeviceService(
			ISystemParametersService systemParametersService,
			IAppSettingsService appSettingsService)
	{
		_systemParametersService = systemParametersService;
		_appSettingsService = appSettingsService;
	}
	
	@Override
	public DeviceCoords WorldCoordsToDeviceCoords(WorldPosition position) {
		DeviceDeltas deltas = getDeltas();

		DeviceCoords presenterCoords = new DeviceCoords();

		presenterCoords.x = position.tileX * deltas.deltaXForWorldX
				* getTileSize() + position.tileY * deltas.deltaXForWorldY
				* getTileSize();
		presenterCoords.y = _systemParametersService.getSystemParameters().height
				- (position.tileX * deltas.deltaYForWorldX * getTileSize() + position.tileY
				* deltas.deltaYForWorldY * getTileSize());

		return presenterCoords;
	}

	@Override
	public DeviceDeltas getDeltas() {
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();
		DeviceDeltas deltas = new DeviceDeltas();
		
		if (systemParameters.width >= systemParameters.height) {
			deltas.deltaXForWorldX = 1;
			deltas.deltaYForWorldX = 0;
			deltas.deltaXForWorldY = 0;
			deltas.deltaYForWorldY = 1;
		} else {
			deltas.deltaXForWorldX = 0;
			deltas.deltaYForWorldX = 1;
			deltas.deltaXForWorldY = 1;
			deltas.deltaYForWorldY = 0;
		}
		
		return deltas;
	}

	@Override
	public int getTileSize()
	{
		AppSettings appSettings = _appSettingsService.getAppSettings();
		SystemParameters systemParameters = _systemParametersService
				.getSystemParameters();

		return Math.min(
				Math.max(systemParameters.height, systemParameters.width)
						/ appSettings.tilesHorizontally,
				Math.min(systemParameters.height, systemParameters.width)
						/ appSettings.tilesVertically);
	}
}
