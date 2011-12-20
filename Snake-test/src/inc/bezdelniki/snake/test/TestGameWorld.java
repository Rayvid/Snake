package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.lyingitem.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;

public class TestGameWorld
{
	@Test
	public void testIfLyingObjectIsNeverPlacedOnSnake()
	{
		ILyingItemService lyingItemService = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IAppSettingsService appSettingsService = SnakeInjector.getInjectorInstance().getInstance(IAppSettingsService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);

		gameWorldService.initGameWorld();
		Snake snake = gameWorldService.getGameWorld().snake;
		AppSettings appSettings = appSettingsService.getAppSettings();

		try
		{
			for (int i = 0; i < appSettings.tilesVertically * appSettings.tilesHorizontally + 1; i++)
			{
				gameWorldService.applyLyingItem(lyingItemService.createLyingItemSomewhere(ItemType.APPLE, gameWorldService.getGameWorld()));
			}

			fail();
		}
		catch (LyingItemNowhereToPlaceException ex)
		{
		}

		List<WorldPosition> snakesTrail = snakeService.getSnakesTrail(snake, gameWorldService.getGameWorld().movementChangesInEffect);
		for (WorldPosition position : snakesTrail)
		{
			assertTrue(gameWorldService.getLyingItemInTile(position) == null);
		}
	}

	@Test
	public void testIfSnakeEatsLyingApplesAndGrowsAfterwards() throws SnakeMovementResultedEndOfGameException, CloneNotSupportedException, UnknownLyingItemTypeException
	{
		ILyingItemService lyingItemService = SnakeInjector.getInjectorInstance().getInstance(ILyingItemService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);

		gameWorldService.initGameWorld();
		GameWorld gameWorld = gameWorldService.getGameWorld();
		Snake snake = gameWorld.snake;

		WorldPosition applePosition = (WorldPosition) snake.headPosition.clone();
		applePosition.tileX++;
		gameWorld.lyingItems.add(lyingItemService.createLyingItem(ItemType.APPLE, applePosition));

		int oldSnakeLength = snake.newLength;
		gameWorldService.moveSnake();
		assertTrue(gameWorld.lyingItems.size() == 0);
		assertTrue(snake.newLength > oldSnakeLength);
	}
}
