package inc.bezdelniki.snakegame.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.IUserActionService;

public class TestGameWorld
{
	@Test
	public void testIfSnakeEatsLyingApplesAndGrowsAfterwards() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
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

	@Test
	public void testIfLyingItemIsNeverPlacedOnSnakeOrSnakesPath() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		IAppSettingsService appSettingsService = SnakeInjector.getInjectorInstance().getInstance(IAppSettingsService.class);
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		IUserActionService userActionService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);

		gameWorldService.initGameWorld();
		AppSettings appSettings = appSettingsService.getAppSettings();
		GameWorld gameWorld = gameWorldService.getGameWorld();
		Snake snake = gameWorld.snake;
		
		gameWorldService.applySnakeMovementChange(userActionService.createSnakeMovementChange(snake, Direction.RIGHT));
		gameWorldService.moveSnake();

		try
		{
			for (int i = 0; i < appSettings.tilesVertically * appSettings.tilesHorizontally + 1; i++)
			{
				LyingItem lyingItem = gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
				List<WorldPosition> trail = snakeService.getSnakesTrail(snake, gameWorld.movementChangesInEffect);
				
				for (WorldPosition piece : trail)
				{
					if (piece.equals(lyingItem.position))
					{
						fail("Item created on the snake");
					}
				}
				
				if (snake.headPosition.tileY == lyingItem.position.tileY && snake.headPosition.tileX < lyingItem.position.tileX)
				{
					fail("Item created on the snakes path");
				}
			}
		}
		catch (LyingItemNowhereToPlaceException e)
		{
			// Expected
		}
	}

	@Test(expected = LyingItemNowhereToPlaceException.class)
	public void testIfExceptionIsThrownIfTooManyLyingItems() throws LyingItemNowhereToPlaceException
	{
		IAppSettingsService appSettingsService = SnakeInjector.getInjectorInstance().getInstance(IAppSettingsService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);

		gameWorldService.initGameWorld();
		AppSettings appSettings = appSettingsService.getAppSettings();

		for (int i = 0; i < appSettings.tilesVertically * appSettings.tilesHorizontally + 1; i++)
		{
			gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
		}
	}
}
