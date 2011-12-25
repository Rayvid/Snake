package inc.bezdelniki.snakegame.runtimeparameters;

import inc.bezdelniki.snakegame.runtimeparameters.dto.RenderingParams;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;

public class RuntimeParamsService implements IRuntimeParamsService
{
	@Override
	public RuntimeParams initParamsForNewGame()
	{
		RuntimeParams result = new RuntimeParams();
		
		result.renderingParams = new RenderingParams();
		result.renderingParams.gameBoxPaddingTop = 80;
		result.renderingParams.gameBoxPaddingLeft = 20;
		result.renderingParams.gameBoxPaddingRight = 20;
		result.renderingParams.gameBoxPaddingBottom = 20;
		
		result.snakesMovementNanoInterval = 1000000000; // 1s

		return result;
	}

}
