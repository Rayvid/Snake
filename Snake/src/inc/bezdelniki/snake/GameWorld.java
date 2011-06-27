package inc.bezdelniki.snake;

import com.google.inject.*;

public class GameWorld {
	private ISystemParameters _systemParameters;
	
	@Inject
	GameWorld (ISystemParameters systemParameters)
	{
		_systemParameters = systemParameters;
	}
}
