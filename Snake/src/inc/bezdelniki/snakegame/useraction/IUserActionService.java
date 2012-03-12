package inc.bezdelniki.snakegame.useraction;

import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.PauseAction;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChangeAction;

public interface IUserActionService
{
	SnakeMovementChangeAction createSnakeMovementChange(Snake snake, Direction direction);
	SnakeMovementChangeAction createSnakeMovementChangeAccordingTouch(Snake snake, TouchCoords touchCoords);
	PauseAction createPauseAction();
}
