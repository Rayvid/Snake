package inc.bezdelniki.snake.appsettings;

import inc.bezdelniki.snake.appsettings.dto.AppSettings;

public class AppSettingsService implements IAppSettingsService {

	@Override
	public AppSettings GetAppSettings() {
		AppSettings settings = new AppSettings();
		settings.initialWidth = 480;
		settings.initialHeight = 320;
		settings.tilesHorizontally = 40;
		settings.tilesVertically = 15;
		return settings;
	}

}
