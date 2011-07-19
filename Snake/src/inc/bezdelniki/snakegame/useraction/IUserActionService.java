package inc.bezdelniki.snakegame.useraction;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.model.enums.Direction;

public interface IUserActionService {

	void applyUserActionChangingSnakeMovement(Direction direction, GameWorld world);

}
