package inc.bezdelniki.snakegame.presentation;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;

public class PresentationService implements IPresentationService
{
	private IDeviceService _deviceService;
	private Texture _mainObjectsTexture = null;
	private Sprite _snakesHeadSprite = null;
	private Sprite _snakesBodySprite = null;
	private Sprite _appleSprite = null;

	@Inject
	public PresentationService(IDeviceService deviceService)
	{
		_deviceService = deviceService;
	}
	
	private void initGdxResources()
	{
		if (_mainObjectsTexture == null) _mainObjectsTexture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		if (_snakesHeadSprite == null)
		{
			_snakesHeadSprite = new Sprite(_mainObjectsTexture, 0, 0, 16, 16);
			_snakesHeadSprite.setSize(_deviceService.getTileSize(), _deviceService.getTileSize());
		}
		if (_snakesBodySprite == null)
		{
			_snakesBodySprite = new Sprite(_mainObjectsTexture, 16, 0, 16, 16);
			_snakesBodySprite.setSize(_deviceService.getTileSize(), _deviceService.getTileSize());
		}
		if (_appleSprite == null)
		{
			_appleSprite = new Sprite(_mainObjectsTexture, 32, 0, 16, 16);
			_appleSprite.setSize(_deviceService.getTileSize(), _deviceService.getTileSize());
		}
	}

	@Override
	public void presentSnakesHead(SpriteBatch batch, WorldPosition position)
	{
		initGdxResources();
		
		DeviceCoords headCoords = _deviceService.WorldPositionToDeviceCoords(position);
		_snakesHeadSprite.setPosition(headCoords.x, headCoords.y);
		_snakesHeadSprite.draw(batch);
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
				_snakesBodySprite.setPosition(bodyItemCoords.x, bodyItemCoords.y);
				_snakesBodySprite.draw(batch);
			}
		}
	}

	@Override
	public void presentLyingItem(SpriteBatch batch, LyingItem item)
	{
		initGdxResources();
		
		DeviceCoords itemCoords = _deviceService.WorldPositionToDeviceCoords(item.position);
		_appleSprite.setPosition(itemCoords.x, itemCoords.y);
		_appleSprite.draw(batch);
	}

	@Override
	public void graphicContextCanBeLost()
	{
		_mainObjectsTexture = null;
		_snakesHeadSprite = null;
		_snakesBodySprite = null;
		_appleSprite = null;
	}
}
