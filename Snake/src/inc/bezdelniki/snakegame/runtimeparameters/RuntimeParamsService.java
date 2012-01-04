package inc.bezdelniki.snakegame.runtimeparameters;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.runtimeparameters.dto.LayoutParams;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public class RuntimeParamsService implements IRuntimeParamsService
{
	private IDeviceService _deviceService;
	private ISystemParamsService _systemParamsService;
	private IAppSettingsService _appSettingsService;

	@Inject
	public RuntimeParamsService(
			ISystemParamsService systemParamsService,
			IAppSettingsService appSettingsService,
			IDeviceService deviceService)
	{
		_systemParamsService = systemParamsService;
		_appSettingsService = appSettingsService;
		_deviceService = deviceService;
	}

	@Override
	public RuntimeParams createParamsForNewGame()
	{
		RuntimeParams result = new RuntimeParams();
		
		adjustLayoutParams(result);
		
		result.snakesMovementNanoInterval = 200000000; // 0.2s

		return result;
	}

	@Override
	public void adjustLayoutParams(RuntimeParams runtimeParams)
	{
		LayoutParams zeroPaddingLayout = new LayoutParams();

		int tileSize = _deviceService.getTileSize(zeroPaddingLayout);
		
		runtimeParams.layoutParams = new LayoutParams();
		runtimeParams.layoutParams.gameBoxPaddingTop = tileSize * 2;
		runtimeParams.layoutParams.gameBoxPaddingLeft = tileSize;
		runtimeParams.layoutParams.gameBoxPaddingRight = tileSize;
		runtimeParams.layoutParams.gameBoxPaddingBottom = tileSize;

		SystemParams systemParams = _systemParamsService.getSystemParams();
		AppSettings appSettings = _appSettingsService.getAppSettings();
		tileSize = _deviceService.getTileSize(runtimeParams.layoutParams);

		if (systemParams.width > systemParams.height)
		{
			int whatsLeft = systemParams.width - appSettings.tilesHorizontally * tileSize
					- runtimeParams.layoutParams.gameBoxPaddingLeft
					- runtimeParams.layoutParams.gameBoxPaddingRight;
			runtimeParams.layoutParams.gameBoxPaddingLeft += whatsLeft / 2;
			runtimeParams.layoutParams.gameBoxPaddingRight += whatsLeft - whatsLeft / 2;

			whatsLeft = systemParams.height - appSettings.tilesVertically * tileSize
					- runtimeParams.layoutParams.gameBoxPaddingTop
					- runtimeParams.layoutParams.gameBoxPaddingBottom;
			runtimeParams.layoutParams.gameBoxPaddingTop += whatsLeft / 2;
			runtimeParams.layoutParams.gameBoxPaddingBottom += whatsLeft - whatsLeft / 2;
		}
		else
		{
			int whatsLeft = systemParams.height - appSettings.tilesHorizontally * tileSize
					- runtimeParams.layoutParams.gameBoxPaddingTop
					- runtimeParams.layoutParams.gameBoxPaddingBottom;
			runtimeParams.layoutParams.gameBoxPaddingTop += whatsLeft / 2;
			runtimeParams.layoutParams.gameBoxPaddingBottom += whatsLeft - whatsLeft / 2;

			whatsLeft = systemParams.width - appSettings.tilesVertically * tileSize
					- runtimeParams.layoutParams.gameBoxPaddingLeft
					- runtimeParams.layoutParams.gameBoxPaddingRight;
			runtimeParams.layoutParams.gameBoxPaddingLeft += whatsLeft / 2;
			runtimeParams.layoutParams.gameBoxPaddingRight += whatsLeft - whatsLeft / 2;
		}

		runtimeParams.layoutParams.scoreCoords = new DeviceCoords(2, systemParams.height - 2);
	}

}