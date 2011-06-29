package inc.bezdelniki.snake.test;

import inc.bezdelniki.snake.SnakeInjector;
import inc.bezdelniki.snake.systemparameters.*;
import inc.bezdelniki.snake.systemparameters.dto.*;
import junit.framework.TestCase;

public class TestSystemParameters extends TestCase {
	public void testResolutionChangesPersistsBetweenResolves() {
		SystemParameters systemParameters = SnakeInjector.getInstance().getInstance(ISystemParametersService.class).GetSystemParameters();
		int oldWidth = systemParameters.width;
		int oldHeight = systemParameters.height;
		
		SnakeInjector.getInstance().getInstance(ISystemParametersService.class).NewResolutionWereSet(oldWidth - 1, oldHeight - 1);
		
		systemParameters = SnakeInjector.getInstance().getInstance(ISystemParametersService.class).GetSystemParameters();
		assertTrue(systemParameters.width == oldWidth - 1 && systemParameters.height == oldHeight - 1);
	}
}
