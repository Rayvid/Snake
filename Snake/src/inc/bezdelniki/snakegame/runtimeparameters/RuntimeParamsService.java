package inc.bezdelniki.snakegame.runtimeparameters;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.device.IDeviceService;
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
		LayoutParams zeroPaddingLayout = new LayoutParams();

		result.layoutParams = new LayoutParams();
		result.layoutParams.gameBoxPaddingTop = _deviceService.getTileSize(zeroPaddingLayout) * 2;
		result.layoutParams.gameBoxPaddingLeft = (int) (_deviceService.getTileSize(zeroPaddingLayout) * 1);
		result.layoutParams.gameBoxPaddingRight = (int) (_deviceService.getTileSize(zeroPaddingLayout) * 1);
		result.layoutParams.gameBoxPaddingBottom = (int) (_deviceService.getTileSize(zeroPaddingLayout) * 1);

		SystemParams systemParams = _systemParamsService.getSystemParams();
		AppSettings appSettings = _appSettingsService.getAppSettings();
		int tileSize = _deviceService.getTileSize(result.layoutParams);

		if (systemParams.width > systemParams.height)
		{
			int whatsLeft = systemParams.width - appSettings.tilesHorizontally * tileSize
					- result.layoutParams.gameBoxPaddingLeft
					- result.layoutParams.gameBoxPaddingRight;
			result.layoutParams.gameBoxPaddingLeft += whatsLeft / 2;
			result.layoutParams.gameBoxPaddingRight += whatsLeft - whatsLeft / 2;

			whatsLeft = systemParams.height - appSettings.tilesVertically * tileSize
					- result.layoutParams.gameBoxPaddingTop
					- result.layoutParams.gameBoxPaddingBottom;
			result.layoutParams.gameBoxPaddingTop += whatsLeft / 2;
			result.layoutParams.gameBoxPaddingBottom += whatsLeft - whatsLeft / 2;
		}
		else
		{
			int whatsLeft = systemParams.height - appSettings.tilesHorizontally * tileSize
					- result.layoutParams.gameBoxPaddingTop
					- result.layoutParams.gameBoxPaddingBottom;
			result.layoutParams.gameBoxPaddingTop += whatsLeft / 2;
			result.layoutParams.gameBoxPaddingBottom += whatsLeft - whatsLeft / 2;

			whatsLeft = systemParams.width - appSettings.tilesVertically * tileSize
					- result.layoutParams.gameBoxPaddingLeft
					- result.layoutParams.gameBoxPaddingRight;
			result.layoutParams.gameBoxPaddingLeft += whatsLeft / 2;
			result.layoutParams.gameBoxPaddingRight += whatsLeft - whatsLeft / 2;
		}

		result.snakesMovementNanoInterval = 1000000000; // 1s

		return result;
	}

}
