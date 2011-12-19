package inc.bezdelniki.snakegame;

import inc.bezdelniki.snakegame.gameworld.IGameWorldService;
import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.input.IInputService;
import inc.bezdelniki.snakegame.snake.ISnakeService;
import inc.bezdelniki.snakegame.snake.dtos.Snake;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.systemparameters.ISystemParametersService;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main implements ApplicationListener
{
	SpriteBatch batch;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

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
			}
		}

		try
		{
			gameWorldService.moveSnakeIfItsTime();
		}
		catch (SnakeMovementResultedEndOfGameException e)
		{
			int a = 1;
		}
		catch (CloneNotSupportedException e)
		{
		}

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		snakeService.drawSnake(snake, gameWorld.movementChangesInEffect, batch);
		batch.end();
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