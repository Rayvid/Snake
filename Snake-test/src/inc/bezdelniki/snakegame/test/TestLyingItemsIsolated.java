package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IAnswer;
import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Guice;
import com.google.inject.Injector;

import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
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
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		ILyingItemService service = new LyingItemService(presentationService);

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

	@Test
	public void testIfPresentLyingItemCallsPresentLyingItemMethodAccordingLyingItemsCount() throws LyingItemNowhereToPlaceException
	{
		IGameWorldService gameWorldService = _testInjectorInstance.getInstance(IGameWorldService.class);
		IPresentationService presentationService = _testInjectorInstance.getInstance(IPresentationService.class);
		final ILyingItemService lyingItemService = new LyingItemService(presentationService);

		gameWorldService.initGameWorld();

		SpriteBatch batch = null;
		expect(
				_mockedLyingItemService.createLyingItem(
						isA(ItemType.class),
						isA(WorldPosition.class)))
				.andAnswer(new IAnswer<LyingItem>()
				{
					public LyingItem answer()
					{
						Object[] args = getCurrentArguments();
						return lyingItemService.createLyingItem((ItemType)args[0], (WorldPosition)args[1]);
					}
				})
				.anyTimes();
		_mockedLyingItemService.presentLyingItem(eq(batch), isA(LyingItem.class));
		replay(_mockedLyingItemService);

		gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
		gameWorldService.presentAllLyingItems(batch);
		verify(_mockedLyingItemService);

		reset(_mockedLyingItemService);
		expect(
				_mockedLyingItemService.createLyingItem(
						isA(ItemType.class),
						isA(WorldPosition.class)))
				.andAnswer(new IAnswer<LyingItem>()
				{
					public LyingItem answer()
					{
						Object[] args = getCurrentArguments();
						return lyingItemService.createLyingItem((ItemType)args[0], (WorldPosition)args[1]);
					}
				})
				.anyTimes();
		_mockedLyingItemService.presentLyingItem(eq(batch), isA(LyingItem.class));
		_mockedLyingItemService.presentLyingItem(eq(batch), isA(LyingItem.class));
		replay(_mockedLyingItemService);

		gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
		gameWorldService.presentAllLyingItems(batch);
		verify(_mockedLyingItemService);
	}
}
