package inc.bezdelniki.snakegame.lyingitem;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;

public interface ILyingItemService
{
	LyingItem createLyingItem(ItemType itemType, WorldPosition position);
}
