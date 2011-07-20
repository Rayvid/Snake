package inc.bezdelniki.snakegame.model.dtos;

public class WorldPosition implements Cloneable {
	public int tileX;
	public int tileY;
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
