package inc.bezdelniki.snakegame.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		Sprite sprite = new Sprite(texture, 0, 0, 16, 16);
		PresenterCoords headCoords = WorldCoordsToPresenterCoords(position);
		sprite.setPosition(headCoords.x, headCoords.y);
		sprite.setSize(getTileSize(), getTileSize());
		sprite.draw(batch);
	}

	@Override
	public PresenterCoords WorldCoordsToPresenterCoords(
			WorldPosition position) {
		PresenterDeltas deltas = getDeltas();
		
		PresenterCoords presenterCoords = new PresenterCoords();
		
		presenterCoords.x = position.tileX * deltas.deltaXForWorldX * getTileSize() + position.tileY * deltas.deltaXForWorldY * getTileSize();
		presenterCoords.y = _systemParametersService.getSystemParameters().height - (position.tileX * deltas.deltaYForWorldX * getTileSize() + position.tileY * deltas.deltaYForWorldY * getTileSize());
		
		return presenterCoords;
	}

	@Override
	public PresenterDeltas getDeltas() {
		SystemParameters systemParameters = _systemParametersService.getSystemParameters();
		PresenterDeltas deltas = new PresenterDeltas();
		
		if (systemParameters.width >= systemParameters.height) {
			deltas.deltaXForWorldX = 1;
			deltas.deltaYForWorldX = 0;
			deltas.deltaXForWorldY = 0;
			deltas.deltaYForWorldY = 1;
		} else {
			deltas.deltaXForWorldX = 0;
			deltas.deltaYForWorldX = 1;
			deltas.deltaXForWorldY = 1;
			deltas.deltaYForWorldY = 0;
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
