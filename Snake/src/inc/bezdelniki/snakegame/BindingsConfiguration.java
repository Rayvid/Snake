package inc.bezdelniki.snakegame;

import java.util.ArrayList;

import inc.bezdelniki.snakegame.appsettings.*;
import inc.bezdelniki.snakegame.control.ControlService;
import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.*;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.input.InputService;
import inc.bezdelniki.snakegame.lyingitem.*;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.resources.background.BackgroundService;
import inc.bezdelniki.snakegame.resources.background.IBackgroundService;
import inc.bezdelniki.snakegame.resources.font.FontService;
import inc.bezdelniki.snakegame.resources.font.IFontService;
import inc.bezdelniki.snakegame.resources.font.configuration.FontConfiguration;
import inc.bezdelniki.snakegame.resources.font.configuration.FontConfigurationItem;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.resources.sprite.SpriteService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.score.ScoreService;
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
		RuntimeParams runtimeParams = new RuntimeParams();
		bind(RuntimeParams.class).toInstance(runtimeParams);
		
		FontConfiguration fontConfiguration = new FontConfiguration();
		bind(FontConfiguration.class).toInstance(fontConfiguration);
		
		fontConfiguration.configurationItems = new ArrayList<FontConfigurationItem>();
		FontConfigurationItem fontConfigurationItem1 = new FontConfigurationItem();
		fontConfigurationItem1.tileSizeMin = 0;
		fontConfigurationItem1.smallFontClassPath = "inc/bezdelniki/snakegame/resources/sf12";
		fontConfigurationItem1.regularInfoClassPath = "inc/bezdelniki/snakegame/resources/f12";
		fontConfiguration.configurationItems.add(fontConfigurationItem1);
		FontConfigurationItem fontConfigurationItem2 = new FontConfigurationItem();
		fontConfigurationItem2.tileSizeMin = 13;
		fontConfigurationItem2.smallFontClassPath = "inc/bezdelniki/snakegame/resources/sf16";
		fontConfigurationItem2.regularInfoClassPath = "inc/bezdelniki/snakegame/resources/f16";
		fontConfiguration.configurationItems.add(fontConfigurationItem2);
		
		bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
		bind(IAppSettingsService.class).to(AppSettingsService.class).in(Singleton.class);
		bind(IRuntimeParamsService.class).to(RuntimeParamsService.class).in(Singleton.class);
		bind(IDeviceService.class).to(DeviceService.class).in(Singleton.class);
		bind(ILyingItemService.class).to(LyingItemService.class).in(Singleton.class);
		bind(ISnakeService.class).to(SnakeService.class).in(Singleton.class);
		bind(IUserActionService.class).to(UserActionService.class).in(Singleton.class);
		bind(IScoreService.class).to(ScoreService.class).in(Singleton.class);
		bind(IGameWorldService.class).to(GameWorldService.class).in(Singleton.class);
		bind(IFontService.class).to(FontService.class).in(Singleton.class);
		bind(IBackgroundService.class).to(BackgroundService.class).in(Singleton.class);
		bind(IPresentationService.class).to(PresentationService.class).in(Singleton.class);
		bind(ITimeService.class).to(TimeService.class).in(Singleton.class);
		bind(IInputService.class).to(InputService.class).in(Singleton.class);
		bind(IControlService.class).to(ControlService.class).in(Singleton.class);
		bind(ISpriteService.class).to(SpriteService.class).in(Singleton.class);
	}
}
