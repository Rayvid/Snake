package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.score.ScoreService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestPresentationIsolated
{
	private Injector _testInjectorInstance;
	private IPresentationService _mockedPresentationService;

	private class TestPresentationBindingsConfiguration extends AbstractModule
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
			bind(IScoreService.class).to(ScoreService.class);
			bind(IGameWorldService.class).to(GameWorldService.class);

			bind(IPresentationService.class).toInstance(_mockedPresentationService);
		}
	}

	public TestPresentationIsolated()
	{
		_mockedPresentationService = createNiceMock(IPresentationService.class);
		_testInjectorInstance = Guice.createInjector(new TestPresentationBindingsConfiguration());
	}

	@Test
	public void testIfPresentSnakeCallsPresentHeadMethod()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		Snake snake = snakeService.createSnake();

		SpriteBatch batch = null;
		_mockedPresentationService.presentSnakesHead(batch, snake.headPosition);
		replay(_mockedPresentationService);

		snakeService.presentSnake(snake, new ArrayList<SnakeMovementChange>(), batch);
		verify(_mockedPresentationService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIfPresentSnakeCallsPresentBodyMethod()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		Snake snake = snakeService.createSnake();

		SpriteBatch batch = null;
		_mockedPresentationService.presentSnakesBody(eq(batch), isA(List.class), eq(snake.headPosition));
		replay(_mockedPresentationService);

		snakeService.presentSnake(snake, new ArrayList<SnakeMovementChange>(), batch);
		verify(_mockedPresentationService);
	}

	@Test
	public void testIfPresentLyingItemsCallsPresentLyingItemMethodAccordingLyingItemsCount() throws LyingItemNowhereToPlaceException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		gameWorldService.initGameWorld();

		LyingItem item1 = gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);

		SpriteBatch batch = null;
		_mockedPresentationService.presentLyingItem(batch, item1);
		replay(_mockedPresentationService);

		gameWorldService.presentAllLyingItems(batch);
		verify(_mockedPresentationService);

		LyingItem item2 = gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
		reset(_mockedPresentationService);
		_mockedPresentationService.presentLyingItem(batch, item1);
		_mockedPresentationService.presentLyingItem(batch, item2);
		replay(_mockedPresentationService);

		gameWorldService.presentAllLyingItems(batch);
		verify(_mockedPresentationService);
	}
	
	@Test
	public void testIfPresentScoreCallsPresentScoreMethod()
	{
		IScoreService scoreService = _testInjectorInstance.getInstance(IScoreService.class);
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		gameWorldService.initGameWorld();

		SpriteBatch batch = null;
		int score = 1;
		_mockedPresentationService.presentScore(batch, score);
		replay(_mockedPresentationService);

		scoreService.presentScore(batch, score);
		verify(_mockedPresentationService);
	}
}
