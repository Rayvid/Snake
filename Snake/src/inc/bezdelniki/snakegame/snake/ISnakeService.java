package inc.bezdelniki.snakegame.snake;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public interface ISnakeService {
	Snake createSnake();
	void growSnake(Snake snake);
	void changeSnakesMovementDirection(Snake snake, Direction newDirection);
	void moveSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect) throws SnakeMovementResultedEndOfGameException;
	void drawSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect, SpriteBatch batch);
	boolean doesTileBelongToSnake(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect, WorldPosition tile, boolean doIncludeHead);
	List<WorldPosition> getSnakesTrail(Snake snake, List<SnakeMovementChange> snakeMovementChangesInEffect);
}
