package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.TouchableRegion;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestRuntimeParamsIsolated
{
	private Injector _testInjectorInstance;
	private IRuntimeParamsService _mockedRuntimeParamsService;

	public TestRuntimeParamsIsolated()
	{
		_mockedRuntimeParamsService = createMock(IRuntimeParamsService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IRuntimeParamsService.class,
						_mockedRuntimeParamsService,
						IRuntimeParamsService.class));
	}
	
	@Test
	public void testIfSystemParamsNewResolutionWereSetCallsAdjustLayoutParams()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(SystemParamsService.class);
		
		_mockedRuntimeParamsService.adjustLayoutParams(runtimeParams);
		replay(_mockedRuntimeParamsService);
		systemParamsService.newResolutionWereSet(540, 315);
		verify(_mockedRuntimeParamsService);
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
	public void testIfAdjustLayoutParamsResultDependsOnScreenResolution()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(IDeviceService.class);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(_testInjectorInstance.getInstance(ISystemParamsService.class), appSettingsService, deviceService);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		
		_mockedRuntimeParamsService.adjustLayoutParams(runtimeParams);
		expectLastCall().andDelegateTo(runtimeParamsService).anyTimes();
		replay(_mockedRuntimeParamsService);
	
		systemParamsService.newResolutionWereSet(480, 315);
		
		int oldGameBoxPaddingTop = runtimeParams.layoutParams.gameBoxPaddingTop;
		int oldGameBoxPaddingLeft = runtimeParams.layoutParams.gameBoxPaddingLeft;
		int oldGameBoxPaddingRight = runtimeParams.layoutParams.gameBoxPaddingRight;
		int oldGameBoxPaddingBottom = runtimeParams.layoutParams.gameBoxPaddingBottom;
		
		systemParamsService.newResolutionWereSet(3150, 4800);
		
		assertTrue(oldGameBoxPaddingTop < runtimeParams.layoutParams.gameBoxPaddingTop);
		assertTrue(oldGameBoxPaddingLeft < runtimeParams.layoutParams.gameBoxPaddingLeft);
		assertTrue(oldGameBoxPaddingRight < runtimeParams.layoutParams.gameBoxPaddingRight);
		assertTrue(oldGameBoxPaddingBottom < runtimeParams.layoutParams.gameBoxPaddingBottom);
	}
	
	@Test
	public void testIfScoreCoordsFitsLayout()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(IDeviceService.class);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(_testInjectorInstance.getInstance(ISystemParamsService.class), appSettingsService, deviceService);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		
		_mockedRuntimeParamsService.adjustLayoutParams(runtimeParams);
		expectLastCall().andDelegateTo(runtimeParamsService).anyTimes();
		replay(_mockedRuntimeParamsService);
	
		systemParamsService.newResolutionWereSet(480, 315);
		SystemParams systemParams = systemParamsService.getSystemParams();
		
		assertTrue(systemParams.height - runtimeParams.layoutParams.gameBoxPaddingTop / 2 < runtimeParams.layoutParams.scoreCoords.y);
		assertTrue(systemParams.height > runtimeParams.layoutParams.scoreCoords.y);
		assertTrue(systemParams.width - 20 > runtimeParams.layoutParams.scoreCoords.x);
		assertTrue(0 <= runtimeParams.layoutParams.scoreCoords.x);
		
		systemParamsService.newResolutionWereSet(3150, 4800);
		systemParams = systemParamsService.getSystemParams();
		
		assertTrue(systemParams.height - runtimeParams.layoutParams.gameBoxPaddingTop / 2 < runtimeParams.layoutParams.scoreCoords.y);
		assertTrue(systemParams.height > runtimeParams.layoutParams.scoreCoords.y);
		assertTrue(systemParams.width - 20 > runtimeParams.layoutParams.scoreCoords.x);
		assertTrue(0 <= runtimeParams.layoutParams.scoreCoords.x);
	}
	
	@Test
	public void testIfFpsCoordsFitsLayout()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(IDeviceService.class);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(_testInjectorInstance.getInstance(ISystemParamsService.class), appSettingsService, deviceService);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		
		_mockedRuntimeParamsService.adjustLayoutParams(runtimeParams);
		expectLastCall().andDelegateTo(runtimeParamsService).anyTimes();
		replay(_mockedRuntimeParamsService);
	
		systemParamsService.newResolutionWereSet(480, 315);
		SystemParams systemParams = systemParamsService.getSystemParams();
		
		assertTrue(runtimeParams.layoutParams.gameBoxPaddingBottom / 2 < runtimeParams.layoutParams.fpsCoords.y);
		assertTrue(runtimeParams.layoutParams.gameBoxPaddingBottom > runtimeParams.layoutParams.fpsCoords.y);
		assertTrue(systemParams.width - 20 > runtimeParams.layoutParams.fpsCoords.x);
		assertTrue(0 <= runtimeParams.layoutParams.fpsCoords.x);
		
		systemParamsService.newResolutionWereSet(3150, 4800);
		systemParams = systemParamsService.getSystemParams();
		
		assertTrue(runtimeParams.layoutParams.gameBoxPaddingBottom / 2 < runtimeParams.layoutParams.fpsCoords.y);
		assertTrue(runtimeParams.layoutParams.gameBoxPaddingBottom > runtimeParams.layoutParams.fpsCoords.y);
		assertTrue(systemParams.width - 20 > runtimeParams.layoutParams.fpsCoords.x);
		assertTrue(0 <= runtimeParams.layoutParams.fpsCoords.x);
	}
	
	@Test
	public void testIfControlsFitsLayout()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(IDeviceService.class);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(_testInjectorInstance.getInstance(ISystemParamsService.class), appSettingsService, deviceService);
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		IControlService controlService = _testInjectorInstance.getInstance(IControlService.class);
		
		_mockedRuntimeParamsService.adjustLayoutParams(runtimeParams);
		expectLastCall().andDelegateTo(runtimeParamsService).anyTimes();
		replay(_mockedRuntimeParamsService);
		
		runtimeParams.layoutParams.controls.add(controlService.CreatePauseControl());
		runtimeParams.layoutParams.controls.add(controlService.CreateArrowPadControl());
	
		systemParamsService.newResolutionWereSet(480, 315);
		SystemParams systemParams = systemParamsService.getSystemParams();
		
		for (Control control : runtimeParams.layoutParams.controls)
		{
			for (TouchableRegion touchRegion : control.touchableRegions)
			{
				assertTrue(
						touchRegion.x >= 0
						&& touchRegion.y >= 0
						&& touchRegion.x + touchRegion.width <= control.width
						&& touchRegion.y + touchRegion.height <= control.height);
				
				assertTrue(
						control.y - touchRegion.y <= runtimeParams.layoutParams.gameBoxPaddingBottom
						|| control.y - control.height == 0
						|| control.y - touchRegion.y - touchRegion.height >= runtimeParams.layoutParams.gameBoxPaddingTop
						|| control.y == systemParams.height - 1);
				
				assertTrue(
						control.x + touchRegion.x + touchRegion.width <= runtimeParams.layoutParams.gameBoxPaddingLeft
						|| control.x == 0
						|| control.x + touchRegion.x >= runtimeParams.layoutParams.gameBoxPaddingRight
						|| control.x + control.width == systemParams.width);
			}
			
			for (Control controlToCompare : runtimeParams.layoutParams.controls)
			{
				if (controlToCompare != control)
				{
					assertTrue(
							(control.x + control.width <= controlToCompare.x
									|| control.x >= controlToCompare.x + controlToCompare.width)
							&& (control.y + control.height <= controlToCompare.y
									|| control.y >= controlToCompare.y + controlToCompare.height)
							);
				}
			}
		}
		
		systemParamsService.newResolutionWereSet(3150, 4800);
		systemParams = systemParamsService.getSystemParams();
		
		for (Control control : runtimeParams.layoutParams.controls)
		{
			for (TouchableRegion touchRegion : control.touchableRegions)
			{
				assertTrue(
						touchRegion.x >= 0
						&& touchRegion.y >= 0
						&& touchRegion.x + touchRegion.width <= control.width
						&& touchRegion.y + touchRegion.height <= control.height);
				
				assertTrue(
						control.y - touchRegion.y <= runtimeParams.layoutParams.gameBoxPaddingBottom
						|| control.y - control.height == 0
						|| control.y - touchRegion.y - touchRegion.height >= runtimeParams.layoutParams.gameBoxPaddingTop
						|| control.y == systemParams.height - 1);
				
				assertTrue(
						control.x + touchRegion.x + touchRegion.width <= runtimeParams.layoutParams.gameBoxPaddingLeft
						|| control.x == 0
						|| control.x + touchRegion.x >= runtimeParams.layoutParams.gameBoxPaddingRight
						|| control.x + control.width == systemParams.width);
			}
			
			for (Control controlToCompare : runtimeParams.layoutParams.controls)
			{
				if (controlToCompare != control)
				{
					assertTrue(
							(control.x + control.width <= controlToCompare.x
									|| control.x >= controlToCompare.x + controlToCompare.width)
							&& (control.y + control.height <= controlToCompare.y
									|| control.y >= controlToCompare.y + controlToCompare.height)
							);
				}
			}
		}
	}
}
