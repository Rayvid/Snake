package inc.bezdelniki.snakegame.resources.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import inc.bezdelniki.snakegame.resources.background.dtos.Background;

public class BackgroundService implements IBackgroundService
{
	@Override
	public Background GetBackground()
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/background.png"));
		
		Background background = new Background();
		background.leftTop = new NinePatch(new TextureRegion(texture, 0, 0, 4, 4), 1, 2, 1, 2);
		background.middleTop = new NinePatch(new TextureRegion(texture, 4, 0, 3, 4), 1, 1, 1, 2);
		background.rightTop = new NinePatch(new TextureRegion(texture, 12, 0, 4, 4), 2, 1, 1, 2);
		background.leftCenter = new NinePatch(new TextureRegion(texture, 0, 4, 4, 3), 1, 2, 1, 1);
		background.middleCenter = new TextureRegion(texture, 7, 7, 2, 2);
		background.rightCenter = new NinePatch(new TextureRegion(texture, 12, 4, 4, 3), 2, 1, 1, 1);
		background.leftBottom = new NinePatch(new TextureRegion(texture, 0, 12, 4, 4), 1, 2, 2, 1);
		background.middleBottom = new NinePatch(new TextureRegion(texture, 4, 12, 3, 4), 1, 1, 2, 1);
		background.rightBottom = new NinePatch(new TextureRegion(texture, 12, 12, 4, 4), 2, 1, 2, 1);
		
		return background;
	}

}
