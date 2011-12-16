package inc.bezdelniki.snakegame.gameworld;

import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public interface IGameWorldService
{
	void initGameWorld();

	GameWorld getGameWorld();

	void applySnakeMovementChange(SnakeMovementChange movementChange);

	void applyLyingItem(LyingItem lyingItem);

	LyingItem getLyingItemInTile(WorldPosition position);

	void moveSnakeIfItsTime() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException;

	void moveSnake() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException;
}
