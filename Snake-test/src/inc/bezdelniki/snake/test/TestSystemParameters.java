package inc.bezdelniki.snake.test;

import static org.junit.Assert.*;
import org.junit.Test;

import inc.bezdelniki.snakegame.SnakeInjector;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

public class TestSystemParameters {
	@Test
	public void testResolutionChangesPersistsBetweenResolves() {
		SystemParameters systemParameters = SnakeInjector.getInstance().getInstance(ISystemParametersService.class).GetSystemParameters();
		int oldWidth = systemParameters.width;
		int oldHeight = systemParameters.height;
		
		SnakeInjector.getInstance().getInstance(ISystemParametersService.class).NewResolutionWereSet(oldWidth - 1, oldHeight - 1);
		
		systemParameters = SnakeInjector.getInstance().getInstance(ISystemParametersService.class).GetSystemParameters();
		assertTrue(systemParameters.width == oldWidth - 1 && systemParameters.height == oldHeight - 1);
	}
}
