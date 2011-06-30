package inc.bezdelniki.snakegame.useraction.dtos;

import inc.bezdelniki.snakegame.model.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;

public class SnakeMovementChange {
	public Direction direction;
	public WorldPosition headPositionWhenChangeWereMade;
}
