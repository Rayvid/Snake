package inc.bezdelniki.snakegame.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

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
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.test.helpers.BindingsConfigurationFactory;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
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
	public void testIfControlServiceProducesFullyFunctionalPauseControl()
	{
		ControlService controlService = new ControlService(
				_testInjectorInstance.getInstance(ISystemParamsService.class),
				_testInjectorInstance.getInstance(IDeviceService.class),
				_testInjectorInstance.getInstance(ISpriteService.class),
				_testInjectorInstance.getInstance(IPresentationService.class),
				_testInjectorInstance.getInstance(IUserActionService.class));
		PauseControl pauseControl = controlService.CreatePauseControl();
		
		assertTrue(pauseControl.coords != null);
		assertTrue(pauseControl.width > 0);
		assertTrue(pauseControl.height > 0);
		assertTrue(pauseControl.noTouchImage != null);
		assertTrue(pauseControl.touchableRegions.size() > 0);
		for (TouchableRegion touchableRegion : pauseControl.touchableRegions)
		{
			assertTrue(touchableRegion.imageWhenTouched != null);
			assertTrue(touchableRegion.userActionWhenTouched != null);
		}
		assertTrue(pauseControl.regionCurrentlyTouched == null);
	}
	
	@Test
	public void testIfControlServiceProducesFullyFunctionalArrowPadControl()
	{
		ControlService controlService = new ControlService(
				_testInjectorInstance.getInstance(ISystemParamsService.class),
				_testInjectorInstance.getInstance(IDeviceService.class),
				_testInjectorInstance.getInstance(ISpriteService.class),
				_testInjectorInstance.getInstance(IPresentationService.class),
				_testInjectorInstance.getInstance(IUserActionService.class));
		PauseControl arrowPadControl = controlService.CreatePauseControl();
		
		assertTrue(arrowPadControl.coords != null);
		assertTrue(arrowPadControl.width > 0);
		assertTrue(arrowPadControl.height > 0);
		assertTrue(arrowPadControl.noTouchImage != null);
		assertTrue(arrowPadControl.touchableRegions.size() > 0);
		for (TouchableRegion touchableRegion : arrowPadControl.touchableRegions)
		{
			assertTrue(touchableRegion.imageWhenTouched != null);
			assertTrue(touchableRegion.userActionWhenTouched != null);
		}
		assertTrue(arrowPadControl.regionCurrentlyTouched == null);
	}
	
	@Test
	public void testIfScreenResolutionChangeInvokesRecalculateControlLayout()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IRuntimeParamsService runtimeParamsService = _testInjectorInstance.getInstance(IRuntimeParamsService.class);
		
		runtimeParamsService.initParamsForNewGame(runtimeParams);
		
		Control control1 = createNiceMock(PauseControl.class);
		runtimeParams.layoutParams.controls.add(control1);
		
		Control control2 = createNiceMock(ArrowPadControl.class);
		runtimeParams.layoutParams.controls.add(control2);
		
		for (Control control : runtimeParams.layoutParams.controls)
		{
			_mockedControlService.adjustLayoutParams(control);
		}
		replay(_mockedControlService);
		
		runtimeParamsService.adjustLayoutParams(runtimeParams);
		verify(_mockedControlService);
	}
	
	@Test
	public void testIfAdjustControlsOnTouchCallsControlServiceAsMuchTimesAsThereIsControls()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IRuntimeParamsService runtimeParamsService = _testInjectorInstance.getInstance(IRuntimeParamsService.class);
		
		runtimeParamsService.initParamsForNewGame(runtimeParams);
		
		Control control1 = createNiceMock(PauseControl.class);
		runtimeParams.layoutParams.controls.add(control1);
		
		Control control2 = createNiceMock(ArrowPadControl.class);
		runtimeParams.layoutParams.controls.add(control2);
		
		Random random = new Random();
		TouchCoords touchCoords = new TouchCoords(random.nextInt(), random.nextInt());
		for (Control control : runtimeParams.layoutParams.controls)
		{
			expect(_mockedControlService.GetUserActionIfTouched(control, touchCoords)).andReturn(null);
		}
		replay(_mockedControlService);
		
		runtimeParamsService.adjustControlsOnTouch(runtimeParams, touchCoords);
		verify(_mockedControlService);
	}
	
	@Test
	public void testIfAdjustControlsOnReleaseCallsControlServiceAsMuchTimesAsThereIsControls()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		IRuntimeParamsService runtimeParamsService = _testInjectorInstance.getInstance(IRuntimeParamsService.class);
		
		runtimeParamsService.initParamsForNewGame(runtimeParams);
		
		Control control1 = createNiceMock(PauseControl.class);
		runtimeParams.layoutParams.controls.add(control1);
		
		Control control2 = createNiceMock(ArrowPadControl.class);
		runtimeParams.layoutParams.controls.add(control2);
		
		Random random = new Random();
		TouchCoords touchCoords = new TouchCoords(random.nextInt(), random.nextInt());
		for (Control control : runtimeParams.layoutParams.controls)
		{
			_mockedControlService.ReleaseTouch(control);
		}
		replay(_mockedControlService);
		
		runtimeParamsService.adjustControlsOnTouch(runtimeParams, touchCoords);
		verify(_mockedControlService);
	}
	
	@Test
	public void testIfControlsProducesUserActionIfTouchedInTouchableZone()
	{
		RuntimeParams runtimeParams = _testInjectorInstance.getInstance(RuntimeParams.class);
		ControlService controlService = new ControlService(
				_testInjectorInstance.getInstance(ISystemParamsService.class),
				_testInjectorInstance.getInstance(IDeviceService.class),
				_testInjectorInstance.getInstance(ISpriteService.class),
				_testInjectorInstance.getInstance(IPresentationService.class),
				_testInjectorInstance.getInstance(IUserActionService.class));
		
		runtimeParams.layoutParams.controls.add(controlService.CreatePauseControl());
		runtimeParams.layoutParams.controls.add(controlService.CreateArrowPadControl());
		
		for (int i = 0; i < 10; i++)
		{
			Random random = new Random(i);
		
			for (Control control : runtimeParams.layoutParams.controls)
			{	
				for (TouchableRegion touchableRegion : control.touchableRegions)
				{
					UserAction userAction = controlService.GetUserActionIfTouched(control, new TouchCoords(random.nextInt(), random.nextInt()));
					assertTrue(userAction != null && userAction.getClass() != NoAction.class);
				}
			}
		}
	}
}
