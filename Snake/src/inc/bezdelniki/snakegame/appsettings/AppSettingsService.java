package inc.bezdelniki.snakegame.appsettings;

import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.model.enums.Direction;

public class AppSettingsService implements IAppSettingsService {

	@Override
	public AppSettings GetAppSettings() {
		AppSettings settings = new AppSettings();
		settings.initialWidth = 480;
		settings.initialHeight = 320;
		settings.tilesHorizontally = 40;
		settings.tilesVertically = 15;
		settings.initialSnakeDirection = Direction.RIGHT;
		settings.initialSnakeLength = 5;
		settings.initialHeadPositionX = 20;
		settings.initialHeadPositionY = 7;
		
		
		return settings;
	}

}
