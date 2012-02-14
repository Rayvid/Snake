package inc.bezdelniki.snakegame.input;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;

public class InputService implements IInputService
{
	private IRuntimeParamsService _runtimeParamsService;
	private RuntimeParams _runtimeParams;
	
	private boolean _thereWereReleaseBetweenTouches = true;
	
	@Inject
	public InputService(
			IRuntimeParamsService runtimeParamsService,
			RuntimeParams runtimeParams)
	{
		_runtimeParamsService = runtimeParamsService;
		_runtimeParams = runtimeParams;
	}

	@Override
	public boolean isThereTouchInEffect()
	{
		boolean isTouched = false;
		
		if (_thereWereReleaseBetweenTouches)
		{
			isTouched = Gdx.input.isTouched();
		}
		else
		{
			_thereWereReleaseBetweenTouches = !Gdx.input.isTouched();
		}
		
		if (!isTouched)
		{
			_runtimeParamsService.adjustControlsOnRelease(_runtimeParams);
		}
		
		return isTouched;
	}

	@Override
	public TouchCoords GetTouchCoords()
	{
		TouchCoords touchCoords = new TouchCoords(Gdx.input.getX(), Gdx.input.getY());
		
		if (_thereWereReleaseBetweenTouches) // Fresh touch
		{
			_runtimeParamsService.adjustControlsOnTouch(_runtimeParams, touchCoords);
		}
		
		_thereWereReleaseBetweenTouches = false;
		return touchCoords;
	}
}
