package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class ScoreTestIsolated
{
	private Injector _testInjectorInstance;
	private IScoreService _mockedScoreService;

	private class TestLyingItemsBindingsConfiguration extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IRuntimeParamsService.class).to(RuntimeParamsService.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(ITimeService.class).to(TimeService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(ILyingItemService.class).to(LyingItemService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(IGameWorldService.class).to(GameWorldService.class);

			bind(IScoreService.class).toInstance(_mockedScoreService);
		}
	}

	public ScoreTestIsolated()
	{
		_mockedScoreService = createMock(IScoreService.class);
		_testInjectorInstance = Guice.createInjector(new TestLyingItemsBindingsConfiguration());
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
