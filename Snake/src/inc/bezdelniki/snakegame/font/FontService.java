package inc.bezdelniki.snakegame.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontService implements IFontService
{
	public BitmapFont getSmallFont()
	{
		return new BitmapFont(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/sf16.fnt"), Gdx.files.classpath("inc/bezdelniki/snakegame/resources/sf16.png"), false);
	}
	
	public BitmapFont getRegularInfoFont()
	{
		return new BitmapFont(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/f16.fnt"), Gdx.files.classpath("inc/bezdelniki/snakegame/resources/f16.png"), false);
	}
}
