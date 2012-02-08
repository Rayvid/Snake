package inc.bezdelniki.snakegame.presentation;

import java.util.List;

import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.resources.background.dtos.Background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IPresentationService
{
	void adjustToLostContextOrChangedResolution();
	void presentSnakesHead(SpriteBatch batch, WorldPosition position);
	void presentSnakesBody(SpriteBatch batch, List<WorldPosition> snakesTrail, WorldPosition headPosition);
	void presentLyingItem(SpriteBatch batch, LyingItem item);
	void presentScore(SpriteBatch batch, int score);
	void presentBackground(SpriteBatch batch, Background background);
	void presentFps(SpriteBatch batch);
	void presentControl(SpriteBatch batch, Control control);
}
