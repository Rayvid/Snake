package inc.bezdelniki.snakegame.lyingitem;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;

public interface ILyingItemService {
	LyingItem createLyingItem(ItemType itemType, WorldPosition position);
	void createLyingItemSomewhereInTheWorld(GameWorld world, ItemType itemType);
	void removeAllLyingItems(GameWorld world);
}
