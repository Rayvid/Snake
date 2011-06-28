package inc.bezdelniki.snake.systemparameters;

import inc.bezdelniki.snake.systemparameters.dto.SystemParameters;

public interface ISystemParametersService {
	SystemParameters GetSystemParameters();
	void NewResolutionWereSet(int width, int height);
}
