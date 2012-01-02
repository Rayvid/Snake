package inc.bezdelniki.snakegame.score;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.runtimeparameters.dto.LayoutParams;

public interface IScoreService
{
	int getScore4Item(LyingItem item);
	void presentScore(SpriteBatch batch, int score, LayoutParams layoutParams);
}
