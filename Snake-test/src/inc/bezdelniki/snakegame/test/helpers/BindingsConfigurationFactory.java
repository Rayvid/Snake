package inc.bezdelniki.snakegame.test.helpers;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class BindingsConfigurationFactory
{	
	public static AbstractModule BuildDefaultBindingsConfiguration(Class<?>... doNotBindList)
	{
		return new BindingsConfiguration<Object>(doNotBindList);
	}
	
	public static <T> Module BuildDefaultBindingsConfiguration(
			Class<T> bindClass,
	        T toInstance,
	        Class<?>... doNotBindList)
	{
		return new BindingsConfiguration<T>(bindClass, toInstance, doNotBindList);
	}
}
