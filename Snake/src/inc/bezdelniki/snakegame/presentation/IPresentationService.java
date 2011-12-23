package inc.bezdelniki.snakegame.presentation;

import java.util.List;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IPresentationService
{
	void graphicContextCanBeLost();
	void presentSnakesHead(SpriteBatch batch, WorldPosition position);
	void presentSnakesBody(SpriteBatch batch, List<WorldPosition> snakesTrail, WorldPosition headPosition);
	void presentLyingItem(SpriteBatch batch, LyingItem item);
}
