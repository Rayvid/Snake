package inc.bezdelniki.snake.test;

import org.junit.Test;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.SnakeInjector;

public class TestGameWorld {
	@Test
	public void testGameWorldIsCreatedWOExceptions() {
		SnakeInjector.getInstance().getInstance(GameWorld.class);
	}
}
