package inc.bezdelniki.snakegame.control;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.device.IDeviceService;

public class ControlService implements IControlService
{
	private IDeviceService _deviceService;
	
	@Inject
	public ControlService(IDeviceService deviceService)
	{
		_deviceService = deviceService;
	}
	
	@Override
	public PauseControl CreatePauseControl()
	{
		return new PauseControl(_deviceService);
	}

	@Override
	public ArrowPadControl CreateArrowPadControl()
	{
		return new ArrowPadControl(_deviceService);
	}

}
