package inc.bezdelniki.snakegame.snake;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.GameWorld;

public interface ISnakeService {
	void createSnake(GameWorld world);
	void growSnake(GameWorld world);
	boolean moveSnake(GameWorld world);
	void removeSnake(GameWorld world);
	void drawSnake(SpriteBatch batch, GameWorld world);
	boolean isTileInSnakesPath(GameWorld world, int tileX, int tileY);
}
