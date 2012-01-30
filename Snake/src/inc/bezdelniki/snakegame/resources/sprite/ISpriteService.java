package inc.bezdelniki.snakegame.resources.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface ISpriteService
{
	public TextureRegion getSnakesBody();
	public TextureRegion getSnakesHead();
	public TextureRegion getApple();
	public TextureRegion getPauseButtonUnpressed();
	public TextureRegion getPauseButtonPressed();
}
