package inc.bezdelniki.snake.systemparameters;

import inc.bezdelniki.snake.systemparameters.dto.SystemParameters;


public class SystemParametersService implements ISystemParametersService {
	private int _width = -1;
	private int _height = -1;
	
	public SystemParameters GetSystemParameters() {
		SystemParameters systemParameters = new SystemParameters();
		return systemParameters;
	}
	
	public void NewResolutionWereSet(int width, int height) {
		_width = width;
		_height = height; 
	}
}
