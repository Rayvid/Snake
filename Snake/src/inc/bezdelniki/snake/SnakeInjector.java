package inc.bezdelniki.snake;

import com.google.inject.*;

public class SnakeInjector {
	private static com.google.inject.Injector _instance = null;
	
	static
	{	
		_instance = Guice.createInjector(new BindingsConfiguration());
	}
	
	public static com.google.inject.Injector getInstance() {
		return _instance;
	}
}
