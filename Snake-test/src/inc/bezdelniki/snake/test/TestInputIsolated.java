package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.SystemParametersService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.UserActionService;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestInputIsolated {
	private Injector _testInjectorInstance;
	private IInputService _mockedInputService;
	
	private class TestInputBindingsConfiguration extends AbstractModule {
		@Override
		protected void configure()
		{
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(ISystemParametersService.class).to(SystemParametersService.class).in(Singleton.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(ITimeService.class).to(TimeService.class);
			bind(IGameWorldService.class).to(GameWorldService.class);
			bind(IUserActionService.class).to(UserActionService.class);
			
			bind(IInputService.class).toInstance(_mockedInputService);
		}
	}
	
	public TestInputIsolated()
	{
		_mockedInputService = createMock(IInputService.class);
		_testInjectorInstance = Guice.createInjector(new TestInputBindingsConfiguration());
	}
	
	@Test
	public void testIfCorrectUserActionIsCreatedAccordingTouch()
	{
		fail();
	}
}
