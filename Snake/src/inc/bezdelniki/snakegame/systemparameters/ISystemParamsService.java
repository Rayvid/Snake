package inc.bezdelniki.snakegame.systemparameters;

import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public interface ISystemParamsService
{
	SystemParams getSystemParams();
	void newResolutionWereSet(int width, int height);
}
