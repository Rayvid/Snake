package inc.bezdelniki.snake.test;

import org.junit.Test;

import inc.bezdelniki.snake.GameWorld;
import inc.bezdelniki.snake.SnakeInjector;
import inc.bezdelniki.snake.lyingitem.ILyingItemService;
import inc.bezdelniki.snake.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snake.lyingitem.enums.ItemType;
import inc.bezdelniki.snake.lyingitem.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snake.model.dto.WorldPosition;
import junit.framework.TestCase;

public class TestLyingItems extends TestCase {
	public void testIfLyingItemIsCreatedWithSpecifiedParameters() {
		ILyingItemService service = SnakeInjector.getInstance().getInstance(ILyingItemService.class);
		
		WorldPosition position = new WorldPosition();
		position.tileX = 1;
		position.tileY = 2;
		
		LyingItem item = service.CreateLyingItem(ItemType.Apple, position);
		
		assertTrue(
				item.itemType == ItemType.Apple &&
				item.position.tileX == 1 &&
				item.position.tileY == 2);
	}
	
	public void testIfLyingItemHasBeenCreatedAndCleanupOfLyingItemsWorks() {
		ILyingItemService service = SnakeInjector.getInstance().getInstance(ILyingItemService.class);
		GameWorld world = SnakeInjector.getInstance().getInstance(GameWorld.class);
		
		service.RemoveAllLyingItems(world);
		service.CreateLyingItemSomewhereInTheWorld(world, ItemType.Apple);
		assertTrue(world.getLyingItems().size() == 1);
		
		service.RemoveAllLyingItems(world);
		assertTrue(world.getLyingItems().size() == 0);
	}
	
	@Test(expected=LyingItemNowhereToPlaceException.class)
	public void testIfExceptionIsThrownIfTooManyLyingItems() {
		ILyingItemService service = SnakeInjector.getInstance().getInstance(ILyingItemService.class);
		GameWorld world = SnakeInjector.getInstance().getInstance(GameWorld.class);
		
		for (int i = 0; i < world.getGameWorldWidth() * world.getGameWorldHeight() + 1; i++) {
			service.CreateLyingItemSomewhereInTheWorld(world, ItemType.Apple);
		}
		
		assertTrue(false);
	}
}
