package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

public class PauseControl extends Control
{
	private IDeviceService _deviceService;
	private ISpriteService _spriteService;
	private TextureRegion _pauseUnpressed;
	private TextureRegion _pausePressed;
	
	public PauseControl(
			IDeviceService deviceService,
			ISpriteService spriteService)
	{
		_deviceService = deviceService;
		_spriteService = spriteService;
		adjustToLostContextOrChangedResolution();
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
		int tileSize = _deviceService.getTileSize();
		coords = new DeviceCoords(0, 0);
		width = tileSize * 2;
		height = tileSize * 2;
		
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
