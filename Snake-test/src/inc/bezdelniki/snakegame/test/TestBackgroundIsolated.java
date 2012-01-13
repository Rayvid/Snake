package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import inc.bezdelniki.snakegame.resources.background.IBackgroundService;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestBackgroundIsolated
{
	@SuppressWarnings("unused")
	private Injector _testInjectorInstance;
	private IBackgroundService _mockedBackgroundService;

	public TestBackgroundIsolated()
	{
		_mockedBackgroundService = createMock(IBackgroundService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IBackgroundService.class,
						_mockedBackgroundService,
						IBackgroundService.class));
	}
	
	// Nothing can be tested atm without resources layer mocking
}
