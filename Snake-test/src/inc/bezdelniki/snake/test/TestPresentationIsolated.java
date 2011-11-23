package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;


import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterCoords;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.SystemParametersService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class TestPresentationIsolated {
	private Injector _testInjectorInstance;
	private IPresentationService _mockedPresentationService;
	
	private class TestPresentationBindingsConfiguration extends AbstractModule {
		@Override
		protected void configure()
		{
			bind(ISystemParametersService.class).to(SystemParametersService.class).in(Singleton.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class).in(Singleton.class);
			bind(ISnakeService.class).to(SnakeService.class).in(Singleton.class);
			bind(IPresentationService.class).toInstance(_mockedPresentationService);
		}
	}
	
	public TestPresentationIsolated()
	{
		_mockedPresentationService = createMock(IPresentationService.class);
		_testInjectorInstance = Guice.createInjector(new TestPresentationBindingsConfiguration());
	}
	
	@Test
	public void testIfDrawSnakeCallsDrawHeadMethod()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		Snake snake = snakeService.createSnake();
		
		SpriteBatch batch = null;
		
		_mockedPresentationService.presentSnakesHead(batch, snake.headPosition);
		replay(_mockedPresentationService);
		
		snakeService.drawSnake(snake, new ArrayList<SnakeMovementChange>(), batch);
		verify(_mockedPresentationService);
	}
	
	@Test
	public void testIfWorldPositionTransformationReturnsMeaningfullResults() throws CloneNotSupportedException
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		IPresentationService presentationService =
			new PresentationService(
					_testInjectorInstance.getInstance(ISystemParametersService.class),
					_testInjectorInstance.getInstance(IAppSettingsService.class));
		Snake snake = snakeService.createSnake();
		
		PresenterCoords headCoords = presentationService.WorldCoordsToPresenterCoords(snake.headPosition);
		
		WorldPosition toTheRight = (WorldPosition)snake.headPosition.clone();
		toTheRight.tileX++;
		PresenterCoords toTheRightCoords = presentationService.WorldCoordsToPresenterCoords(toTheRight);
		
		WorldPosition toTheLeft = (WorldPosition)snake.headPosition.clone();
		toTheRight.tileX--;
		PresenterCoords toTheLeftCoords = presentationService.WorldCoordsToPresenterCoords(toTheLeft);

		WorldPosition up = (WorldPosition)snake.headPosition.clone();
		toTheRight.tileY--;
		PresenterCoords upCoords = presentationService.WorldCoordsToPresenterCoords(up);

		WorldPosition down = (WorldPosition)snake.headPosition.clone();
		toTheRight.tileY++;
		PresenterCoords downCoords = presentationService.WorldCoordsToPresenterCoords(down);
		
		assertTrue(toTheRightCoords.x > headCoords.x && headCoords.x > toTheLeftCoords.x ||
				   toTheRightCoords.x < headCoords.x && headCoords.x < toTheLeftCoords.x);
		assertTrue(upCoords.y > headCoords.y && headCoords.y > downCoords.y ||
				   upCoords.y < headCoords.y && headCoords.y < downCoords.y);

	}
}
