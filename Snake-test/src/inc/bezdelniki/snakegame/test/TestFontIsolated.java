package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.font.FontService;
import inc.bezdelniki.snakegame.font.IFontService;
import inc.bezdelniki.snakegame.font.configuration.FontConfiguration;
import inc.bezdelniki.snakegame.font.configuration.FontConfigurationItem;

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
	
	@Test
	public void testIfCorrectFontIsChoosenAccordingTileSize()
	{
		IDeviceService mockedDeviceService = createMock(IDeviceService.class);
		
		expect(mockedDeviceService.getTileSize()).andReturn(16).anyTimes();
		replay(mockedDeviceService);
		
		FontConfiguration fontConfiguration = new FontConfiguration();
		fontConfiguration.configurationItems = new ArrayList<FontConfigurationItem>();
		
		FontConfigurationItem item1 = new FontConfigurationItem();
		item1.tileSizeMin = 0;
		fontConfiguration.configurationItems.add(item1);
		
		FontConfigurationItem item2 = new FontConfigurationItem();
		item1.tileSizeMin = 17;
		fontConfiguration.configurationItems.add(item2);
		
		IFontService fontService = new FontService(mockedDeviceService, fontConfiguration);
		int tileSize1 = fontService.getCurrentFontConfigurationItem().tileSizeMin;
		assertTrue(tileSize1 <= 16);
		
		reset(mockedDeviceService);
		expect(mockedDeviceService.getTileSize()).andReturn(32).anyTimes();
		replay(mockedDeviceService);
		
		int tileSize2 = fontService.getCurrentFontConfigurationItem().tileSizeMin;
		assertTrue(tileSize2 <= 32);
		assertTrue(tileSize1 < tileSize2);
	}
}
