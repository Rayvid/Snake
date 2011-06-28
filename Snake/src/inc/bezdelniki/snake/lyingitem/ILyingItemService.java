package inc.bezdelniki.snake.lyingitem;

import inc.bezdelniki.snake.GameWorld;
import inc.bezdelniki.snake.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snake.lyingitem.enums.ItemType;
import inc.bezdelniki.snake.model.dto.WorldPosition;

public interface ILyingItemService {
	LyingItem CreateLyingItem(ItemType itemType, WorldPosition position);
	void CreateLyingItemSomewhereInTheWorld(GameWorld world, ItemType itemType);
}
