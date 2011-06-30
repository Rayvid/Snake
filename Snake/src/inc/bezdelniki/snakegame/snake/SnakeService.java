package inc.bezdelniki.snakegame.snake;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	public void createSnake(GameWorld world) {
		AppSettings settings = _appSettingsService.getAppSettings();
		
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
	public void growSnake(GameWorld world) {
		AppSettings settings = _appSettingsService.getAppSettings();
		
		world.getSnake().newLength += settings.growSnakeBy;
	}
	
	@Override
	public boolean moveSnake(GameWorld world) {
		return false;
	}

	@Override
	public void removeSnake(GameWorld world) {
		world.setSnake(null);
	}

	@Override
	public void drawSnake(SpriteBatch batch, GameWorld world) {
		// TODO Auto-generated method stub
	}
	
	public boolean isTileInSnakesPath(GameWorld world, int tileX, int tileY) {
		return false;
	}
}
