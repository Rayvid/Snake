package inc.bezdelniki.snakegame.runtimeparameters;

import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;

public interface IRuntimeParamsService
{
	void initParamsForNewGame(RuntimeParams _runtimeParams);
	void adjustLayoutParams(RuntimeParams runtimeParams);
}
