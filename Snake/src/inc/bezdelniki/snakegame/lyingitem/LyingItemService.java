package inc.bezdelniki.snakegame.lyingitem;

import java.util.Random;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;

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
			ItemType itemType) throws LyingItemNowhereToPlaceException {
		Random random = new Random();
		
		int generatedX = random.nextInt(world.getGameWorldWidth());
		int generatedY = random.nextInt(world.getGameWorldHeight());
		int posX = generatedX;
		int posY = generatedY;
		
		while (world.isWorldTileOccupied(posX, posY)) {
			posX++;
			if (posX == world.getGameWorldWidth()) {
				posX = 0;
				
				posY++;
				if (posY == world.getGameWorldHeight()) {
					posY = 0;
				}
			}
			
			if (posX == generatedX && posY == generatedY) {
				throw new LyingItemNowhereToPlaceException();
			}
		}
		
		WorldPosition position = new WorldPosition();
		position.tileX = posX;
		position.tileY = posY;
		world.getLyingItems().add(CreateLyingItem(itemType, position));
	}

	@Override
	public void RemoveAllLyingItems(GameWorld world) {
		world.getLyingItems().clear();
	}
}
