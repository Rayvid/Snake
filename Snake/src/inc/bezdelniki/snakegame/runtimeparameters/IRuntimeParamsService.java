package inc.bezdelniki.snakegame.runtimeparameters;

import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;

public interface IRuntimeParamsService
{
	void initParamsForNewGame(RuntimeParams runtimeParams);
	void adjustLayoutParams(RuntimeParams runtimeParams);
	void adjustControlsOnTouch(RuntimeParams runtimeParams, TouchCoords touchCoords);
	void adjustControlsOnRelease(RuntimeParams runtimeParams);
}
