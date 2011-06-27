package inc.bezdelniki.snake.test;

import junit.framework.*;

import inc.bezdelniki.snake.GameWorld;
import inc.bezdelniki.snake.SnakeInjector;

public class TestGameWorld extends TestCase {
	public void setUp() 
	{
		SnakeInjector.configure();
	}
	
	public void testGameWorldIsCreatedWOExceptions()
	{
		SnakeInjector.getInstance().getInstance(GameWorld.class);
	}
}
