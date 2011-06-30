package inc.bezdelniki.snakegame.snake;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;
import inc.bezdelniki.snakegame.snake.dtos.Snake;

public class SnakeService implements ISnakeService {
	private IAppSettingsService _appSettingsService;

	@Inject
	SnakeService (IAppSettingsService appSettingsService) {
		_appSettingsService = appSettingsService;
	}
	
	@Override
	public void CreateSnake(GameWorld world) {
		AppSettings settings = _appSettingsService.GetAppSettings();
		
		WorldPosition position = new WorldPosition();
		position.tileX = settings.initialHeadPositionX;
		position.tileY = settings.initialHeadPositionY;
		
		Snake snake = new Snake();
		snake.currLength = 1;
		snake.headPosition = position;
		snake.newLength = settings.initialSnakeLength;
		
		world.setSnake(snake);
	}
	
	@Override
	public void GrowSnake(GameWorld world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveSnake(GameWorld world) {
		world.setSnake(null);
	}

}
