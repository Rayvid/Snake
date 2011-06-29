package inc.bezdelniki.snakegame.snake;

import inc.bezdelniki.snakegame.GameWorld;

public interface ISnakeService {
	void CreateSnake(GameWorld world);
	void GrowSnake(GameWorld world);
	void RemoveSnake(GameWorld world);
}
