package inc.bezdelniki.snakegame;

import inc.bezdelniki.snakegame.appsettings.*;
import inc.bezdelniki.snakegame.lyingitem.*;
import inc.bezdelniki.snakegame.snake.*;
import inc.bezdelniki.snakegame.systemparameters.*;

import com.google.inject.*;

public class BindingsConfiguration extends AbstractModule {
	@Override
	protected void configure()
	{
		bind(ISystemParametersService.class).to(SystemParametersService.class).in(Singleton.class);
		bind(IAppSettingsService.class).to(AppSettingsService.class).in(Singleton.class);
		bind(ILyingItemService.class).to(LyingItemService.class).in(Singleton.class);
		bind(ISnakeService.class).to(SnakeService.class).in(Singleton.class);
	}
}

