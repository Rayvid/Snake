package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.resources.font.FontService;
import inc.bezdelniki.snakegame.resources.font.IFontService;
import inc.bezdelniki.snakegame.resources.font.configuration.FontConfiguration;
import inc.bezdelniki.snakegame.resources.font.configuration.FontConfigurationItem;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestFontIsolated
{
	@SuppressWarnings("unused")
	private Injector _testInjectorInstance;
	private IFontService _mockedFontService;

	public TestFontIsolated()
	{
		_mockedFontService = createMock(IFontService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IFontService.class,
						_mockedFontService,
						IFontService.class));
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
