package inc.bezdelniki.snakegame.snake;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;

public interface ISnakeService {
	void createSnake(GameWorld world);
	Direction getSnakeDirection(GameWorld world);
	void growSnake(GameWorld world);
	boolean moveSnake(GameWorld world);
	void removeSnake(GameWorld world);
	void drawSnake(SpriteBatch batch, GameWorld world);
	boolean doesTileBelongToSnake(GameWorld world, WorldPosition tile);
}
