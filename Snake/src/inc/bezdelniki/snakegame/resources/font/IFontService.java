package inc.bezdelniki.snakegame.resources.font;

import inc.bezdelniki.snakegame.resources.font.configuration.FontConfigurationItem;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface IFontService
{
	FontConfigurationItem getCurrentFontConfigurationItem();
	BitmapFont getSmallFont();
	BitmapFont getRegularInfoFont();
}
