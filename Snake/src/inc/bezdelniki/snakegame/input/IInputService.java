package inc.bezdelniki.snakegame.input;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;

public interface IInputService {
	boolean isThereTouchInEffect();
	DeviceCoords GetTouchCoords();
}
