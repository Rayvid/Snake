package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public class ArrowPadControl extends Control
{
	@Override
	public void adjustToLostContextOrChangedResolution()
	{
	}

	@Override
	public void recalculateControlLayout(SystemParams systemParams, int tileSize)
	{
		coords = new DeviceCoords(0, 0);
		width = tileSize * 4;
		height = tileSize * 4;
		
		touchableRegions = new ArrayList<TouchableRegion>(); 
		touchableRegions.add(new TouchableRegion(0, (int) (tileSize * 1.5) + 1, (int) (tileSize * 1.5) + 1, tileSize));
		touchableRegions.add(new TouchableRegion((int) (tileSize * 1.5) + 1, 0, tileSize, (int) (tileSize * 1.5) + 1));
		touchableRegions.add(new TouchableRegion((int) (tileSize * 2.5) + 1, (int) (tileSize * 1.5) + 1, (int) (tileSize * 1.5) + 1, tileSize));
		touchableRegions.add(new TouchableRegion((int) (tileSize * 1.5) + 1, (int) (tileSize * 2.5) + 1, tileSize, (int) (tileSize * 1.5) + 1));
	}
}
