package inc.bezdelniki.snakegame.control.dtos;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

public class TouchableRegion
{
	public TouchableRegion(
			int x,
			int y,
			int width,
			int height,
			UserAction userActionWhenTouched,
			TextureRegion imageWhenTouched)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.userActionWhenTouched = userActionWhenTouched;
		this.imageWhenTouched = imageWhenTouched;
	}
	
	public int x;
	public int y;
	public int width;
	public int height;
	public UserAction userActionWhenTouched;
	public TextureRegion imageWhenTouched;
}
