package inc.bezdelniki.snakegame.controls.dtos;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Control
{
	public int x;
	public int y;
	public int width;
	public int height;
	public TextureRegion noTouchImage;
	public List<TouchableRegion> touchableRegions;
}
