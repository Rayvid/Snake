package inc.bezdelniki.snakegame.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;

public class ControlService implements IControlService
{
	private ISystemParamsService _systemParamsService;
	private IDeviceService _deviceService;
	private ISpriteService _spriteService;
	private IPresentationService _presentationService;
	
	@Inject
	public ControlService(
			ISystemParamsService systemParamsService,
			IDeviceService deviceService,
			ISpriteService spriteService,
			IPresentationService presentationService)
	{
		_systemParamsService = systemParamsService;
		_deviceService = deviceService;
		_spriteService = spriteService;
		_presentationService = presentationService;
	}
	
	@Override
	public PauseControl CreatePauseControl()
	{
		return new PauseControl(_systemParamsService, _deviceService, _spriteService);
	}

	@Override
	public ArrowPadControl CreateArrowPadControl()
	{
		return new ArrowPadControl(_deviceService);
	}

	@Override
	public void Present(SpriteBatch batch, Control control)
	{
		_presentationService.presentControl(batch, control);
	}
}
