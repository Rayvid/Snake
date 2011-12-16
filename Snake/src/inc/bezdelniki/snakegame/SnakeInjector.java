package inc.bezdelniki.snakegame;

import com.google.inject.*;

public class SnakeInjector
{
	private static Injector _instance = null;

	static
	{
		_instance = Guice.createInjector(new BindingsConfiguration());
	}

	public static Injector getInjectorInstance()
	{
		return _instance;
	}
}
