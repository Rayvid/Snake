package inc.bezdelniki.snake;

import inc.bezdelniki.snake.systemparameters.ISystemParametersService;

import com.google.inject.*;

public class GameWorld {
	private ISystemParametersService _systemParameters;
	
	@Inject
	GameWorld (ISystemParametersService systemParameters)
	{
		_systemParameters = systemParameters;
	}
}
