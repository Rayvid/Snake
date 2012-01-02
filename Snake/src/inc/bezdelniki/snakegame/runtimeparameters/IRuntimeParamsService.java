package inc.bezdelniki.snakegame.runtimeparameters;

import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;

public interface IRuntimeParamsService
{
	RuntimeParams createParamsForNewGame();
	void adjustLayoutParams(RuntimeParams result);
}
