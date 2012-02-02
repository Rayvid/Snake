package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestControlsIsolated
{
	private Injector _testInjectorInstance;
	private IControlService _mockedControlService;

	public TestControlsIsolated()
	{
		_mockedControlService = createMock(IControlService.class);
		_testInjectorInstance = Guice.createInjector(
				BindingsConfigurationFactory.BuildDefaultBindingsConfiguration(
						IControlService.class,
						_mockedControlService,
						IControlService.class));
		
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		
		Control control1 = createMock(PauseControl.class);
		runtimeParams.layoutParams.controls.add(control1);
		
		Control control2 = createMock(ArrowPadControl.class);
		runtimeParams.layoutParams.controls.add(control2);
	}

	@Test
	public void testIfAdjustLayoutParamsCallsAllControlsRecalculateControlLayout()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(IDeviceService.class);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(_testInjectorInstance.getInstance(ISystemParamsService.class), appSettingsService, deviceService);
		
		for (Control control : runtimeParams.layoutParams.controls)
		{
			control.recalculateControlLayout();
			replay(control);
		}
		
		runtimeParamsService.adjustLayoutParams(runtimeParams);
		for (Control control : runtimeParams.layoutParams.controls)
		{
			verify(control);
		}
	}
	
	@Test
	public void testIfScreenResolutionChangeInvokesAdjustToLostContextOrChangedResolution()
	{
		ISystemParamsService systemParamsService = _testInjectorInstance.getInstance(ISystemParamsService.class);
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		
		for (Control control : runtimeParams.layoutParams.controls)
		{
			control.adjustToLostContextOrChangedResolution();
			replay(control);
		}
		
		SystemParams systemParams = systemParamsService.getSystemParams();
		systemParamsService.newResolutionWereSet(systemParams.height, systemParams.width);
		for (Control control : runtimeParams.layoutParams.controls)
		{
			verify(control);
		}
	}
	
	@Test
	public void testIfControlsProducesUserActionIfTouchedInTouchableZone()
	{
		fail();
	}
}
