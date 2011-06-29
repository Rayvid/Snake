package inc.bezdelniki.snake.desktop;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Main {
    public static void main (String[] args) {
    	IAppSettingsService appSettingsService = SnakeInjector.getInstance().getInstance(IAppSettingsService.class);
    	AppSettings appSettings = appSettingsService.GetAppSettings();
        new LwjglApplication(new inc.bezdelniki.snakegame.Main(), "Snake", appSettings.initialWidth, appSettings.initialHeight, false);
    }
}