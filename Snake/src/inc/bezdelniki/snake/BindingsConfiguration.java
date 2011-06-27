package inc.bezdelniki.snake;

import com.google.inject.*;

public class BindingsConfiguration extends AbstractModule {
	@Override
	protected void configure()
	{
		bind(ISystemParameters.class).to(SystemParameters.class).in(Singleton.class);
	}
}

