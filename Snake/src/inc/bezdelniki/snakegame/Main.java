package inc.bezdelniki.snakegame;

import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
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

	@Override
	public void create()
	{
		if (_batch == null)
		{
			_batch = new SpriteBatch();
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

		GameWorld gameWorld = gameWorldService.getGameWorld();
		Snake snake = gameWorld.snake;

		if (inputService.isThereTouchInEffect())
		{
			SnakeMovementChange movementChange = userActionsService.createSnakeMovementChangeAccordingTouch(snake, inputService.GetTouchCoords());

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
		catch (CloneNotSupportedException e)
		{
		}
		catch (UnknownLyingItemTypeException e)
		{
		}

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		_batch.begin();
		snakeService.drawSnake(snake, gameWorld.movementChangesInEffect, _batch);
		gameWorldService.drawAllLyingItems(_batch);
		BitmapFont font = new BitmapFont();
		font.draw(_batch, new Double(Gdx.graphics.getFramesPerSecond()).toString(), 100, 100);
		_batch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		ISystemParametersService systemParametersService = SnakeInjector.getInjectorInstance().getInstance(ISystemParametersService.class);
		systemParametersService.newResolutionWereSet(width, height);
	}

	@Override
	public void resume()
	{
	}
}