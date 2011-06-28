package inc.bezdelniki.snake.test;

import inc.bezdelniki.snake.SnakeInjector;
import inc.bezdelniki.snake.lyingitem.ILyingItemService;
import inc.bezdelniki.snake.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snake.lyingitem.enums.ItemType;
import inc.bezdelniki.snake.model.dto.WorldPosition;
import junit.framework.TestCase;

public class TestLyingItems extends TestCase {
	public void testIfLyingItemIsCreatedWithSpecifiedParameters()
	{
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
}
