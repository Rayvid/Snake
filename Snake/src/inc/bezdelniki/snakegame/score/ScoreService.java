package inc.bezdelniki.snakegame.score;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.presentation.IPresentationService;

public class ScoreService implements IScoreService
{
	IPresentationService _presentationService;
	
	@Inject
	public ScoreService(IPresentationService presentationService)
	{
		_presentationService = presentationService;
	}
	
	@Override
	public int getScore4Item(LyingItem item)
	{
		return 1;
	}

	@Override
	public void presentScore(SpriteBatch batch, int score)
	{
		_presentationService.presentScore(batch, score);
	}
}
