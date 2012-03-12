package inc.bezdelniki.snakegame.control.dtos;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.useraction.dtos.PauseAction;

public class PauseControl extends Control
{
	private TextureRegion _unpressedImage;
	private TextureRegion _pressedImage;
	private PauseAction _pauseAction;
	
	public PauseControl(TextureRegion unpressedImage, TextureRegion pressedImage, PauseAction pauseAction)
	{
		_unpressedImage = unpressedImage;
		_pressedImage = pressedImage;
		_pauseAction = pauseAction;
		
		noTouchImage = _unpressedImage;
	}
	
	@Override
	public void recalculateControlLayout(SystemParams systemParams, int tileSize)
	{
		width = tileSize * 2;
		height = tileSize * 2;
		coords = new DeviceCoords(systemParams.width - width, systemParams.height - height);
		
		touchableRegions = new ArrayList<TouchableRegion>(); 
		touchableRegions.add(new TouchableRegion(1, 1, width - 2, height - 2, _pauseAction, _pressedImage));
	}
}
