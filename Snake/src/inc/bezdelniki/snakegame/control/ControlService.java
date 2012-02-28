package inc.bezdelniki.snakegame.control;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.control.dtos.TouchableRegion;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
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
		PauseControl control = new PauseControl();
		control.noTouchImage = _spriteService.getPauseButtonUnpressed();
		adjustLayoutParams(control);
		
		return control;
	}

	@Override
	public ArrowPadControl CreateArrowPadControl()
	{
		ArrowPadControl control = new ArrowPadControl();
		adjustLayoutParams(control);
		
		return control;
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
}
