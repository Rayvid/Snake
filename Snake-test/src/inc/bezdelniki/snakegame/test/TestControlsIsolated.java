package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
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
		runtimeParams.layoutParams.controls.add(createMock(Control.class));
		runtimeParams.layoutParams.controls.add(createMock(Control.class));
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
			control.recalculateControlLayout(runtimeParams.layoutParams);
			replay(control);
		}
		
		runtimeParamsService.adjustLayoutParams(runtimeParams);
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
