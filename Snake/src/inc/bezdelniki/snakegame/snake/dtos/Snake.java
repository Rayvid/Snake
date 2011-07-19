package inc.bezdelniki.snakegame.snake.dtos;

import inc.bezdelniki.snakegame.model.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;

public class Snake {
	public WorldPosition headPosition;
	public int currLength;
	public int newLength;
	public Direction direction;
}
