package inc.bezdelniki.snakegame.control;

import com.google.inject.Inject;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;

public class ControlService implements IControlService
{
	private IDeviceService _deviceService;
	private ISpriteService _spriteService;
	
	@Inject
	public ControlService(
			IDeviceService deviceService,
			ISpriteService spriteService)
	{
		_deviceService = deviceService;
		_spriteService = spriteService;
	}
	
	@Override
	public PauseControl CreatePauseControl()
	{
		return new PauseControl(_deviceService, _spriteService);
	}

	@Override
	public ArrowPadControl CreateArrowPadControl()
	{
		return new ArrowPadControl(_deviceService);
	}

}
