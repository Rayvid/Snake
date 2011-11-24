package inc.bezdelniki.snakegame.presentation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterCoords;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterDeltas;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParameters;

public class PresentationService implements IPresentationService {
	private ISystemParametersService _systemParametersService;
	private IAppSettingsService _appSettingsService;
	
	@Inject
	public PresentationService(
			ISystemParametersService systemParametersService,
			IAppSettingsService appSettingsService)	{
		_systemParametersService = systemParametersService;
		_appSettingsService = appSettingsService;
	}

	@Override
	public void presentSnakesHead(SpriteBatch batch, WorldPosition position) {
		//Texture texture = new Texture(Gdx.files.internal("assets/SnakeSkin.png"));
		//Sprite sprite = new Sprite(texture, 0, 0, 32, 32);
		//sprite.setPosition(0, 0);
		//sprite.setRotation(90);
		//sprite.draw(batch);
	}

	@Override
	public PresenterCoords WorldCoordsToPresenterCoords(
			WorldPosition position) {
		AppSettings appSettings = _appSettingsService.getAppSettings();
		PresenterCoords presenterCoords = new PresenterCoords();
		
		presenterCoords.x = appSettings.topLeft.x + position.tileX * getTileSize();
		presenterCoords.y = appSettings.topLeft.y + position.tileY * getTileSize();
		
		return presenterCoords;
	}

	@Override
	public PresenterDeltas getMovementDeltas() {
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();
		PresenterDeltas deltas = new PresenterDeltas();
		
		if (systemParameters.width >= systemParameters.height) {
			deltas.deltaXForRightMovement = 1;
			deltas.deltaYForRightMovement = 0;
			deltas.deltaXForDownMovement = 0;
			deltas.deltaYForDownMovement = 1;
		} else {
			deltas.deltaXForRightMovement = 0;
			deltas.deltaYForRightMovement = 1;
			deltas.deltaXForDownMovement = 1;
			deltas.deltaYForDownMovement = 0;
		}
		
		return deltas;
	}

	@Override
	public int getTileSize() {
		AppSettings appSettings = _appSettingsService.getAppSettings();
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();
		
		return Math.min(
				Math.max(systemParameters.height, systemParameters.width) / appSettings.tilesHorizontally,
				Math.min(systemParameters.height, systemParameters.width) / appSettings.tilesVertically);
	}
}
