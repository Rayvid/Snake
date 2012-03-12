package inc.bezdelniki.snakegame.snake;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChangeAction;

public interface ISnakeService
{
	Snake create();
	void growSnake(Snake snake);
	void moveSnake(Snake snake, List<SnakeMovementChangeAction> snakeMovementChangesInEffect) throws SnakeMovementResultedEndOfGameException;
	void presentSnake(Snake snake, List<SnakeMovementChangeAction> snakeMovementChangesInEffect, SpriteBatch batch);
	boolean doesTileBelongToSnake(Snake snake, List<SnakeMovementChangeAction> snakeMovementChangesInEffect, WorldPosition tile, boolean doIncludeHead);
	List<WorldPosition> getSnakesTrail(Snake snake, List<SnakeMovementChangeAction> snakeMovementChangesInEffect);
}
