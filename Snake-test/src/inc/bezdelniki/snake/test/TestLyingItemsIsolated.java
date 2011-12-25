package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;


import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;

public class TestLyingItemsIsolated
{
	private Injector _testInjectorInstance;
	private ILyingItemService _mockedLyingItemService;

	private class TestLyingItemsBindingsConfiguration extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IRuntimeParamsService.class).to(RuntimeParamsService.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(ITimeService.class).to(TimeService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(IGameWorldService.class).to(GameWorldService.class);

			bind(ILyingItemService.class).toInstance(_mockedLyingItemService);
		}
	}

	public TestLyingItemsIsolated()
	{
		_mockedLyingItemService = createMock(ILyingItemService.class);
		_testInjectorInstance = Guice.createInjector(new TestLyingItemsBindingsConfiguration());
	}
	
	@Test
	public void testIfLyingItemIsCreatedWithSpecifiedParameters()
	{
		ILyingItemService service = new LyingItemService();

		WorldPosition position = new WorldPosition(1, 2);
		LyingItem item = service.createLyingItem(ItemType.APPLE, position);

		assertTrue(item.itemType == ItemType.APPLE && item.position.tileX == 1 && item.position.tileY == 2);
	}

	@Test
	public void testIfLyingItemHasBeenCreatedSomewhere() throws LyingItemNowhereToPlaceException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		gameWorldService.initGameWorld();

		int itemsCount = gameWorldService.getGameWorld().lyingItems.size();
		gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
		assertTrue(gameWorldService.getGameWorld().lyingItems.size() > itemsCount);
	}
	
	@Test
	public void testIfLyingItemHasBeenCreatedSomewhereCallsCreateLyingItemUnderTheHood() throws LyingItemNowhereToPlaceException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		gameWorldService.initGameWorld();
		
		expect(_mockedLyingItemService.createLyingItem(isA(ItemType.class), isA(WorldPosition.class))).andReturn(new LyingItem());
		replay(_mockedLyingItemService);

		gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
		verify(_mockedLyingItemService);
	}
}
