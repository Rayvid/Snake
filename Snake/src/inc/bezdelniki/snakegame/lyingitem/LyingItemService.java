package inc.bezdelniki.snakegame.lyingitem;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;

public class LyingItemService implements ILyingItemService
{
	@Inject
	public LyingItemService()
	{
	}

	@Override
	public LyingItem createLyingItem(ItemType itemType, WorldPosition position)
	{
		LyingItem lyingItem = new LyingItem();
		lyingItem.itemType = itemType;
		lyingItem.position = position;

		return lyingItem;
	}
}
