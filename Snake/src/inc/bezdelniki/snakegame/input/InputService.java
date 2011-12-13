package inc.bezdelniki.snakegame.input;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;

public class InputService implements IInputService {

	@Override
	public boolean isThereTouchInEffect() {
		return false;
	}

	@Override
	public DeviceCoords GetTouchCoords() {
		return null;
	}

}
