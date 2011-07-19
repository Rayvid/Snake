package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;

public class TestGameWorld {
	@Test
	public void testGameWorldIsCreatedWOExceptions() {
		SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
	}
	
	@Test
	public void testIfLyingObjectIsNeverPlacedOnSnake() {
		ILyingItemService service = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		try
		{
			for (int i = 0; i < world.getGameWorldWidth() * world.getGameWorldHeight() + 1; i++) {
				service.createLyingItemSomewhereInTheWorld(world, ItemType.APPLE);
			}
		}
		catch (LyingItemNowhereToPlaceException ex) { }
		
		fail();
	}
}
