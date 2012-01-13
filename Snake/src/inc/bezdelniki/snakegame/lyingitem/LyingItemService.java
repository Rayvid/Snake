package inc.bezdelniki.snakegame.lyingitem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.presentation.IPresentationService;

public class LyingItemService implements ILyingItemService
{
	private IPresentationService _presentationService;
	
	@Inject
	public LyingItemService(IPresentationService presentationService)
	{
		_presentationService = presentationService;
	}

	@Override
	public LyingItem createLyingItem(ItemType itemType, WorldPosition position)
	{
		LyingItem lyingItem = new LyingItem();
		lyingItem.itemType = itemType;
		lyingItem.position = position;

		return lyingItem;
	}

	@Override
	public void presentLyingItem(SpriteBatch batch, LyingItem item)
	{
		_presentationService.presentLyingItem(batch, item);
	}
}
