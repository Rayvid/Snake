package inc.bezdelniki.snakegame.systemparameters;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public class SystemParamsService implements ISystemParamsService
{
	private int _width;
	private int _height;
	
	private IAppSettingsService _appSettingsService;
	private IRuntimeParamsService _runtimeParamsService;
	private RuntimeParams _runtimeParams;
	private IPresentationService _presentationService;

	@Inject
	public SystemParamsService(
			IAppSettingsService appSettingsService,
			IRuntimeParamsService runtimeParamsService,
			RuntimeParams runtimeParams,
			IPresentationService presentationService)
	{
		_appSettingsService = appSettingsService;
		_runtimeParamsService = runtimeParamsService;
		_runtimeParams = runtimeParams;
		_presentationService = presentationService;

		AppSettings appSettings = _appSettingsService.getAppSettings();
		_width = appSettings.initialWidth;
		_height = appSettings.initialHeight;
	}

	@Override
	public SystemParams getSystemParams()
	{
		SystemParams systemParameters = new SystemParams();

		systemParameters.width = _width;
		systemParameters.height = _height;

		return systemParameters;
	}

	@Override
	public void newResolutionWereSet(int width, int height)
	{
		_width = width;
		_height = height;
		
		_runtimeParamsService.adjustLayoutParams(_runtimeParams);
		_presentationService.adjustToChangedResolution();
	}
}
