package inc.bezdelniki.snakegame.useraction;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChangeUserAction;

public class UserActionService implements IUserActionService {

	@Override
	public void applyUserActionChangingSnakeMovement(Direction direction,
			GameWorld world) {
		SnakeMovementChangeUserAction userAction = new SnakeMovementChangeUserAction();
		userAction.direction = direction;
		userAction.headPositionWhenChangeWereMade = world.getSnake().headPosition;
		
		world.getMovementChangesInEffect().add(userAction);
	}

}
