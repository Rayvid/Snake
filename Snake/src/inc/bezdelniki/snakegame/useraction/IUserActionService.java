package inc.bezdelniki.snakegame.useraction;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public interface IUserActionService {

	SnakeMovementChange createSnakeMovementChange(Snake snake, Direction direction);
	SnakeMovementChange createSnakeMovementChangeAccordingTouch(Snake snake, DeviceCoords getTouchCoords);

}
