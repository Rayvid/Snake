package inc.bezdelniki.snakegame.control.dtos;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Control
{
	public int x;
	public int y;
	public int width;
	public int height;
	public TextureRegion noTouchImage;
	public List<TouchableRegion> touchableRegions;
	
	abstract public void adjustToLostContextOrChangedResolution();
	abstract public void recalculateControlLayout();
}
