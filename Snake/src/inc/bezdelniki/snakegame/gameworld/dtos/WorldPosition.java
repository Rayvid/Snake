package inc.bezdelniki.snakegame.gameworld.dtos;

public class WorldPosition implements Cloneable {
	public int tileX;
	public int tileY;
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	@Override
	public boolean equals(Object worldPositionCompareTo) {
		WorldPosition compareTo = (WorldPosition)worldPositionCompareTo;
		return this == compareTo || this.tileX == compareTo.tileX && this.tileY == compareTo.tileY;
	}
}
