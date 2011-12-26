package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
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
			bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(IDeviceService.class).to(DeviceService.class);
			bind(ITimeService.class).to(TimeService.class);
			bind(IPresentationService.class).to(PresentationService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			bind(ILyingItemService.class).to(LyingItemService.class);
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
	public void testIfInitGameWorldCallsInitRuntimeParameters4NewGameUnderTheHood()
	{
		RuntimeParams mockedReturn;
		mockedReturn = new RuntimeParams();
		
		expect(_mockedRuntimeParamsService.createParamsForNewGame()).andReturn(mockedReturn);
		replay(_mockedRuntimeParamsService);
		
		GameWorldService worldService = (GameWorldService) _testInjectorInstance.getInstance(IGameWorldService.class);
		worldService.initGameWorld();
		verify(_mockedRuntimeParamsService);
	}
	
	@Test
	public void testIfRuntimeParams4NewGameDependsOnScreenResolution()
	{	
		IAppSettingsService appSettingsService = new AppSettingsService();
		ISystemParamsService systemParamsService = new SystemParamsService(appSettingsService);
		IDeviceService deviceService = new DeviceService(systemParamsService, appSettingsService);
		RuntimeParamsService runtimeParamsService = new RuntimeParamsService(systemParamsService, appSettingsService, deviceService);
		
		systemParamsService.newResolutionWereSet(480, 315);
		RuntimeParams runtimeParams1 = runtimeParamsService.createParamsForNewGame();
		
		systemParamsService.newResolutionWereSet(3150, 4800);
		RuntimeParams runtimeParams2 = runtimeParamsService.createParamsForNewGame();
		
		assertTrue(runtimeParams1.layoutParams.gameBoxPaddingTop < runtimeParams2.layoutParams.gameBoxPaddingTop);
		assertTrue(runtimeParams1.layoutParams.gameBoxPaddingLeft < runtimeParams2.layoutParams.gameBoxPaddingLeft);
		assertTrue(runtimeParams1.layoutParams.gameBoxPaddingRight < runtimeParams2.layoutParams.gameBoxPaddingRight);
		assertTrue(runtimeParams1.layoutParams.gameBoxPaddingBottom < runtimeParams2.layoutParams.gameBoxPaddingBottom);
	}
	
}
