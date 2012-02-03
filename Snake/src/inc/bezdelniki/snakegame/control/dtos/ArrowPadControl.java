package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

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
	public void recalculateControlLayout()
	{
		int tileSize = _deviceService.getTileSize();
		coords = new DeviceCoords(0, 0);
		width = tileSize * 4;
		height = tileSize * 4;
		
		touchableRegions = new ArrayList<TouchableRegion>(); 
		touchableRegions.add(new TouchableRegion(0, tileSize, tileSize * 2, tileSize * 2));
		touchableRegions.add(new TouchableRegion(tileSize, 0, tileSize * 2, tileSize * 2));
		touchableRegions.add(new TouchableRegion(tileSize * 2, tileSize, tileSize * 2, tileSize * 2));
		touchableRegions.add(new TouchableRegion(tileSize, tileSize * 2, tileSize * 2, tileSize * 2));
	}

	@Override
	public UserAction translateTouchToUserAction(TouchCoords touchCoords)
	{
		// TODO Auto-generated method stub
		return null;
	}

}