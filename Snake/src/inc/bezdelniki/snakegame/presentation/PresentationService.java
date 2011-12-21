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

	@Inject
	public PresentationService(IDeviceService deviceService)
	{
		_deviceService = deviceService;
	}

	@Override
	public void presentSnakesHead(SpriteBatch batch, WorldPosition position)
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		Sprite sprite = new Sprite(texture, 0, 0, 16, 16);
		DeviceCoords headCoords = _deviceService.WorldPositionToDeviceCoords(position);
		sprite.setPosition(headCoords.x, headCoords.y);
		sprite.setSize(_deviceService.getTileSize(), _deviceService.getTileSize());
		sprite.draw(batch);
	}

	@Override
	public void presentSnakesBody(SpriteBatch batch, List<WorldPosition> snakesTrail, WorldPosition headPosition)
	{
		for (WorldPosition position : snakesTrail)
		{
			if (!position.equals(headPosition))
			{
				Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
				Sprite sprite = new Sprite(texture, 16, 0, 16, 16);
				DeviceCoords bodyItemCoords = _deviceService.WorldPositionToDeviceCoords(position);
				sprite.setPosition(bodyItemCoords.x, bodyItemCoords.y);
				sprite.setSize(_deviceService.getTileSize(), _deviceService.getTileSize());
				sprite.draw(batch);
			}
		}
	}

	@Override
	public void presentLyingItem(SpriteBatch batch, LyingItem item)
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		Sprite sprite = new Sprite(texture, 32, 0, 16, 16);
		DeviceCoords itemCoords = _deviceService.WorldPositionToDeviceCoords(item.position);
		sprite.setPosition(itemCoords.x, itemCoords.y);
		sprite.setSize(_deviceService.getTileSize(), _deviceService.getTileSize());
		sprite.draw(batch);
	}
}
