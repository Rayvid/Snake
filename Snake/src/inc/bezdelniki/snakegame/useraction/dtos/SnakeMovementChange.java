package inc.bezdelniki.snakegame.useraction.dtos;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;

public class SnakeMovementChange
{
	public Direction previousDirection;
	public Direction newDirection;
	public WorldPosition headPositionWhenChangeWereMade;
}
