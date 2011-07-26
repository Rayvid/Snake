package inc.bezdelniki.snakegame.useraction;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public class UserActionService implements IUserActionService {

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

}
