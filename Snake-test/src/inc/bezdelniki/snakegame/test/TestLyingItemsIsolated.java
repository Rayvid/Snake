package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;


import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

public class TestLyingItemsIsolated
{
	private Injector _testInjectorInstance;
	private ILyingItemService _mockedLyingItemService;

	public TestLyingItemsIsolated()
	{
		_mockedLyingItemService = createMock(ILyingItemService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						ILyingItemService.class,
						_mockedLyingItemService,
						ILyingItemService.class));
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
