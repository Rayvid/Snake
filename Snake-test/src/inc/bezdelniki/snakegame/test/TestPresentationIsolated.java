package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChangeAction;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestPresentationIsolated
{
	private Injector _testInjectorInstance;
	private IPresentationService _mockedPresentationService;

	public TestPresentationIsolated()
	{
		_mockedPresentationService = createNiceMock(IPresentationService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IPresentationService.class,
						_mockedPresentationService,
						IPresentationService.class));
	}

	@Test
	public void testIfPresentSnakeCallsPresentHeadMethod()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		Snake snake = snakeService.create();

		SpriteBatch batch = null;
		_mockedPresentationService.presentSnakesHead(batch, snake.headPosition);
		replay(_mockedPresentationService);

		snakeService.presentSnake(snake, new ArrayList<SnakeMovementChangeAction>(), batch);
		verify(_mockedPresentationService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIfPresentSnakeCallsPresentBodyMethod()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		Snake snake = snakeService.create();

		SpriteBatch batch = null;
		_mockedPresentationService.presentSnakesBody(eq(batch), isA(List.class), eq(snake.headPosition));
		replay(_mockedPresentationService);

		snakeService.presentSnake(snake, new ArrayList<SnakeMovementChangeAction>(), batch);
		verify(_mockedPresentationService);
	}

	@Test
	public void testIfPresentLyingItemCallsPresentLyingItemMethod()
	{
		ILyingItemService lyingItemService = _testInjectorInstance.getInstance(ILyingItemService.class);

		SpriteBatch batch = null;
		LyingItem item = new LyingItem();
		_mockedPresentationService.presentLyingItem(batch, item);
		replay(_mockedPresentationService);

		lyingItemService.presentLyingItem(batch, item);
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
	
	@Test
	public void testIfPauseControlPresentCallsPresentControl()
	{
		IControlService controlService = _testInjectorInstance.getInstance(IControlService.class);
		
		Control control = controlService.CreatePauseControl();
		
		SpriteBatch batch = null;
		_mockedPresentationService.presentControl(batch, control);
		replay(_mockedPresentationService);
		
		controlService.Present(batch, control);
		verify(_mockedPresentationService);
	}
	
	@Test
	public void testIfScreenResolutionChangeInvokesAdjustToLostContextOrChangedResolution()
	{
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);

		_mockedPresentationService.adjustToChangedResolution();
		replay(_mockedPresentationService);
		
		SystemParams systemParams = systemParamsService.getSystemParams();
		systemParamsService.newResolutionWereSet(systemParams.height, systemParams.width);
		verify(_mockedPresentationService);
	}
}
