package inc.bezdelniki.snakegame.gameworld.dtos;

public class WorldPosition implements Cloneable
{
	public WorldPosition(int tileX, int tileY)
	{
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public int tileX;
	public int tileY;

	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}

	@Override
	public boolean equals(Object worldPositionCompareTo)
	{
		WorldPosition compareTo = (WorldPosition) worldPositionCompareTo;
		return this == compareTo || this.tileX == compareTo.tileX && this.tileY == compareTo.tileY;
	}
}
