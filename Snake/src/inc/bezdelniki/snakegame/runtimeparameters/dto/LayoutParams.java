package inc.bezdelniki.snakegame.runtimeparameters.dto;

import java.util.ArrayList;
import java.util.List;

import inc.bezdelniki.snakegame.controls.dtos.Control;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;

public class LayoutParams
{
	public LayoutParams()
	{
		controls = new ArrayList<Control>();
	}
	
	public int gameBoxPaddingTop;
	public int gameBoxPaddingLeft;
	public int gameBoxPaddingBottom;
	public int gameBoxPaddingRight;
	public DeviceCoords scoreCoords;
	public DeviceCoords fpsCoords;
	public List<Control> controls;
}
