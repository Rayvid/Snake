package inc.bezdelniki.snakegame.appsettings.dtos;

import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterCoords;

public class AppSettings {
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
	
	public PresenterCoords topLeft;
}
