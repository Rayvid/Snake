package inc.bezdelniki.snakegame.input;

import com.badlogic.gdx.Gdx;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;

public class InputService implements IInputService {

	@Override
	public boolean isThereTouchInEffect() {
		return Gdx.input.isTouched();
	}

	@Override
	public DeviceCoords GetTouchCoords() {
		return new DeviceCoords(Gdx.input.getX(), Gdx.input.getY());
	}

}
