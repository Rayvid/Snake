package inc.bezdelniki.snakegame.snake;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public interface ISnakeService {
	Snake createSnake();
	void growSnake(Snake snake);
	boolean moveSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect);
	void drawSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect, SpriteBatch batch);
	boolean doesTileBelongToSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect, WorldPosition tile, boolean doIncludeHead);
	List<WorldPosition> getSnakesTrail(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect);
}
