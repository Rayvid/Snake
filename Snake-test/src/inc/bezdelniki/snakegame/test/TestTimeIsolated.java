package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestTimeIsolated
{
	private Injector _testInjectorInstance;
	private ITimeService _mockedTimeService;

	public TestTimeIsolated()
	{
		_mockedTimeService = createMock(ITimeService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						ITimeService.class,
						_mockedTimeService,
						ITimeService.class));
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
	public void testIfMovementOccursWhenEnoughTimePassedAndDoesNotOtherwise() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException
	{
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) 0).anyTimes();
		replay(_mockedTimeService);

		IGameWorldService worldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		worldService.initGameWorld();
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);

		reset(_mockedTimeService);
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) runtimeParams.snakesMovementNanoInterval);
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) runtimeParams.snakesMovementNanoInterval);
		expect(_mockedTimeService.getNanoStamp()).andReturn((long) (runtimeParams.snakesMovementNanoInterval * 1.5));
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
