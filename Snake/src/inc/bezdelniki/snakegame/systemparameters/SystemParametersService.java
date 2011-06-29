package inc.bezdelniki.snakegame.systemparameters;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;


public class SystemParametersService implements ISystemParametersService {
	private int _width;
	private int _height;
	private IAppSettingsService _appSettingsService;
	
	@Inject
	public SystemParametersService(IAppSettingsService appSettingsService)
	{
		_appSettingsService = appSettingsService;
		
		AppSettings appSettings = _appSettingsService.GetAppSettings();
		_width = appSettings.initialWidth;
		_height = appSettings.initialHeight;
	}
	
	@Override
	public SystemParameters GetSystemParameters() {
		SystemParameters systemParameters = new SystemParameters();
		
		systemParameters.width = _width;
		systemParameters.height = _height;
		
		return systemParameters;
	}
	
	@Override
	public void NewResolutionWereSet(int width, int height) {
		_width = width;
		_height = height; 
	}
}