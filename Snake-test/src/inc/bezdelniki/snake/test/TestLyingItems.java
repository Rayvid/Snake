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
		ILyingItemService service = SnakeInjector.getInstance().getInstance(ILyingItemService.class);
		
		WorldPosition position = new WorldPosition();
		position.tileX = 1;
		position.tileY = 2;
		
		LyingItem item = service.CreateLyingItem(ItemType.APPLE, position);
		
		assertTrue(
				item.itemType == ItemType.APPLE &&
				item.position.tileX == 1 &&
				item.position.tileY == 2);
	}
	
	@Test
	public void testIfLyingItemHasBeenCreatedAndCleanupOfLyingItemsWorks() {
		ILyingItemService service = SnakeInjector.getInstance().getInstance(ILyingItemService.class);
		GameWorld world = SnakeInjector.getInstance().getInstance(GameWorld.class);
		
		service.RemoveAllLyingItems(world);
		service.CreateLyingItemSomewhereInTheWorld(world, ItemType.APPLE);
		assertTrue(world.getLyingItems().size() == 1);
		
		service.RemoveAllLyingItems(world);
		assertTrue(world.getLyingItems().size() == 0);
	}
	
	@Test(expected=LyingItemNowhereToPlaceException.class)
	public void testIfExceptionIsThrownIfTooManyLyingItems() {
		ILyingItemService service = SnakeInjector.getInstance().getInstance(ILyingItemService.class);
		GameWorld world = SnakeInjector.getInstance().getInstance(GameWorld.class);
		
		for (int i = 0; i < world.getGameWorldWidth() * world.getGameWorldHeight() + 1; i++) {
			service.CreateLyingItemSomewhereInTheWorld(world, ItemType.APPLE);
		}
	}
}
