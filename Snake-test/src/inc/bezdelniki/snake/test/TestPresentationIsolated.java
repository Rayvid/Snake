package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import inc.bezdelniki.snakegame.appsettings.AppSettingsService;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.presentation.PresentationService;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterCoords;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterDeltas;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.SnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.SystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestPresentationIsolated {
	private Injector _testInjectorInstance;
	private IPresentationService _mockedPresentationService;
	
	private class TestPresentationBindingsConfiguration extends AbstractModule {
		@Override
		protected void configure()
		{
			bind(ISystemParametersService.class).to(SystemParametersService.class);
			bind(IAppSettingsService.class).to(AppSettingsService.class);
			bind(ISnakeService.class).to(SnakeService.class);
			
			bind(IPresentationService.class).toInstance(_mockedPresentationService);
		}
	}
	
	public TestPresentationIsolated()
	{
		_mockedPresentationService = createNiceMock(IPresentationService.class);
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
		ISystemParametersService systemParametersService = _testInjectorInstance.getInstance(ISystemParametersService.class);
		IPresentationService presentationService =
			new PresentationService(
					systemParametersService,
					_testInjectorInstance.getInstance(IAppSettingsService.class));
		
		Snake snake = snakeService.createSnake();
		WorldPosition toTheRight = (WorldPosition)snake.headPosition.clone();
		toTheRight.tileX++;
		WorldPosition toTheLeft = (WorldPosition)snake.headPosition.clone();
		toTheLeft.tileX--;
		WorldPosition up = (WorldPosition)snake.headPosition.clone();
		up.tileY--;
		WorldPosition down = (WorldPosition)snake.headPosition.clone();
		down.tileY++;

		systemParametersService.newResolutionWereSet(400, 200);
		SystemParameters systemParameters = systemParametersService.getSystemParameters();
		
		PresenterCoords headCoords = presentationService.WorldCoordsToPresenterCoords(snake.headPosition);
		PresenterCoords toTheRightCoords = presentationService.WorldCoordsToPresenterCoords(toTheRight);		
		PresenterCoords toTheLeftCoords = presentationService.WorldCoordsToPresenterCoords(toTheLeft);
		PresenterCoords upCoords = presentationService.WorldCoordsToPresenterCoords(up);
		PresenterCoords downCoords = presentationService.WorldCoordsToPresenterCoords(down);
		
		int tileSize = presentationService.getTileSize();
		
		assertTrue(toTheRightCoords.x > -systemParameters.width && toTheRightCoords.x < systemParameters.width &&
				   headCoords.x > -systemParameters.width && headCoords.x < systemParameters.width &&
				   toTheLeftCoords.x > -systemParameters.width && toTheLeftCoords.x < systemParameters.width &&
				   toTheRightCoords.y > -systemParameters.height && toTheRightCoords.y < systemParameters.height &&
				   headCoords.y > -systemParameters.height && headCoords.y < systemParameters.height &&
				   toTheLeftCoords.y > -systemParameters.height && toTheLeftCoords.y < systemParameters.height &&
				   (Math.abs(toTheRightCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - toTheLeftCoords.x) == tileSize ||
				    Math.abs(toTheRightCoords.y - headCoords.y) == tileSize && Math.abs(headCoords.y - toTheLeftCoords.y) == tileSize));
		assertTrue(upCoords.x > -systemParameters.width && upCoords.x < systemParameters.width &&
				   headCoords.x > -systemParameters.width && headCoords.x < systemParameters.width &&
				   downCoords.x > -systemParameters.width && downCoords.x < systemParameters.width &&
				   upCoords.y > -systemParameters.height && upCoords.y < systemParameters.height &&
				   headCoords.y > -systemParameters.height && headCoords.y < systemParameters.height &&
				   downCoords.y > -systemParameters.height && downCoords.y < systemParameters.height &&
				   (Math.abs(upCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - downCoords.x) == tileSize ||
				    Math.abs(upCoords.y - headCoords.y) == tileSize && Math.abs(headCoords.y - downCoords.y) == tileSize));
		
		systemParametersService.newResolutionWereSet(200, 400);
		systemParameters = systemParametersService.getSystemParameters();
		
		headCoords = presentationService.WorldCoordsToPresenterCoords(snake.headPosition);
		toTheRightCoords = presentationService.WorldCoordsToPresenterCoords(toTheRight);		
		toTheLeftCoords = presentationService.WorldCoordsToPresenterCoords(toTheLeft);
		upCoords = presentationService.WorldCoordsToPresenterCoords(up);
		downCoords = presentationService.WorldCoordsToPresenterCoords(down);
		
		tileSize = presentationService.getTileSize();
		
		assertTrue(toTheRightCoords.x > -systemParameters.width && toTheRightCoords.x < systemParameters.width &&
				   headCoords.x > -systemParameters.width && headCoords.x < systemParameters.width &&
				   toTheLeftCoords.x > -systemParameters.width && toTheLeftCoords.x < systemParameters.width &&
				   toTheRightCoords.y > -systemParameters.height && toTheRightCoords.y < systemParameters.height &&
				   headCoords.y > -systemParameters.height && headCoords.y < systemParameters.height &&
				   toTheLeftCoords.y > -systemParameters.height && toTheLeftCoords.y < systemParameters.height &&
				   (Math.abs(toTheRightCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - toTheLeftCoords.x) == tileSize ||
				    Math.abs(toTheRightCoords.y - headCoords.y) == tileSize && Math.abs(headCoords.y - toTheLeftCoords.y) == tileSize));
		assertTrue(upCoords.x > -systemParameters.width && upCoords.x < systemParameters.width &&
				   headCoords.x > -systemParameters.width && headCoords.x < systemParameters.width &&
				   downCoords.x > -systemParameters.width && downCoords.x < systemParameters.width &&
				   upCoords.y > -systemParameters.height && upCoords.y < systemParameters.height &&
				   headCoords.y > -systemParameters.height && headCoords.y < systemParameters.height &&
				   downCoords.y > -systemParameters.height && downCoords.y < systemParameters.height &&
				   (Math.abs(upCoords.x - headCoords.x) == tileSize && Math.abs(headCoords.x - downCoords.x) == tileSize ||
				    Math.abs(upCoords.y - headCoords.y) == tileSize && Math.abs(headCoords.y - downCoords.y) == tileSize));
	}
	
	@Test
	public void testIfBiggerDimensionHasBeenChoosenAsHorizontal()
	{
		ISystemParametersService systemParametersService = _testInjectorInstance.getInstance(ISystemParametersService.class);
		IPresentationService presentationService =
			new PresentationService(
					systemParametersService,
					_testInjectorInstance.getInstance(IAppSettingsService.class));
		
		systemParametersService.newResolutionWereSet(480, 320);
		PresenterDeltas deltas = presentationService.getDeltas();
		assertTrue(
				deltas.deltaYForWorldX == 0 && deltas.deltaXForWorldX != 0 &&
				deltas.deltaXForWorldY == 0 && deltas.deltaYForWorldY != 0);
		
		systemParametersService.newResolutionWereSet(320, 480);
		deltas = presentationService.getDeltas();
		assertTrue(
				deltas.deltaYForWorldX != 0 && deltas.deltaXForWorldX == 0 &&
				deltas.deltaXForWorldY != 0 && deltas.deltaYForWorldY == 0);
	}
	
	@Test
	public void testIfTileSizeIsOptimal()
	{
		ISystemParametersService systemParametersService = _testInjectorInstance.getInstance(ISystemParametersService.class);
		IAppSettingsService appSettingsService = _testInjectorInstance.getInstance(IAppSettingsService.class);
		IPresentationService presentationService =
			new PresentationService(
					systemParametersService,
					appSettingsService);
		
		int tileSize = presentationService.getTileSize();
		SystemParameters systemParameters = systemParametersService.getSystemParameters();
		AppSettings appSettings = appSettingsService.getAppSettings();
		
		assertTrue(
				systemParameters.height - tileSize * appSettings.tilesHorizontally < tileSize &&
				systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0 &&
				systemParameters.width - tileSize * appSettings.tilesVertically >= 0 ||
				systemParameters.width - tileSize * appSettings.tilesHorizontally < tileSize &&
				systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0 &&
				systemParameters.height - tileSize * appSettings.tilesVertically >= 0 ||
				systemParameters.height - tileSize * appSettings.tilesVertically < tileSize &&
				systemParameters.height - tileSize * appSettings.tilesVertically >= 0 &&
				systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0 ||
				systemParameters.width - tileSize * appSettings.tilesVertically < tileSize &&
				systemParameters.width - tileSize * appSettings.tilesVertically >= 0 &&
				systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0);
		
		systemParametersService.newResolutionWereSet(200, 100);
		tileSize = presentationService.getTileSize();
		systemParameters = systemParametersService.getSystemParameters();
		appSettings = appSettingsService.getAppSettings();

		assertTrue(
				systemParameters.height - tileSize * appSettings.tilesHorizontally < tileSize &&
				systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0 &&
				systemParameters.width - tileSize * appSettings.tilesVertically >= 0 ||
				systemParameters.width - tileSize * appSettings.tilesHorizontally < tileSize &&
				systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0 &&
				systemParameters.height - tileSize * appSettings.tilesVertically >= 0 ||
				systemParameters.height - tileSize * appSettings.tilesVertically < tileSize &&
				systemParameters.height - tileSize * appSettings.tilesVertically >= 0 &&
				systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0 ||
				systemParameters.width - tileSize * appSettings.tilesVertically < tileSize &&
				systemParameters.width - tileSize * appSettings.tilesVertically >= 0 &&
				systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0);
		
		systemParametersService.newResolutionWereSet(1000, 2000);
		tileSize = presentationService.getTileSize();
		systemParameters = systemParametersService.getSystemParameters();
		appSettings = appSettingsService.getAppSettings();

		assertTrue(
				systemParameters.height - tileSize * appSettings.tilesHorizontally < tileSize &&
				systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0 &&
				systemParameters.width - tileSize * appSettings.tilesVertically >= 0 ||
				systemParameters.width - tileSize * appSettings.tilesHorizontally < tileSize &&
				systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0 &&
				systemParameters.height - tileSize * appSettings.tilesVertically >= 0 ||
				systemParameters.height - tileSize * appSettings.tilesVertically < tileSize &&
				systemParameters.height - tileSize * appSettings.tilesVertically >= 0 &&
				systemParameters.width - tileSize * appSettings.tilesHorizontally >= 0 ||
				systemParameters.width - tileSize * appSettings.tilesVertically < tileSize &&
				systemParameters.width - tileSize * appSettings.tilesVertically >= 0 &&
				systemParameters.height - tileSize * appSettings.tilesHorizontally >= 0);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testIfDrawSnakeCallsDrawBodyMethod()
	{
		ISnakeService snakeService = _testInjectorInstance.getInstance(ISnakeService.class);
		Snake snake = snakeService.createSnake();
		
		SpriteBatch batch = null;

        _mockedPresentationService.presentSnakesBody(eq(batch), isA(List.class), eq(snake.headPosition));
		replay(_mockedPresentationService);
		
		snakeService.drawSnake(snake, new ArrayList<SnakeMovementChange>(), batch);
		verify(_mockedPresentationService);
	}
}
