package inc.bezdelniki.snakegame.control.dtos;

public class TouchableRegion
{
	public TouchableRegion(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int x;
	public int y;
	public int width;
	public int height;
}
