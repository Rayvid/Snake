package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.controls.IControlsService;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestControlsIsolated
{
	private Injector _testInjectorInstance;
	private IControlsService _mockedControlsService;

	public TestControlsIsolated()
	{
		_mockedControlsService = createMock(IControlsService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IControlsService.class,
						_mockedControlsService,
						IControlsService.class));
	}
	
	@Test
	public void testIfControlsProducesUserActionIfTouchedInTouchableZone()
	{
		fail();
	}
}
