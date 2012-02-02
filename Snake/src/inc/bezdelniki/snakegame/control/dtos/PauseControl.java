package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

import inc.bezdelniki.snakegame.device.IDeviceService;

public class PauseControl extends Control
{
	private IDeviceService _deviceService;
	
	public PauseControl(IDeviceService deviceService)
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
		x = 0;
		y = 0;
		width = tileSize * 2;
		height = tileSize * 2;
		
		touchableRegions = new ArrayList<TouchableRegion>(); 
		touchableRegions.add(new TouchableRegion(1, 1, width - 2, height - 2));
	}
}
