package inc.bezdelniki.snakegame.presentation;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.resources.background.dtos.Background;
import inc.bezdelniki.snakegame.resources.font.IFontService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;

public class PresentationService implements IPresentationService
{
	private IDeviceService _deviceService;
	private IFontService _fontService;
	private ISystemParamsService _systemParamsService;
	private RuntimeParams _runtimeParams;

	private BitmapFont _smallFont = null;
	private BitmapFont _regularInfoFont = null;
	private Texture _mainObjectsTexture = null;
	private TextureRegion _snakesHeadSprite = null;
	private TextureRegion _snakesBodySprite = null;
	private TextureRegion _appleSprite = null;

	@Inject
	public PresentationService(
			ISystemParamsService systemParamsService,
			IDeviceService deviceService,
			IFontService fontService,
			RuntimeParams runtimeParams)
	{
		_systemParamsService = systemParamsService;
		_deviceService = deviceService;
		_fontService = fontService;
		_runtimeParams = runtimeParams;
	}

	private void initGdxResources()
	{
		if (_smallFont == null)
		{
			_smallFont = _fontService.getSmallFont();
		}
		if (_regularInfoFont == null)
		{
			_regularInfoFont = _fontService.getRegularInfoFont();
		}

		if (_mainObjectsTexture == null)
		{
			_mainObjectsTexture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		}

		if (_snakesHeadSprite == null)
		{
			_snakesHeadSprite = new TextureRegion(_mainObjectsTexture, 0, 0, 16, 16);
		}
		if (_snakesBodySprite == null)
		{
			_snakesBodySprite = new TextureRegion(_mainObjectsTexture, 16, 0, 16, 16);
		}
		if (_appleSprite == null)
		{
			_appleSprite = new TextureRegion(_mainObjectsTexture, 32, 0, 16, 16);
		}
	}
	
	@Override
	public void graphicContextCanBeLostResolutionCanBeChanged()
	{
		_smallFont = null;
		_regularInfoFont = null;

		_mainObjectsTexture = null;

		_snakesHeadSprite = null;
		_snakesBodySprite = null;
		_appleSprite = null;
	}

	@Override
	public void presentSnakesHead(SpriteBatch batch, WorldPosition position)
	{
		initGdxResources();

		DeviceCoords headCoords = _deviceService.WorldPositionToDeviceCoords(position);
		batch.draw(_snakesHeadSprite, headCoords.x, headCoords.y, _deviceService.getTileSize(), _deviceService.getTileSize());
	}

	@Override
	public void presentSnakesBody(SpriteBatch batch, List<WorldPosition> snakesTrail, WorldPosition headPosition)
	{
		initGdxResources();

		for (WorldPosition position : snakesTrail)
		{
			if (!position.equals(headPosition))
			{
				DeviceCoords bodyItemCoords = _deviceService.WorldPositionToDeviceCoords(position);
				batch.draw(_snakesBodySprite, bodyItemCoords.x, bodyItemCoords.y, _deviceService.getTileSize(), _deviceService.getTileSize());
			}
		}
	}

	@Override
	public void presentLyingItem(SpriteBatch batch, LyingItem item)
	{
		initGdxResources();

		DeviceCoords itemCoords = _deviceService.WorldPositionToDeviceCoords(item.position);
		batch.draw(_appleSprite, itemCoords.x, itemCoords.y, _deviceService.getTileSize(), _deviceService.getTileSize());
	}

	@Override
	public void presentScore(SpriteBatch batch, int score)
	{
		initGdxResources();

		_regularInfoFont.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		_regularInfoFont.draw(batch, "Score: " + (new Integer(score)).toString(), _runtimeParams.layoutParams.scoreCoords.x, _runtimeParams.layoutParams.scoreCoords.y);
	}

	@Override
	public void presentBackground(SpriteBatch batch, Background background)
	{
		SystemParams systemParams = _systemParamsService.getSystemParams();
		
		background.leftTop.draw(
				batch,
				0,
				systemParams.height - _runtimeParams.layoutParams.gameBoxPaddingTop,
				_runtimeParams.layoutParams.gameBoxPaddingLeft,
				_runtimeParams.layoutParams.gameBoxPaddingTop);
		background.middleTop.draw(
				batch,
				_runtimeParams.layoutParams.gameBoxPaddingLeft,
				systemParams.height - _runtimeParams.layoutParams.gameBoxPaddingTop,
				systemParams.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - _runtimeParams.layoutParams.gameBoxPaddingRight,
				_runtimeParams.layoutParams.gameBoxPaddingTop);
		background.rightTop.draw(
				batch,
				systemParams.width - _runtimeParams.layoutParams.gameBoxPaddingRight,
				systemParams.height - _runtimeParams.layoutParams.gameBoxPaddingTop,
				_runtimeParams.layoutParams.gameBoxPaddingRight,
				_runtimeParams.layoutParams.gameBoxPaddingTop);
		background.leftCenter.draw(
				batch,
				0,
				_runtimeParams.layoutParams.gameBoxPaddingBottom,
				_runtimeParams.layoutParams.gameBoxPaddingLeft,
				systemParams.height - _runtimeParams.layoutParams.gameBoxPaddingTop - _runtimeParams.layoutParams.gameBoxPaddingBottom);

		batch.draw(
				background.middleCenter,
				_runtimeParams.layoutParams.gameBoxPaddingLeft,
				_runtimeParams.layoutParams.gameBoxPaddingBottom,
				systemParams.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - _runtimeParams.layoutParams.gameBoxPaddingRight,
				systemParams.height - _runtimeParams.layoutParams.gameBoxPaddingTop - _runtimeParams.layoutParams.gameBoxPaddingBottom);
		
		background.rightCenter.draw(
				batch,
				systemParams.width - _runtimeParams.layoutParams.gameBoxPaddingRight,
				_runtimeParams.layoutParams.gameBoxPaddingBottom,
				_runtimeParams.layoutParams.gameBoxPaddingRight,
				systemParams.height - _runtimeParams.layoutParams.gameBoxPaddingTop - _runtimeParams.layoutParams.gameBoxPaddingBottom);
		background.leftBottom.draw(
				batch,
				0,
				0,
				_runtimeParams.layoutParams.gameBoxPaddingLeft,
				_runtimeParams.layoutParams.gameBoxPaddingBottom);
		background.middleBottom.draw(
				batch,
				_runtimeParams.layoutParams.gameBoxPaddingLeft,
				0,
				systemParams.width - _runtimeParams.layoutParams.gameBoxPaddingLeft - _runtimeParams.layoutParams.gameBoxPaddingRight,
				_runtimeParams.layoutParams.gameBoxPaddingBottom);
		background.rightBottom.draw(
				batch,
				systemParams.width - _runtimeParams.layoutParams.gameBoxPaddingRight,
				0,
				_runtimeParams.layoutParams.gameBoxPaddingRight,
				_runtimeParams.layoutParams.gameBoxPaddingBottom);
	}

	@Override
	public void presentFps(SpriteBatch batch)
	{
		initGdxResources();
		
		_smallFont.setColor(new Color(1.0f, 1.0f, 1.0f, .5f));
		_smallFont.draw(batch, (new Integer(Gdx.graphics.getFramesPerSecond())).toString(), _runtimeParams.layoutParams.fpsCoords.x, _runtimeParams.layoutParams.fpsCoords.y);
	}
}
