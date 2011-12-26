package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;
import org.junit.Test;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public class TestSystemParameters
{
	@Test
	public void testResolutionChangesPersistsBetweenResolves()
	{
		SystemParams systemParameters = SnakeInjector.getInjectorInstance().getInstance(ISystemParamsService.class).getSystemParams();
		int oldWidth = systemParameters.width;
		int oldHeight = systemParameters.height;

		SnakeInjector.getInjectorInstance().getInstance(ISystemParamsService.class).newResolutionWereSet(oldWidth - 1, oldHeight - 1);

		systemParameters = SnakeInjector.getInjectorInstance().getInstance(ISystemParamsService.class).getSystemParams();
		assertTrue(systemParameters.width == oldWidth - 1 && systemParameters.height == oldHeight - 1);
	}
}
