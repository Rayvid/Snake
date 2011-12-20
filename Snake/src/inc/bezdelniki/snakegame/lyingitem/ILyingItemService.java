package inc.bezdelniki.snakegame.lyingitem;

import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;

public interface ILyingItemService
{
	LyingItem createLyingItem(ItemType itemType, WorldPosition position);
	LyingItem createLyingItemSomewhere(ItemType itemType, GameWorld gameWorld) throws LyingItemNowhereToPlaceException;
}
