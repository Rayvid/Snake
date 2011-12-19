package inc.bezdelniki.snakegame.input;

import com.badlogic.gdx.Gdx;

import inc.bezdelniki.snakegame.device.dtos.TouchCoords;

public class InputService implements IInputService
{
	private boolean _thereWereReleaseBetweenTouches = true;

	@Override
	public boolean isThereTouchInEffect()
	{
		if (_thereWereReleaseBetweenTouches)
		{
			return Gdx.input.isTouched();
		}
		else
		{
			_thereWereReleaseBetweenTouches = !Gdx.input.isTouched();
			return false;
		}
	}

	@Override
	public TouchCoords GetTouchCoords()
	{
		_thereWereReleaseBetweenTouches = false;
		return new TouchCoords(Gdx.input.getX(), Gdx.input.getY());
	}
}
