package inc.bezdelniki.snakegame.appsettings;

import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.model.enums.Direction;

public class AppSettingsService implements IAppSettingsService
{

	@Override
	public AppSettings getAppSettings()
	{
		AppSettings settings = new AppSettings();
		settings.initialWidth = 540;
		settings.initialHeight = 320;

		settings.initialDirection = Direction.RIGHT;
		settings.initialSnakeLength = 5;
		settings.initialHeadPositionX = 18;
		settings.initialHeadPositionY = 10;

		settings.tilesHorizontally = 36;
		settings.tilesVertically = 20;
		settings.growSnakeBy = 2;

		return settings;
	}

}
