package inc.bezdelniki.snakegame.presentation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterCoords;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;

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
			WorldPosition headPosition) {
		// TODO Auto-generated method stub
		return null;
	}
}
