package inc.bezdelniki.snakegame.useraction;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public class UserActionService implements IUserActionService
{
	IDeviceService _deviceService;

	@Inject
	public UserActionService(IDeviceService deviceService)
	{
		_deviceService = deviceService;
	}

	@Override
	public SnakeMovementChange createSnakeMovementChange(Snake snake, Direction direction)
	{
		SnakeMovementChange userAction = new SnakeMovementChange();
		userAction.previousDirection = snake.direction;
		userAction.newDirection = direction;
		userAction.headPositionWhenChangeWereMade = (WorldPosition) snake.headPosition.clone();

		return userAction;
	}

	@Override
	public SnakeMovementChange createSnakeMovementChangeAccordingTouch(Snake snake, TouchCoords touchCoords)
	{
		WorldPosition touchPosition = _deviceService.DeviceCoordsToWorldPosition(_deviceService.TouchCoordsToDeviceCoords(touchCoords));

		if (Math.abs(touchPosition.tileX - snake.headPosition.tileX) != 0 || Math.abs(touchPosition.tileY - snake.headPosition.tileY) != 0)
		{
			if (snake.direction == Direction.LEFT || snake.direction == Direction.RIGHT)
			{
				if (Math.abs(touchPosition.tileY - snake.headPosition.tileY) != 0)
				{
					return generateVerticalChange(snake, touchPosition);
				}
			}
			else if (Math.abs(touchPosition.tileX - snake.headPosition.tileX) != 0)
			{
				return generateHorizontalChange(snake, touchPosition);
			}
		}

		return null;
	}

	private SnakeMovementChange generateHorizontalChange(Snake snake, WorldPosition touchPosition)
	{
		if (touchPosition.tileX - snake.headPosition.tileX > 0)
		{
			return createSnakeMovementChange(snake, Direction.RIGHT);
		}
		else
		{
			return createSnakeMovementChange(snake, Direction.LEFT);
		}
	}

	private SnakeMovementChange generateVerticalChange(Snake snake, WorldPosition touchPosition)
	{
		if (touchPosition.tileY - snake.headPosition.tileY > 0)
		{
			return createSnakeMovementChange(snake, Direction.DOWN);
		}
		else
		{
			return createSnakeMovementChange(snake, Direction.UP);
		}
	}
}
