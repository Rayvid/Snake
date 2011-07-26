package inc.bezdelniki.snakegame.lyingitem;

import java.util.List;
import java.util.Random;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.snake.ISnakeService;

public class LyingItemService implements ILyingItemService {
	
	private IAppSettingsService _appSettingsService;
	private ISnakeService _snakeService;

	@Inject
	LyingItemService(
		IAppSettingsService appSettingsService,
		ISnakeService snakeService) {
		
		_appSettingsService = appSettingsService;
		_snakeService = snakeService;
	}
	
	@Override
	public LyingItem createLyingItem(
			ItemType itemType,
			WorldPosition position) {
		LyingItem lyingItem = new LyingItem();
		lyingItem.itemType = itemType;
		lyingItem.position = position;
		
		return lyingItem;
	}
	
	private boolean isWorldTileOccupied(
		WorldPosition position, 
		GameWorld gameWorld)
	{
		for (LyingItem item : gameWorld.lyingItems) {
			if (item.position.equals(position)) {
				return true;
			}
		}
		
		List<WorldPosition> snakesTrail = _snakeService.getSnakesTrail(gameWorld.snake, gameWorld.movementChangesInEffect);
		for (WorldPosition snakePiece : snakesTrail) {
			if (snakePiece.equals(position)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public LyingItem createLyingItemSomewhere(
			ItemType itemType,
			GameWorld gameWorld) throws LyingItemNowhereToPlaceException {
		
		Random random = new Random();
		
		AppSettings appSettings = _appSettingsService.getAppSettings();
		
		int generatedX = random.nextInt(appSettings.tilesHorizontally);
		int generatedY = random.nextInt(appSettings.tilesVertically);

		WorldPosition position = new WorldPosition();
		position.tileX = generatedX;
		position.tileY = generatedY;
		
		while (isWorldTileOccupied(position, gameWorld)) {
			position.tileX++;
			if (position.tileX == appSettings.tilesHorizontally) {
				position.tileX = 0;
				
				position.tileY++;
				if (position.tileY == appSettings.tilesVertically) {
					position.tileY = 0;
				}
			}
			
			if (position.tileX == generatedX && position.tileY == generatedY) {
				throw new LyingItemNowhereToPlaceException();
			}
		}
		
		return createLyingItem(itemType, position);
	}
}
