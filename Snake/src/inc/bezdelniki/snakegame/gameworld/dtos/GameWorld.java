package inc.bezdelniki.snakegame.gameworld.dtos;

import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import java.util.List;

public class GameWorld
{
	public List<LyingItem> lyingItems;
	public Snake snake;
	public List<SnakeMovementChange> movementChangesInEffect;
}
