package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;

public class TestLyingItems {
	@Test
	public void testIfLyingItemIsCreatedWithSpecifiedParameters() {
		ILyingItemService service = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		
		WorldPosition position = new WorldPosition();
		position.tileX = 1;
		position.tileY = 2;
		
		LyingItem item = service.createLyingItem(ItemType.APPLE, position);
		
		assertTrue(
				item.itemType == ItemType.APPLE &&
				item.position.tileX == 1 &&
				item.position.tileY == 2);
	}
	
	@Test
	public void testIfLyingItemHasBeenCreatedAndCleanupOfLyingItemsWorks() {
		ILyingItemService service = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		service.removeAllLyingItems(world);
		service.createLyingItemSomewhereInTheWorld(world, ItemType.APPLE);
		assertTrue(world.getLyingItems().size() == 1);
		
		service.removeAllLyingItems(world);
		assertTrue(world.getLyingItems().size() == 0);
	}
	
	@Test(expected=LyingItemNowhereToPlaceException.class)
	public void testIfExceptionIsThrownIfTooManyLyingItems() {
		ILyingItemService service = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		GameWorld world = SnakeInjector.getInjectorInstance().getInstance(GameWorld.class);
		
		for (int i = 0; i < world.getGameWorldWidth() * world.getGameWorldHeight() + 1; i++) {
			service.createLyingItemSomewhereInTheWorld(world, ItemType.APPLE);
		}
	}
}
