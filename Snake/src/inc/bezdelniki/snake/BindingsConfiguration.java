package inc.bezdelniki.snake;

import inc.bezdelniki.snake.appsettings.*;
import inc.bezdelniki.snake.systemparameters.ISystemParametersService;
import inc.bezdelniki.snake.systemparameters.SystemParametersService;

import com.google.inject.*;

public class BindingsConfiguration extends AbstractModule {
	@Override
	protected void configure()
	{
		bind(ISystemParametersService.class).to(SystemParametersService.class).in(Singleton.class);
		bind(IAppSettingsService.class).to(AppSettingsService.class).in(Singleton.class);
	}
}

