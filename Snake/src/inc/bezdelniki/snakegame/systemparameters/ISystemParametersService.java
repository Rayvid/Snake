package inc.bezdelniki.snakegame.systemparameters;

import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

public interface ISystemParametersService
{
	SystemParameters getSystemParameters();

	void newResolutionWereSet(int width, int height);
}
