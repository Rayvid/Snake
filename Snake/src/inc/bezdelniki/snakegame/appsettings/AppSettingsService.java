package inc.bezdelniki.snakegame.appsettings;

import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterCoords;

public class AppSettingsService implements IAppSettingsService {

	@Override
	public AppSettings getAppSettings() {
		AppSettings settings = new AppSettings();
		settings.initialWidth = 480;
		settings.initialHeight = 320;

		settings.initialDirection = Direction.RIGHT;
		settings.initialSnakeLength = 5;
		settings.initialHeadPositionX = 20;
		settings.initialHeadPositionY = 12;
		
		settings.tilesHorizontally = 30;
		settings.tilesVertically = 20;
		settings.textureSize = 16;
		settings.growSnakeBy = 2;
		
		settings.snakesMovementNanoInterval = 1000000000; // 1s
		
		return settings;
	}

}
