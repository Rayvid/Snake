package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestSpriteIsolated
{
	private Injector _testInjectorInstance;
	private ISpriteService _mockedSpriteService;

	public TestSpriteIsolated()
	{
		_mockedSpriteService = createMock(ISpriteService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						ISpriteService.class,
						_mockedSpriteService,
						ISpriteService.class));
	}
	
	@Test
	public void testIfCreatePauseControlGetsPausePressedAndUnpressedSprites()
	{
		IControlService controlService = _testInjectorInstance.getInstance(IControlService.class);
		
		expect(_mockedSpriteService.getPauseButtonPressed()).andReturn(null);
		expect(_mockedSpriteService.getPauseButtonUnpressed()).andReturn(null);
		replay(_mockedSpriteService);
		
		controlService.CreatePauseControl();
		verify(_mockedSpriteService);
	}
}
