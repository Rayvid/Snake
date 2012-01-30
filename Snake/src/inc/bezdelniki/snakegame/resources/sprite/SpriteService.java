package inc.bezdelniki.snakegame.resources.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteService implements ISpriteService
{
	@Override
	public TextureRegion getSnakesBody()
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		return new TextureRegion(texture, 16, 0, 16, 16);
	}

	@Override
	public TextureRegion getSnakesHead()
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		return new TextureRegion(texture, 0, 0, 16, 16);
	}

	@Override
	public TextureRegion getApple()
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		return new TextureRegion(texture, 32, 0, 16, 16);
	}

	@Override
	public TextureRegion getPauseButtonUnpressed()
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		return null;
	}

	@Override
	public TextureRegion getPauseButtonPressed()
	{
		Texture texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/16.png"));
		return null;
	}

}
