package inc.bezdelniki.snake;

import com.google.inject.*;

public class SnakeInjector {
	private static com.google.inject.Injector _instance = null;
	
	public static com.google.inject.Injector getInstance() {
		return _instance;
	}
	
	public static void configure()
	{
		if (_instance != null)
		{
			return;
		}
		
		_instance = Guice.createInjector(new BindingsConfiguration());
	}
}
