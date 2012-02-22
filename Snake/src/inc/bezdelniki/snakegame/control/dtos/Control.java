package inc.bezdelniki.snakegame.control.dtos;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Control
{
	public DeviceCoords coords;
	public int width;
	public int height;
	public TextureRegion noTouchImage;
	public List<TouchableRegion> touchableRegions;
	public TouchableRegion regionCurrentlyTouched;
	
	abstract public void recalculateControlLayout(SystemParams systemParams, int tileSize);
}
