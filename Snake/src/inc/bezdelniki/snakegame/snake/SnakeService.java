package inc.bezdelniki.snakegame.snake;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;

public class SnakeService implements ISnakeService {
	private IAppSettingsService _appSettingsService;

	@Inject
	SnakeService (IAppSettingsService appSettingsService) {
		_appSettingsService = appSettingsService;
	}
	
	@Override
	public void CreateSnake(GameWorld world) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void GrowSnake(GameWorld world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveSnake(GameWorld world) {
		// TODO Auto-generated method stub
		
	}

}
