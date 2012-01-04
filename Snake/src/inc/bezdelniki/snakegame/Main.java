package inc.bezdelniki.snakegame;

import inc.bezdelniki.snakegame.font.IFontService;
import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.presentation.IPresentationService;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.score.IScoreService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.systemparameters.ISystemParamsService;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main implements ApplicationListener
{
	SpriteBatch _batch;
	BitmapFont _font;

	@Override
	public void create()
	{
		if (_batch == null)	_batch = new SpriteBatch();
		if (_font == null)
		{
			IFontService fontService = SnakeInjector.getInjectorInstance().getInstance(IFontService.class);
			_font = fontService.getSmallFont();
		}
		
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		if (gameWorldService.getGameWorld() == null)
		{
			gameWorldService.initGameWorld();
		}
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void render()
	{
		ISnakeService snakeService = SnakeInjector.getInjectorInstance().getInstance(ISnakeService.class);
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		IInputService inputService = SnakeInjector.getInjectorInstance().getInstance(IInputService.class);
		IUserActionService userActionsService = SnakeInjector.getInjectorInstance().getInstance(IUserActionService.class);
		IScoreService scoreService = SnakeInjector.getInjectorInstance().getInstance(IScoreService.class);

		GameWorld gameWorld = gameWorldService.getGameWorld();
		Snake snake = gameWorld.snake;
		RuntimeParams runtimeParams = gameWorldService.getRuntimeParams();

		if (inputService.isThereTouchInEffect())
		{
			SnakeMovementChange movementChange = userActionsService.createSnakeMovementChangeAccordingTouch(snake, inputService.GetTouchCoords(), runtimeParams.layoutParams);

			if (movementChange != null)
			{
				gameWorldService.applySnakeMovementChange(movementChange);
				try
				{
					gameWorldService.createAndApplyLyingItemSomewhere(ItemType.APPLE);
				}
				catch (LyingItemNowhereToPlaceException e)
				{
				}
			}
		}

		try
		{
			gameWorldService.moveSnakeIfItsTime();
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
		}
		catch (UnknownLyingItemTypeException e)
		{
		}

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		_batch.begin();
		snakeService.presentSnake(snake, gameWorld.movementChangesInEffect, _batch, runtimeParams.layoutParams);
		gameWorldService.presentAllLyingItems(_batch);
		scoreService.presentScore(_batch, gameWorldService.getScore(), runtimeParams.layoutParams);
		_font.draw(_batch, new Double(Gdx.graphics.getFramesPerSecond()).toString(), 2, runtimeParams.layoutParams.gameBoxPaddingBottom - 2);
		_batch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		ISystemParamsService systemParametersService = SnakeInjector.getInjectorInstance().getInstance(ISystemParamsService.class);
		systemParametersService.newResolutionWereSet(width, height);
		
		IGameWorldService gameWorldService = SnakeInjector.getInjectorInstance().getInstance(IGameWorldService.class);
		gameWorldService.systemParamsCanBeChanged();
		
		IPresentationService presentationService = SnakeInjector.getInjectorInstance().getInstance(IPresentationService.class);
		presentationService.graphicContextCanBeLost();
	}

	@Override
	public void resume()
	{
	}
}