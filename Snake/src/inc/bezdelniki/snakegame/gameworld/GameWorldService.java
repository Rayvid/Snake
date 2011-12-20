package inc.bezdelniki.snakegame.gameworld;

import java.util.ArrayList;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.model.enums.Direction;
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
		if ((movementChange.previousDirection == Direction.LEFT
				|| movementChange.previousDirection == Direction.RIGHT
				&& movementChange.newDirection == Direction.UP
				|| movementChange.newDirection == Direction.DOWN)
				|| (movementChange.previousDirection == Direction.UP
						|| movementChange.previousDirection == Direction.DOWN
						&& movementChange.newDirection == Direction.LEFT
						|| movementChange.newDirection == Direction.RIGHT))
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

	private void eatLyingItemInTile(WorldPosition position)
	{
		for (int i = 0; i < _gameWorld.lyingItems.size(); i++)
		{
			if (_gameWorld.lyingItems.get(i).position.equals(position))
			{
				_gameWorld.lyingItems.remove(i);
				return;
			}
		}
	}

	@Override
	public void moveSnakeIfItsTime() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException, UnknownLyingItemTypeException
	{
		if (_timeService.getNanoStamp() - _gameWorld.lastMoveNanoTimestamp >= _appSettingsService.getAppSettings().snakesMovementNanoInterval)
		{
			moveSnake();
		}

	}

	@Override
	public void moveSnake() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException, UnknownLyingItemTypeException
	{
		_snakeService.moveSnake(_gameWorld.snake, _gameWorld.movementChangesInEffect);
		_gameWorld.lastMoveNanoTimestamp = _timeService.getNanoStamp();

		LyingItem lyingItem = getLyingItemInTile(_gameWorld.snake.headPosition);
		if (lyingItem != null)
		{
			switch (lyingItem.itemType)
			{
			case APPLE:
				eatLyingItemInTile(_gameWorld.snake.headPosition);
				_snakeService.growSnake(_gameWorld.snake);
				break;

			default:
				throw new UnknownLyingItemTypeException();
			}
		}
	}
}
