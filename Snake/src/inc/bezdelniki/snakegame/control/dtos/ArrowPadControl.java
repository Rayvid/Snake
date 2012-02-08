package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

public class ArrowPadControl extends Control
{
	IDeviceService _deviceService;
	
	public ArrowPadControl(IDeviceService deviceService)
	{
		_deviceService = deviceService;
		recalculateControlLayout();
	}

	@Override
	public void adjustToLostContextOrChangedResolution()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public TextureRegion getPresentationSprite()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recalculateControlLayout()
	{
		int tileSize = _deviceService.getTileSize();
		coords = new DeviceCoords(0, 0);
		width = tileSize * 4;
		height = tileSize * 4;
		
		touchableRegions = new ArrayList<TouchableRegion>(); 
		touchableRegions.add(new TouchableRegion(0, (int) (tileSize * 1.5) + 1, (int) (tileSize * 1.5) + 1, tileSize));
		touchableRegions.add(new TouchableRegion((int) (tileSize * 1.5) + 1, 0, tileSize, (int) (tileSize * 1.5) + 1));
		touchableRegions.add(new TouchableRegion((int) (tileSize * 2.5) + 1, (int) (tileSize * 1.5) + 1, (int) (tileSize * 1.5) + 1, tileSize));
		touchableRegions.add(new TouchableRegion((int) (tileSize * 1.5) + 1, (int) (tileSize * 2.5) + 1, tileSize, (int) (tileSize * 1.5) + 1));
	}

	@Override
	public UserAction translateTouchToUserAction(TouchCoords touchCoords)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
