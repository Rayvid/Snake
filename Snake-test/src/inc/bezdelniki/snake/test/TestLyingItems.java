package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;

public class TestLyingItems {
	@Test
	public void testIfLyingItemIsCreatedWithSpecifiedParameters() {
		ILyingItemService service = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		
		WorldPosition position = new WorldPosition();
		position.tileX = 1;
		position.tileY = 2;
		
		LyingItem item = service.createLyingItem(ItemType.APPLE, position);
		
		assertTrue(
				item.itemType == ItemType.APPLE &&
				item.position.tileX == 1 &&
				item.position.tileY == 2);
	}
	
	@Test
	public void testIfLyingItemHasBeenCreatedSomewhere() {
		ILyingItemService lyingItemsService = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		
		LyingItem item = lyingItemsService.createLyingItemSomewhere(ItemType.APPLE, gameWorldService.getGameWorld());
		assertTrue(item != null);
	}
	
	@Test(expected=LyingItemNowhereToPlaceException.class)
	public void testIfExceptionIsThrownIfTooManyLyingItems() {
		ILyingItemService lyingItemsService = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		IAppSettingsService appSettingsService = SnakeInjector.getInjectorInstance().getInstance(IAppSettingsService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		AppSettings appSettings = appSettingsService.getAppSettings();
		
		for (int i = 0; i < appSettings.tilesVertically * appSettings.tilesHorizontally + 1; i++) {
			gameWorldService.applyLyingItem(lyingItemsService.createLyingItemSomewhere(ItemType.APPLE, gameWorldService.getGameWorld()));
		}
	}
}
