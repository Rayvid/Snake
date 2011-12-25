package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestTimeIsolated
{
	private Injector _testInjectorInstance;
	private ITimeService _mockedTimeService;

	private class TestTimeBindingsConfiguration extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
			bind(IRuntimeParamsService.class).to(RuntimeParamsService.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(ILyingItemService.class).to(LyingItemService.class);
			bind(IGameWorldService.class).to(GameWorldService.class).in(Singleton.class);

			bind(ITimeService.class).toInstance(_mockedTimeService);
		}
	}

	public TestTimeIsolated()
	{
		_mockedTimeService = createMock(ITimeService.class);
		_testInjectorInstance = Guice.createInjector(new TestTimeBindingsConfiguration());
	}

	@Test
	@Ignore("Not deterministic")
	public void testIfTimerIsAccurateEnough() throws InterruptedException
	{
		ITimeService service = new TimeService();

		long[] nanoStamps = new long[10];
		for (int i = 0; i < 10; i++)
		{
			Thread.sleep(1);
			nanoStamps[i] = service.getNanoStamp();
		}

		long avgDiff = 0;
		for (int i = 3; i < 10; i++)
		{
			avgDiff += nanoStamps[i] - nanoStamps[i - 1];
		}
		avgDiff /= 7;

		assertTrue(avgDiff > 900000 && avgDiff < 1100000);
	}

	@Test
	public void testIfMovementOccursWhenEnoughTimePassedAndDoesNotOtherwise() throws CloneNotSupportedException, SnakeMovementResultedEndOfGameException,
			UnknownLyingItemTypeException
	{
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) 0).anyTimes();
		replay(_mockedTimeService);

		IGameWorldService worldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		worldService.initGameWorld();

		reset(_mockedTimeService);
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) worldService.getRuntimeParams().snakesMovementNanoInterval);
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) worldService.getRuntimeParams().snakesMovementNanoInterval);
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) (worldService.getRuntimeParams().snakesMovementNanoInterval * 1.5));
		replay(_mockedTimeService);

		WorldPosition headPosition = (WorldPosition) worldService.getGameWorld().snake.headPosition.clone();

		worldService.moveSnakeIfItsTime();
		assertTrue(!headPosition.equals(worldService.getGameWorld().snake.headPosition));
		headPosition = (WorldPosition) worldService.getGameWorld().snake.headPosition.clone();
		worldService.moveSnakeIfItsTime();
		assertTrue(headPosition.equals(worldService.getGameWorld().snake.headPosition));
		verify(_mockedTimeService);
	}
}
