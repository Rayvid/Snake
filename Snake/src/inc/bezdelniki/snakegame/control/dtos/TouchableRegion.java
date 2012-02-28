package inc.bezdelniki.snakegame.control.dtos;

import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

public class TouchableRegion
{
	public TouchableRegion(int x, int y, int width, int height, UserAction userActionWhenTouched)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.userActionWhenTouched = userActionWhenTouched;
	}
	
	public int x;
	public int y;
	public int width;
	public int height;
	public UserAction userActionWhenTouched;
}
