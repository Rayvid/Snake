package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

public class PauseControl extends Control
{
	private ISystemParamsService _systemParamsService;
	private IDeviceService _deviceService;
	private ISpriteService _spriteService;
	private TextureRegion _pauseUnpressed;
	private TextureRegion _pausePressed;
	
	public PauseControl(
			ISystemParamsService systemParamsService,
			IDeviceService deviceService,
			ISpriteService spriteService)
	{
		_systemParamsService = systemParamsService;
		_deviceService = deviceService;
		_spriteService = spriteService;
		adjustToLostContextOrChangedResolution();
	}
	
	@Override
	public TextureRegion getPresentationSprite()
	{
		return _pauseUnpressed;
	}
	
	@Override
	public void adjustToLostContextOrChangedResolution()
	{
		_pauseUnpressed = _spriteService.getPauseButtonUnpressed();
		_pausePressed = _spriteService.getPauseButtonPressed();
		
		recalculateControlLayout();
	}
	
	@Override
	public void recalculateControlLayout()
	{
		SystemParams systemParams = _systemParamsService.getSystemParams();
		int tileSize = _deviceService.getTileSize();
		width = tileSize * 2;
		height = tileSize * 2;
		coords = new DeviceCoords(systemParams.width - width, systemParams.height - height);
		
		touchableRegions = new ArrayList<TouchableRegion>(); 
		touchableRegions.add(new TouchableRegion(1, 1, width - 2, height - 2));
	}

	@Override
	public UserAction translateTouchToUserAction(TouchCoords touchCoords)
	{
		// TODO Auto-generated method stub
		return null;
	}	
}
