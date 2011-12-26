package inc.bezdelniki.snakegame.presentation;

import java.util.List;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.runtimeparameters.dto.LayoutParams;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IPresentationService
{
	void graphicContextCanBeLost();
	void presentSnakesHead(SpriteBatch batch, WorldPosition position, LayoutParams layoutParams);
	void presentSnakesBody(SpriteBatch batch, List<WorldPosition> snakesTrail, WorldPosition headPosition, LayoutParams layoutParams);
	void presentLyingItem(SpriteBatch batch, LyingItem item, LayoutParams layoutParams);
}
