package inc.bezdelniki.snakegame.gameworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public class GameWorldService implements IGameWorldService
{
	private IAppSettingsService _appSettingsService;
	private ISnakeService _snakeService;
	private ILyingItemService _lyingItemsService;
	private ITimeService _timeService;
	private IPresentationService _presentationService;
	private IRuntimeParamsService _runtimeParamsService;
	private IScoreService _scoreService;
	private GameWorld _gameWorld = null;
	private RuntimeParams _runtimeParams = null;
	private int _score;

	@Inject
	public GameWorldService(
			IAppSettingsService appSettingsService,
			ISnakeService snakeService,
			ITimeService timeService,
			ILyingItemService lyingItemsService,
			IPresentationService presentationService,
			IRuntimeParamsService runtimeParamsService,
			IScoreService scoreService,
			RuntimeParams runtimeParams)
	{
		_appSettingsService = appSettingsService;
		_snakeService = snakeService;
		_timeService = timeService;
		_lyingItemsService = lyingItemsService;
		_presentationService = presentationService;
		_runtimeParamsService = runtimeParamsService;
		_scoreService = scoreService;
		
		_runtimeParams = runtimeParams;
	}

	@Override
	public void initGameWorld()
	{
		_gameWorld = new GameWorld();

		_gameWorld.lyingItems = new ArrayList<LyingItem>();
		_gameWorld.snake = _snakeService.createSnake();
		_gameWorld.movementChangesInEffect = new ArrayList<SnakeMovementChange>();
		_gameWorld.lastSnakesMovementNanoTimestamp = _timeService.getNanoStamp();
		
		_runtimeParamsService.initParamsForNewGame(_runtimeParams);
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
	public LyingItem createAndApplyLyingItemSomewhere(ItemType itemType) throws LyingItemNowhereToPlaceException
	{
		Random random = new Random();

		AppSettings appSettings = _appSettingsService.getAppSettings();

		int generatedX = random.nextInt(appSettings.tilesHorizontally);
		int generatedY = random.nextInt(appSettings.tilesVertically);

		WorldPosition position = new WorldPosition(generatedX, generatedY);

		while (isWorldTileOccupied(position))
		{
			position.tileX++;
			if (position.tileX == appSettings.tilesHorizontally)
			{
				position.tileX = 0;

				position.tileY++;
				if (position.tileY == appSettings.tilesVertically)
				{
					position.tileY = 0;
				}
			}

			if (position.tileX == generatedX && position.tileY == generatedY)
			{
				throw new LyingItemNowhereToPlaceException();
			}
		}

		LyingItem result = _lyingItemsService.createLyingItem(itemType, position);
		applyLyingItem(result);

		return result;
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
				_score += _scoreService.getScore4Item(_gameWorld.lyingItems.get(i));
				_gameWorld.lyingItems.remove(i);
				return;
			}
		}
	}

	@Override
	public void moveSnakeIfItsTime() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		if (_timeService.getNanoStamp() - _gameWorld.lastSnakesMovementNanoTimestamp >= _runtimeParams.snakesMovementNanoInterval)
		{
			moveSnake();
		}

	}

	@Override
	public void moveSnake() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		_snakeService.moveSnake(_gameWorld.snake, _gameWorld.movementChangesInEffect);
		_gameWorld.lastSnakesMovementNanoTimestamp = _timeService.getNanoStamp();

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

	private boolean isWorldTileOccupied(WorldPosition position)
	{
		for (LyingItem item : _gameWorld.lyingItems)
		{
			if (item.position.equals(position))
			{
				return true;
			}
		}

		List<WorldPosition> snakesTrail = _snakeService.getSnakesTrail(_gameWorld.snake, _gameWorld.movementChangesInEffect);
		for (WorldPosition snakePiece : snakesTrail)
		{
			if (snakePiece.equals(position))
			{
				return true;
			}
		}

		switch (_gameWorld.snake.direction)
		{
		case RIGHT:
			if (_gameWorld.snake.headPosition.tileY == position.tileY && _gameWorld.snake.headPosition.tileX < position.tileX)
			{
				return true;
			}
			break;

		case LEFT:
			if (_gameWorld.snake.headPosition.tileY == position.tileY && _gameWorld.snake.headPosition.tileX > position.tileX)
			{
				return true;
			}
			break;

		case UP:
			if (_gameWorld.snake.headPosition.tileX == position.tileX && _gameWorld.snake.headPosition.tileY > position.tileY)
			{
				return true;
			}
			break;

		case DOWN:
			if (_gameWorld.snake.headPosition.tileX == position.tileX && _gameWorld.snake.headPosition.tileY < position.tileY)
			{
				return true;
			}
			break;
		}

		return false;
	}

	@Override
	public void presentAllLyingItems(SpriteBatch batch)
	{
		for (LyingItem item : _gameWorld.lyingItems)
		{
			_presentationService.presentLyingItem(batch, item);
		}
	}

	@Override
	public int getScore()
	{
		return _score;
	}
}
