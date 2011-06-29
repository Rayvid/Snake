package inc.bezdelniki.snakegame.lyingitem;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;

public interface ILyingItemService {
	LyingItem CreateLyingItem(ItemType itemType, WorldPosition position);
	void CreateLyingItemSomewhereInTheWorld(GameWorld world, ItemType itemType);
	void RemoveAllLyingItems(GameWorld world);
}
