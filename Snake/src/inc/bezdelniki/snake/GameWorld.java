package inc.bezdelniki.snake;

import java.util.ArrayList;
import java.util.List;

import inc.bezdelniki.snake.appsettings.IAppSettingsService;
import inc.bezdelniki.snake.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snake.systemparameters.ISystemParametersService;

import com.google.inject.*;

public class GameWorld {
	private IAppSettingsService _appSettingsService;
	
	private List<LyingItem> _lyingItems = new ArrayList<LyingItem>();
	
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

	public List<LyingItem> getLyingItems() {
		return _lyingItems;
	}
}
