package inc.bezdelniki.snakegame.font;

import inc.bezdelniki.snakegame.device.IDeviceService;
import inc.bezdelniki.snakegame.font.configuration.FontConfiguration;
import inc.bezdelniki.snakegame.font.configuration.FontConfigurationItem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.google.inject.Inject;

public class FontService implements IFontService
{
	private IDeviceService _deviceService;
	private FontConfiguration _fontConfiguration;
	
	@Inject
	public FontService(
			IDeviceService deviceService,
			FontConfiguration fontConfiguration)
	{
		_deviceService = deviceService;
		_fontConfiguration = fontConfiguration;
	}
	
	@Override
	public FontConfigurationItem getCurrentFontConfigurationItem()
	{
		int tileSize = _deviceService.getTileSize();
		
		int choosen = -1;
		for (int index = 0; index < _fontConfiguration.configurationItems.size(); index++)
		{
			if (_fontConfiguration.configurationItems.get(index).tileSizeMin <= tileSize
					&& (choosen == -1
							|| _fontConfiguration.configurationItems.get(index).tileSizeMin > _fontConfiguration.configurationItems.get(choosen).tileSizeMin))
			{
				choosen = index;
			}
		}
		
		return _fontConfiguration.configurationItems.get(choosen);
	}
	
	@Override
	public BitmapFont getSmallFont()
	{
		return new BitmapFont(Gdx.files.classpath(getCurrentFontConfigurationItem().smallFontClassPath + ".fnt"), Gdx.files.classpath(getCurrentFontConfigurationItem().smallFontClassPath + ".png"), false);
	}
	
	@Override
	public BitmapFont getRegularInfoFont()
	{
		return new BitmapFont(Gdx.files.classpath(getCurrentFontConfigurationItem().regularInfoClassPath + ".fnt"), Gdx.files.classpath(getCurrentFontConfigurationItem().regularInfoClassPath + ".png"), false);
	}
}
