package inc.bezdelniki.snake.lyingitem;

import inc.bezdelniki.snake.GameWorld;
import inc.bezdelniki.snake.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snake.lyingitem.enums.ItemType;
import inc.bezdelniki.snake.model.dto.WorldPosition;

public class LyingItemService implements ILyingItemService {

	@Override
	public LyingItem CreateLyingItem(
			ItemType itemType,
			WorldPosition position) {
		LyingItem lyingItem = new LyingItem();
		lyingItem.itemType = itemType;
		lyingItem.position = position;
		
		return lyingItem;
	}

	@Override
	public void CreateLyingItemSomewhereInTheWorld(
			GameWorld world,
			ItemType itemType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveAllLyingItems(GameWorld world) {
		// TODO Auto-generated method stub
		
	}

}