package inc.bezdelniki.snakegame.useraction;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public class UserActionService implements IUserActionService {
	IDeviceService _deviceService;
	
	@Inject
	public UserActionService(IDeviceService deviceService) {
		_deviceService = deviceService;
	}

	@Override
	public SnakeMovementChange createSnakeMovementChange(Snake snake, Direction direction) {
		SnakeMovementChange userAction = new SnakeMovementChange();
		userAction.previousDirection = snake.direction;
		userAction.newDirection = direction;
		try {
			userAction.headPositionWhenChangeWereMade = (WorldPosition)snake.headPosition.clone();
		} catch (CloneNotSupportedException e) {
			userAction.headPositionWhenChangeWereMade = null;
		}
		
		return userAction;
	}

	@Override
	public SnakeMovementChange createSnakeMovementChangeAccordingTouch(
			Snake snake,
			DeviceCoords touchCoords) {
		WorldPosition touchPosition = _deviceService.DeviceCoordsToWorldPosition(touchCoords);
		
		if (Math.abs(touchPosition.tileY - snake.headPosition.tileY) > Math.abs(touchPosition.tileX - snake.headPosition.tileX)) {
			if (touchPosition.tileY - snake.headPosition.tileY > 0) {
				return createSnakeMovementChange(snake, Direction.DOWN);
			} else {
				return createSnakeMovementChange(snake, Direction.UP);
			}
		}
		else if (Math.abs(touchPosition.tileX - snake.headPosition.tileX) != 0)
		{
			if (touchPosition.tileX - snake.headPosition.tileX > 0) {
				return createSnakeMovementChange(snake, Direction.RIGHT);
			} else {
				return createSnakeMovementChange(snake, Direction.LEFT);
			}
		}
		
		return null;
	}

}
