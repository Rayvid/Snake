package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ScoreTestIsolated
{
	private Injector _testInjectorInstance;
	private IScoreService _mockedScoreService;

	public ScoreTestIsolated()
	{
		_mockedScoreService = createMock(IScoreService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IScoreService.class,
						_mockedScoreService,
						IScoreService.class));
	}
	
	@Test
	public void testIfGetScore4ItemIsCalledAndScoreIsAddedAfterSnakeEatsApple() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		ILyingItemService lyingItemService = _testInjectorInstance.getInstance(ILyingItemService.class);
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		
		expect(_mockedScoreService.getScore4Item(isA(LyingItem.class))).andReturn(1);
		replay(_mockedScoreService);

		gameWorldService.initGameWorld();
		GameWorld gameWorld = gameWorldService.getGameWorld();
		Snake snake = gameWorld.snake;

		WorldPosition applePosition = (WorldPosition) snake.headPosition.clone();
		applePosition.tileX++;
		gameWorld.lyingItems.add(lyingItemService.createLyingItem(ItemType.APPLE, applePosition));

		int oldScore = gameWorldService.getScore();
		gameWorldService.moveSnake();
		assertTrue(gameWorldService.getScore() > oldScore);
		verify();
	}
}
