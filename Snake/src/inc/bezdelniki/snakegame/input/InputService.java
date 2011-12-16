package inc.bezdelniki.snakegame.input;

import com.badlogic.gdx.Gdx;

import inc.bezdelniki.snakegame.device.dtos.TouchCoords;

public class InputService implements IInputService
{

	@Override
	public boolean isThereTouchInEffect()
	{
		return Gdx.input.isTouched();
	}

	@Override
	public TouchCoords GetTouchCoords()
	{
		return new TouchCoords(Gdx.input.getX(), Gdx.input.getY());
	}

}
