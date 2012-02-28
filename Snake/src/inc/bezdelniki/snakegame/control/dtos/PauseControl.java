package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.useraction.dtos.PauseAction;

public class PauseControl extends Control
{
	@Override
	public void recalculateControlLayout(SystemParams systemParams, int tileSize)
	{
		width = tileSize * 2;
		height = tileSize * 2;
		coords = new DeviceCoords(systemParams.width - width, systemParams.height - height);
		
		touchableRegions = new ArrayList<TouchableRegion>(); 
		touchableRegions.add(new TouchableRegion(1, 1, width - 2, height - 2, new PauseAction()));
	}
}
