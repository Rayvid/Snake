package inc.bezdelniki.snakegame.systemparameters;

import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

public interface ISystemParametersService {
	SystemParameters GetSystemParameters();
	void NewResolutionWereSet(int width, int height);
}