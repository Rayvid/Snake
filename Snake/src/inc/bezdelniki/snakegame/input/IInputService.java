package inc.bezdelniki.snakegame.input;

import inc.bezdelniki.snakegame.device.dtos.TouchCoords;

public interface IInputService
{
	boolean isThereTouchInEffect();
	TouchCoords GetTouchCoords();
}
