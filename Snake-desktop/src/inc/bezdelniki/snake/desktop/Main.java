package inc.bezdelniki.snake.desktop;

import inc.bezdelniki.snake.SnakeInjector;
import inc.bezdelniki.snake.appsettings.IAppSettingsService;
import inc.bezdelniki.snake.appsettings.dto.AppSettings;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Main {
    public static void main (String[] args) {
    	IAppSettingsService appSettingsService = SnakeInjector.getInstance().getInstance(IAppSettingsService.class);
    	AppSettings appSettings = appSettingsService.GetAppSettings();
        new LwjglApplication(new inc.bezdelniki.snake.Main(), "Snake", appSettings.initialWidth, appSettings.initialHeight, false);
    }
}