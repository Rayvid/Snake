package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.font.FontService;
import inc.bezdelniki.snakegame.font.IFontService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.score.ScoreService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestRuntimeParamsIsolated
{
	private Injector _testInjectorInstance;
	private IRuntimeParamsService _mockedRuntimeParamsService;

	private class TestDeviceBindingsConfiguration extends AbstractModule
	{
		@Override
		protected void configure()
		{
			RuntimeParams runtimeParams = new RuntimeParams();
			bind(RuntimeParams.class).toInstance(runtimeParams);
			
			bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(ITimeService.class).to(TimeService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(IFontService.class).to(FontService.class);
			bind(ILyingItemService.class).to(LyingItemService.class);
			bind(IScoreService.class).to(ScoreService.class);
			bind(IGameWorldService.class).to(GameWorldService.class);
			
			bind(IRuntimeParamsService.class).toInstance(_mockedRuntimeParamsService);
		}
	}

	public TestRuntimeParamsIsolated()
	{
		_mockedRuntimeParamsService = createMock(IRuntimeParamsService.class);
		_testInjectorInstance = Guice.createInjector(new TestDeviceBindingsConfiguration());
	}
	
	@Test
	public void testIfInitGameWorldCallsCreateParamsForNewGameUsingConfiguredInstance()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		_mockedRuntimeParamsService.initParamsForNewGame(runtimeParams);
		replay(_mockedRuntimeParamsService);
		
		GameWorldService worldService = (GameWorldService) _testInjectorInstance.getInstance(IGameWorldService.class);
		worldService.initGameWorld();
		verify(_mockedRuntimeParamsService);
	}
	
	@Test
	public void testIfAdjustLayoutParamsDependsOnScreenResolution()
	{	
		IAppSettingsService appSettingsService = new AppSettingsService();
		ISystemParamsService systemParamsService = new SystemParamsService(appSettingsService);
		
		RuntimeParams runtimeParams = new RuntimeParams();
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);
		
		RuntimeParamsService runtimeParamsService = new RuntimeParamsService(systemParamsService, appSettingsService, deviceService);
		
		systemParamsService.newResolutionWereSet(480, 315);
		runtimeParamsService.adjustLayoutParams(runtimeParams);
		int oldGameBoxPaddingTop = runtimeParams.layoutParams.gameBoxPaddingTop;
		int oldGameBoxPaddingLeft = runtimeParams.layoutParams.gameBoxPaddingLeft;
		int oldGameBoxPaddingRight = runtimeParams.layoutParams.gameBoxPaddingRight;
		int oldGameBoxPaddingBottom = runtimeParams.layoutParams.gameBoxPaddingBottom;
		
		systemParamsService.newResolutionWereSet(3150, 4800);
		runtimeParamsService.adjustLayoutParams(runtimeParams);
		
		assertTrue(oldGameBoxPaddingTop < runtimeParams.layoutParams.gameBoxPaddingTop);
		assertTrue(oldGameBoxPaddingLeft < runtimeParams.layoutParams.gameBoxPaddingLeft);
		assertTrue(oldGameBoxPaddingRight < runtimeParams.layoutParams.gameBoxPaddingRight);
		assertTrue(oldGameBoxPaddingBottom < runtimeParams.layoutParams.gameBoxPaddingBottom);
	}
	
	@Test
	public void testIfScoreCoordsFitsLayout()
	{
		IAppSettingsService appSettingsService = new AppSettingsService();
		ISystemParamsService systemParamsService = new SystemParamsService(appSettingsService);

		RuntimeParams runtimeParams = new RuntimeParams();
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService, runtimeParams);

		RuntimeParamsService runtimeParamsService = new RuntimeParamsService(systemParamsService, appSettingsService, deviceService);
	
		systemParamsService.newResolutionWereSet(480, 315);
		SystemParams systemParams = systemParamsService.getSystemParams();
		runtimeParamsService.adjustLayoutParams(runtimeParams);
		
		assertTrue(systemParams.height - runtimeParams.layoutParams.gameBoxPaddingTop / 2 < runtimeParams.layoutParams.scoreCoords.y);
		assertTrue(systemParams.height > runtimeParams.layoutParams.scoreCoords.y);
		
		systemParamsService.newResolutionWereSet(3150, 4800);
		systemParams = systemParamsService.getSystemParams();
		runtimeParamsService.adjustLayoutParams(runtimeParams);
		
		assertTrue(systemParams.height - runtimeParams.layoutParams.gameBoxPaddingTop / 2 < runtimeParams.layoutParams.scoreCoords.y);
		assertTrue(systemParams.height > runtimeParams.layoutParams.scoreCoords.y);
	}
}
