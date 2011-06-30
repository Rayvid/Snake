package inc.bezdelniki.snakegame;

import java.util.ArrayList;
import java.util.List;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.snake.dtos.Snake;

import com.google.inject.*;

public class GameWorld {
	private IAppSettingsService _appSettingsService;
	
	private List<LyingItem> _lyingItems = new ArrayList<LyingItem>();
	private Snake _snake;
	
	@Inject
	GameWorld (
			IAppSettingsService appSettingsService)
	{
		_appSettingsService = appSettingsService;
	}
	
	public int getGameWorldWidth()
	{
		return _appSettingsService.GetAppSettings().tilesHorizontally;
	}
	
	public int getGameWorldHeight()
	{
		return _appSettingsService.GetAppSettings().tilesVertically;
	}
	
	public boolean isWorldTileOccupied(int posX, int posY)
	{
		for (LyingItem item : _lyingItems) {
			if (item.position.tileX == posX && item.position.tileY == posY) {
				return true;
			}
		}
		
		return false;
	}

	public List<LyingItem> getLyingItems() {
		return _lyingItems;
	}
	
	public Snake getSnake() {
		return _snake;
	}

	public void setSnake(Snake snake) {
		_snake = snake;
	}
}
