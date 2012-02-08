package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.control.ControlService;
import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.control.dtos.TouchableRegion;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;
import inc.bezdelniki.snakegame.useraction.dtos.NoAction;
import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

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
		
	}

	@Test
	public void testIfAdjustLayoutParamsCallsAllControlsRecalculateControlLayout()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IDeviceService deviceService = _testInjectorInstance.getInstance(IDeviceService.class);
		IRuntimeParamsService runtimeParamsService = new RuntimeParamsService(_testInjectorInstance.getInstance(ISystemParamsService.class), appSettingsService, deviceService);
		
		Control control1 = createNiceMock(PauseControl.class);
		runtimeParams.layoutParams.controls.add(control1);
		
		Control control2 = createNiceMock(ArrowPadControl.class);
		runtimeParams.layoutParams.controls.add(control2);
		
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
		
		Control control1 = createNiceMock(PauseControl.class);
		runtimeParams.layoutParams.controls.add(control1);
		
		Control control2 = createNiceMock(ArrowPadControl.class);
		runtimeParams.layoutParams.controls.add(control2);
		
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
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		ControlService controlService = new ControlService(
				_testInjectorInstance.getInstance(ISystemParamsService.class),
				_testInjectorInstance.getInstance(IDeviceService.class),
				_testInjectorInstance.getInstance(ISpriteService.class),
				_testInjectorInstance.getInstance(IPresentationService.class));
		
		runtimeParams.layoutParams.controls.add(controlService.CreatePauseControl());
		runtimeParams.layoutParams.controls.add(controlService.CreateArrowPadControl());
		
		for (int i = 0; i < 10; i++)
		{
			Random random = new Random(i);
		
			for (Control control : runtimeParams.layoutParams.controls)
			{	
				for (TouchableRegion touchableRegion : control.touchableRegions)
				{
					UserAction userAction = control.translateTouchToUserAction(new TouchCoords(random.nextInt(), random.nextInt()));
					assertTrue(userAction != null && userAction.getClass() != NoAction.class);
				}
			}
		}
	}
}
