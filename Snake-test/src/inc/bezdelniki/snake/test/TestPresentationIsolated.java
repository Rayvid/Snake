package inc.bezdelniki.snake.test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;


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
		toTheLeft.tileX--;
		PresenterCoords toTheLeftCoords = presentationService.WorldCoordsToPresenterCoords(toTheLeft);

		WorldPosition up = (WorldPosition)snake.headPosition.clone();
		up.tileY--;
		PresenterCoords upCoords = presentationService.WorldCoordsToPresenterCoords(up);

		WorldPosition down = (WorldPosition)snake.headPosition.clone();
		down.tileY++;
		PresenterCoords downCoords = presentationService.WorldCoordsToPresenterCoords(down);
		
		int tileSize = presentationService.getTileSize();
		
		assertTrue(toTheRightCoords.x - headCoords.x == tileSize && headCoords.x - toTheLeftCoords.x == tileSize ||
				   toTheRightCoords.x - headCoords.x == -tileSize && headCoords.x - toTheLeftCoords.x == -tileSize);
		assertTrue(upCoords.y - headCoords.y == tileSize && headCoords.y - downCoords.y == tileSize ||
				   upCoords.y - headCoords.y == -tileSize && headCoords.y - downCoords.y == -tileSize);

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
		PresenterDeltas deltas = presentationService.getMovementDeltas();
		assertTrue(
				deltas.deltaYForRightMovement == 0 && deltas.deltaXForRightMovement != 0 &&
				deltas.deltaXForDownMovement == 0 && deltas.deltaYForDownMovement != 0);
		
		systemParametersService.newResolutionWereSet(320, 480);
		deltas = presentationService.getMovementDeltas();
		assertTrue(
				deltas.deltaYForRightMovement != 0 && deltas.deltaXForRightMovement == 0 &&
				deltas.deltaXForDownMovement != 0 && deltas.deltaYForDownMovement == 0);
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
}
