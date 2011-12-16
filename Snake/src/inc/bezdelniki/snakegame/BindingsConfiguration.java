package inc.bezdelniki.snakegame;

import inc.bezdelniki.snakegame.appsettings.*;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.*;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.input.InputService;
import inc.bezdelniki.snakegame.lyingitem.*;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.snake.*;
import inc.bezdelniki.snakegame.systemparameters.*;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.UserActionService;

import com.google.inject.*;

public class BindingsConfiguration extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(ISystemParametersService.class).to(SystemParametersService.class).in(Singleton.class);
		bind(IAppSettingsService.class).to(AppSettingsService.class).in(Singleton.class);
		bind(IDeviceService.class).to(DeviceService.class).in(Singleton.class);
		bind(ILyingItemService.class).to(LyingItemService.class).in(Singleton.class);
		bind(ISnakeService.class).to(SnakeService.class).in(Singleton.class);
		bind(IUserActionService.class).to(UserActionService.class).in(Singleton.class);
		bind(IGameWorldService.class).to(GameWorldService.class).in(Singleton.class);
		bind(IPresentationService.class).to(PresentationService.class).in(Singleton.class);
		bind(ITimeService.class).to(TimeService.class).in(Singleton.class);
		bind(IInputService.class).to(InputService.class).in(Singleton.class);
	}
}
