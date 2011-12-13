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
	public DeviceCoords WorldPositionToDeviceCoords(WorldPosition position) {
		DeviceDeltas deltas = getDeltas();

		DeviceCoords presenterCoords = new DeviceCoords(
					position.tileX * deltas.deltaDeviceXForWorldX
						* getTileSize() + position.tileY * deltas.deltaDeviceXForWorldY
						* getTileSize(),
					_systemParametersService.getSystemParameters().height
						- (position.tileX * deltas.deltaDeviceYForWorldX * getTileSize() + position.tileY
						* deltas.deltaDeviceYForWorldY * getTileSize())
				);

		return presenterCoords;
	}
	

	@Override
	public WorldPosition DeviceCoordsToWorldPosition(
			DeviceCoords coords) {
		AppSettings appSettings = _appSettingsService.getAppSettings();
			
		for (int x = 0; x < appSettings.tilesHorizontally; x++) {	
			for (int y = 0; y < appSettings.tilesVertically; y++) {
				WorldPosition currWorldPosition = new WorldPosition(x, y);
				
				DeviceCoords currCoords = WorldPositionToDeviceCoords(currWorldPosition);
				DeviceCoords rightBottomCoords = WorldPositionToDeviceCoords(new WorldPosition(x + 1 , y + 1));
				
				if (coords.x >= Math.min(currCoords.x, rightBottomCoords.x) && coords.x < Math.max(currCoords.x, rightBottomCoords.x) &&
					coords.y > Math.min(currCoords.y, rightBottomCoords.y) && coords.y <= Math.max(currCoords.y, rightBottomCoords.y)){
					return currWorldPosition;
				}
			}
		}
		
		return null;
	}

	@Override
	public DeviceDeltas getDeltas() {
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();
		DeviceDeltas deltas = new DeviceDeltas();
		
		if (systemParameters.width >= systemParameters.height) {
			deltas.deltaDeviceXForWorldX = 1;
			deltas.deltaDeviceYForWorldX = 0;
			deltas.deltaDeviceXForWorldY = 0;
			deltas.deltaDeviceYForWorldY = 1;
		} else {
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
		SystemParameters systemParameters = _systemParametersService
				.getSystemParameters();

		return Math.min(
				Math.max(systemParameters.height, systemParameters.width)
						/ appSettings.tilesHorizontally,
				Math.min(systemParameters.height, systemParameters.width)
						/ appSettings.tilesVertically);
	}
}
