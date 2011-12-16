package inc.bezdelniki.snakegame.gameworld;

import java.util.ArrayList;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public class GameWorldService implements IGameWorldService
{

	private IAppSettingsService _appSettingsService;
	private ISnakeService _snakeService;
	private ITimeService _timeService;
	private GameWorld _gameWorld = null;

	@Inject
	public GameWorldService(IAppSettingsService appSettingsService, ISnakeService snakeService, ITimeService timeService)
	{
		_appSettingsService = appSettingsService;
		_snakeService = snakeService;
		_timeService = timeService;
	}

	@Override
	public void initGameWorld()
	{
		_gameWorld = new GameWorld();

		_gameWorld.lyingItems = new ArrayList<LyingItem>();
		_gameWorld.snake = _snakeService.createSnake();
		_gameWorld.movementChangesInEffect = new ArrayList<SnakeMovementChange>();
		_gameWorld.lastMoveNanoTimestamp = _timeService.getNanoStamp();
	}

	@Override
	public GameWorld getGameWorld()
	{
		return _gameWorld;
	}

	@Override
	public void applyLyingItem(LyingItem lyingItem)
	{
		_gameWorld.lyingItems.add(lyingItem);
	}

	@Override
	public void applySnakeMovementChange(SnakeMovementChange movementChange)
	{
		_gameWorld.movementChangesInEffect.add(movementChange);
	}

	@Override
	public LyingItem getLyingItemInTile(WorldPosition position)
	{
		for (LyingItem item : _gameWorld.lyingItems)
		{
			if (item.position.equals(position))
			{
				return item;
			}
		}

		return null;
	}

	@Override
	public void moveSnakeIfItsTime() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		if (_timeService.getNanoStamp() - _gameWorld.lastMoveNanoTimestamp >= _appSettingsService.getAppSettings().snakesMovementNanoInterval)
		{
			moveSnake();
		}

	}

	@Override
	public void moveSnake() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException
	{
		_snakeService.moveSnake(_gameWorld.snake, _gameWorld.movementChangesInEffect);
		_gameWorld.lastMoveNanoTimestamp = _timeService.getNanoStamp();
	}
}
