package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.useraction.IUserActionService;

public class TestLyingItems {
	@Test
	public void testIfLyingItemIsCreatedWithSpecifiedParameters() {
		ILyingItemService service = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		
		WorldPosition position = new WorldPosition(1, 2);
		LyingItem item = service.createLyingItem(ItemType.APPLE, position);
		
		assertTrue(
				item.itemType == ItemType.APPLE &&
				item.position.tileX == 1 &&
				item.position.tileY == 2);
	}
	
	@Test
	public void testIfLyingItemHasBeenCreatedSomewhere() throws LyingItemNowhereToPlaceException {
		ILyingItemService lyingItemsService = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		
		gameWorldService.initGameWorld();
		
		LyingItem item = lyingItemsService.createLyingItemSomewhere(ItemType.APPLE, gameWorldService.getGameWorld());
		assertTrue(item != null);
	}
	
	@Test
	public void testIfLyingItemIsNeverCreatedInSnakesPath() {
		ILyingItemService lyingItemsService = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		IAppSettingsService appSettingsService = SnakeInjector.getInjectorInstance().getInstance(IAppSettingsService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		
		gameWorldService.initGameWorld();
		AppSettings appSettings = appSettingsService.getAppSettings();
		GameWorld gameWorld = gameWorldService.getGameWorld();
		Snake snake = gameWorld.snake;
		
		fail(); // TODO check if no item in path
		
		try
		{
			for (int i = 0; i < appSettings.tilesVertically * appSettings.tilesHorizontally + 1; i++) {
				gameWorldService.applyLyingItem(lyingItemsService.createLyingItemSomewhere(ItemType.APPLE, gameWorldService.getGameWorld()));
			}
		}
		catch (LyingItemNowhereToPlaceException e)
		{
			// Expected
		}
	}
	
	@Test(expected=LyingItemNowhereToPlaceException.class)
	public void testIfExceptionIsThrownIfTooManyLyingItems() throws LyingItemNowhereToPlaceException {
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
