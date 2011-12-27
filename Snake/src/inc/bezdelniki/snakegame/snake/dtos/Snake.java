package inc.bezdelniki.snakegame.snake.dtos;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;

public class Snake implements Cloneable
{
	public WorldPosition headPosition;
	public int currLength;
	public int newLength;
	public Direction direction;
	
	@Override
	public Object clone()
	{
		Snake result;
		try
		{
			result = (Snake) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
		
		result.headPosition = (WorldPosition) headPosition.clone();
		
		return result;
	}
}
