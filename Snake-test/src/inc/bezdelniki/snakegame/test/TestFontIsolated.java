package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import inc.bezdelniki.snakegame.font.IFontService;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestFontIsolated
{
	private Injector _testInjectorInstance;
	private IFontService _mockedFontService;

	private class TestLyingItemsBindingsConfiguration extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind(IFontService.class).toInstance(_mockedFontService);
		}
	}

	public TestFontIsolated()
	{
		_mockedFontService = createMock(IFontService.class);
		_testInjectorInstance = Guice.createInjector(new TestLyingItemsBindingsConfiguration());
	}
}
