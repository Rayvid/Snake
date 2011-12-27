package inc.bezdelniki.snakegame.snake;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.LayoutParams;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public class SnakeService implements ISnakeService
{
	private IAppSettingsService _appSettingsService;
	private IPresentationService _presentationService;

	@Inject
	public SnakeService(IAppSettingsService appSettingsService, IPresentationService presentationService)
	{
		_appSettingsService = appSettingsService;
		_presentationService = presentationService;
	}

	@Override
	public Snake createSnake()
	{
		AppSettings settings = _appSettingsService.getAppSettings();

		WorldPosition position = new WorldPosition(settings.initialHeadPositionX, settings.initialHeadPositionY);

		Snake snake = new Snake();
		snake.currLength = 1;
		snake.headPosition = position;
		snake.newLength = settings.initialSnakeLength;
		snake.direction = settings.initialDirection;

		return snake;
	}

	@Override
	public void growSnake(Snake snake)
	{
		AppSettings settings = _appSettingsService.getAppSettings();

		snake.newLength += settings.growSnakeBy;
	}

	@Override
	public void moveSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect) throws SnakeMovementResultedEndOfGameException
	{
		AppSettings settings = _appSettingsService.getAppSettings();

		if (snakeMovementChangesInEffect.size() > 0
				&& snakeMovementChangesInEffect.get(snakeMovementChangesInEffect.size() - 1).headPositionWhenChangeWereMade.equals(snake.headPosition))
		{
			snake.direction = snakeMovementChangesInEffect.get(snakeMovementChangesInEffect.size() - 1).newDirection;
		}

		Snake oldSnake = (Snake) snake.clone();
		switch (snake.direction)
		{
		case RIGHT:
			snake.headPosition.tileX++;
			break;

		case LEFT:
			snake.headPosition.tileX--;
			break;

		case UP:
			snake.headPosition.tileY--;
			break;

		case DOWN:
			snake.headPosition.tileY++;
			break;
		}

		if (snake.currLength < snake.newLength)
		{
			snake.currLength++;
		}

		if (snake.headPosition.tileX < 0 || snake.headPosition.tileY < 0 || snake.headPosition.tileX >= settings.tilesHorizontally
				|| snake.headPosition.tileY >= settings.tilesVertically
				|| doesTileBelongToSnake(snake, snakeMovementChangesInEffect, snake.headPosition, false))
		{
			snake.headPosition = oldSnake.headPosition;
			snake.currLength = oldSnake.currLength;
			throw new SnakeMovementResultedEndOfGameException();
		}
	}

	@Override
	public void presentSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect, SpriteBatch batch, LayoutParams layoutParams)
	{
		_presentationService.presentSnakesHead(batch, snake.headPosition, layoutParams);
		_presentationService.presentSnakesBody(batch, getSnakesTrail(snake, snakeMovementChangesInEffect), snake.headPosition, layoutParams);
	}

	@Override
	public boolean doesTileBelongToSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect, WorldPosition tile, boolean doIncludeHead)
	{

		List<WorldPosition> snakesTrail = getSnakesTrail(snake, snakeMovementChangesInEffect);
		for (int i = (doIncludeHead) ? 0 : 1; i < snakesTrail.size(); i++)
		{
			if (snakesTrail.get(i).equals(tile))
			{
				return true;
			}
		}

		return false;
	}

	private WorldPosition traverseBackTroughSnakesTrail(WorldPosition position, Direction snakesDirection)
	{
		WorldPosition newPosition = null;
		newPosition = (WorldPosition) position.clone();

		switch (snakesDirection)
		{
		case LEFT:
			newPosition.tileX++;
			break;

		case RIGHT:
			newPosition.tileX--;
			break;

		case UP:
			newPosition.tileY++;
			break;

		case DOWN:
			newPosition.tileY--;
			break;
		}

		return newPosition;
	}

	@Override
	public List<WorldPosition> getSnakesTrail(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect)
	{
		List<WorldPosition> snakesTrailList = new ArrayList<WorldPosition>();

		WorldPosition position;
		position = (WorldPosition) snake.headPosition.clone();

		Direction direction = snake.direction;
		int currentMovementChange = snakeMovementChangesInEffect.size() - 1;
		while (currentMovementChange >= 0 && snakeMovementChangesInEffect.get(currentMovementChange).headPositionWhenChangeWereMade.equals(position))
		{
			direction = snakeMovementChangesInEffect.get(currentMovementChange).previousDirection;
			currentMovementChange--;
		}

		snakesTrailList.add(position);
		for (int i = 1; i < snake.currLength; i++)
		{
			position = traverseBackTroughSnakesTrail(position, direction);
			snakesTrailList.add(position);

			if (i + 1 != snake.currLength && currentMovementChange >= 0
					&& snakeMovementChangesInEffect.get(currentMovementChange).headPositionWhenChangeWereMade.equals(position))
			{
				do
				{
					direction = snakeMovementChangesInEffect.get(currentMovementChange).previousDirection;
					currentMovementChange--;
				}
				while (currentMovementChange >= 0 && snakeMovementChangesInEffect.get(currentMovementChange).headPositionWhenChangeWereMade.equals(position));
			}
		}

		// Garbage collection :)
		for (int i = 0; i <= currentMovementChange; i++)
		{
			if (!snakeMovementChangesInEffect.get(0).headPositionWhenChangeWereMade.equals(position))
			{
				snakeMovementChangesInEffect.remove(0);
			}
		}
		//

		return snakesTrailList;
	}
}
