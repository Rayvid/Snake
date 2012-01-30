package inc.bezdelniki.snakegame.test.helpers;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.control.ControlService;
import inc.bezdelniki.snakegame.control.IControlService;
import inc.bezdelniki.snakegame.device.DeviceService;
import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.gameworld.GameWorldService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.lyingitem.ILyingItemService;
import inc.bezdelniki.snakegame.lyingitem.LyingItemService;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.resources.font.FontService;
import inc.bezdelniki.snakegame.resources.font.IFontService;
import inc.bezdelniki.snakegame.resources.sprite.ISpriteService;
import inc.bezdelniki.snakegame.resources.sprite.SpriteService;
import inc.bezdelniki.snakegame.runtimeparameters.IRuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.RuntimeParamsService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.score.ScoreService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.SystemParamsService;
import inc.bezdelniki.snakegame.time.ITimeService;
import inc.bezdelniki.snakegame.time.TimeService;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.UserActionService;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class BindingsConfiguration<T> extends AbstractModule
{
	private List<Class<?>> _doNotBindList;
	private Class<T> _bindClass;
	private T _toInstance;
	
	public BindingsConfiguration(
			Class<T> bindClass,
	        T toInstance,
	        Class<?>... doNotBindList)
	{
		_bindClass = bindClass;
		_toInstance = toInstance;
		
		_doNotBindList = new ArrayList<Class<?>>();
		for (Class<?> doNotBind : doNotBindList)
		{
			_doNotBindList.add(doNotBind);
		}
	}
	
	public BindingsConfiguration(Class<?>... doNotBindList)
	{
		this(null, null, doNotBindList);
	}
	
	@Override
	protected void configure()
	{
		RuntimeParams runtimeParams = new RuntimeParams();
		bind(RuntimeParams.class).toInstance(runtimeParams);
		
		if (!_doNotBindList.contains(ISystemParamsService.class)) bind(ISystemParamsService.class).to(SystemParamsService.class).in(Singleton.class);
		if (!_doNotBindList.contains(IAppSettingsService.class)) bind(IAppSettingsService.class).to(AppSettingsService.class);
		if (!_doNotBindList.contains(IRuntimeParamsService.class)) bind(IRuntimeParamsService.class).to(RuntimeParamsService.class);
		if (!_doNotBindList.contains(IDeviceService.class)) bind(IDeviceService.class).to(DeviceService.class);
		if (!_doNotBindList.contains(ILyingItemService.class)) bind(ILyingItemService.class).to(LyingItemService.class);
		if (!_doNotBindList.contains(ISnakeService.class)) bind(ISnakeService.class).to(SnakeService.class);
		if (!_doNotBindList.contains(IUserActionService.class)) bind(IUserActionService.class).to(UserActionService.class);
		if (!_doNotBindList.contains(IScoreService.class)) bind(IScoreService.class).to(ScoreService.class);
		if (!_doNotBindList.contains(IGameWorldService.class)) bind(IGameWorldService.class).to(GameWorldService.class);
		if (!_doNotBindList.contains(IFontService.class)) bind(IFontService.class).to(FontService.class).in(Singleton.class);
		if (!_doNotBindList.contains(IPresentationService.class)) bind(IPresentationService.class).to(PresentationService.class).in(Singleton.class);
		if (!_doNotBindList.contains(ITimeService.class)) bind(ITimeService.class).to(TimeService.class);
		if (!_doNotBindList.contains(IControlService.class)) bind(IControlService.class).to(ControlService.class);
		if (!_doNotBindList.contains(ISpriteService.class)) bind(ISpriteService.class).to(SpriteService.class);
		
		if (_bindClass != null)	bind(_bindClass).toInstance(_toInstance);
	}	
}