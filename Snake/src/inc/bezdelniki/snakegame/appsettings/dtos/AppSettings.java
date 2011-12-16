package inc.bezdelniki.snakegame.appsettings.dtos;

import inc.bezdelniki.snakegame.model.enums.Direction;

public class AppSettings
{
	// TODO Move stuff from there to RuntimeParameters as soon as it becomes
	// dynamic

	public int initialWidth;
	public int initialHeight;

	public int initialSnakeLength;
	public int initialHeadPositionX;
	public int initialHeadPositionY;
	public Direction initialDirection;

	public int tilesHorizontally;
	public int tilesVertically;
	public int textureSize;

	public int growSnakeBy;

	public long snakesMovementNanoInterval;
}
