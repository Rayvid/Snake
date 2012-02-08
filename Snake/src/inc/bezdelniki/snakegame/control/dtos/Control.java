package inc.bezdelniki.snakegame.control.dtos;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Control
{
	public DeviceCoords coords;
	public int width;
	public int height;
	public TextureRegion noTouchImage;
	public List<TouchableRegion> touchableRegions;
	
	abstract public TextureRegion getPresentationSprite();
	abstract public void adjustToLostContextOrChangedResolution();
	abstract public void recalculateControlLayout();
	abstract public UserAction translateTouchToUserAction(TouchCoords touchCoords);
}
