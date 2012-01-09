package inc.bezdelniki.snakegame.font;

import inc.bezdelniki.snakegame.font.configuration.FontConfigurationItem;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface IFontService
{
	FontConfigurationItem getCurrentFontConfigurationItem();
	BitmapFont getSmallFont();
	BitmapFont getRegularInfoFont();
}
