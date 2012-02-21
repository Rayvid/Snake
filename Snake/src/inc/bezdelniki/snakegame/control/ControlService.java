package inc.bezdelniki.snakegame.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

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
		return new PauseControl();
	}

	@Override
	public ArrowPadControl CreateArrowPadControl()
	{
		return new ArrowPadControl();
	}

	@Override
	public void Present(SpriteBatch batch, Control control)
	{
		_presentationService.presentControl(batch, control);
	}

	@Override
	public UserAction GetUserActionIfTouched(Control control, TouchCoords touchCoords)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ReleaseTouch(Control control)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adjustLayoutParams(Control control)
	{
		SystemParams systemParams = _systemParamsService.getSystemParams();
		int tileSize = _deviceService.getTileSize();
		
		control.recalculateControlLayout(systemParams, tileSize);
	}

	@Override
	public void adjustToLostContextOrChangedResolution(Control control)
	{
		control.adjustToLostContextOrChangedResolution();
	}
}
