package inc.bezdelniki.snakegame;

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
import inc.bezdelniki.snakegame.systemparameters.dtos.SystemParams;
import inc.bezdelniki.snakegame.useraction.IUserActionService;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Main implements ApplicationListener
{
	SpriteBatch _batch;
	Texture _texture;
	NinePatch _topLeft;
	NinePatch _topCenter;
	NinePatch _topRight;

	@Override
	public void create()
	{
		_texture = new Texture(Gdx.files.classpath("inc/bezdelniki/snakegame/resources/background.png"));
		//_texture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		_topLeft = new NinePatch(new TextureRegion(_texture, 0, 0, 4, 4), 1, 2, 1, 2);
		_topCenter = new NinePatch(new TextureRegion(_texture, 4, 0, 3, 4), 1, 1, 1, 2);
		//_topRight = new NinePatch(new TextureRegion(_texture, 12, 0, 4, 4), 2, 3, 1, 2);

		if (_batch == null)
			_batch = new SpriteBatch();
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
		ISystemParamsService systemParamsService = SnakeInjector.getInjectorInstance().getInstance(ISystemParamsService.class);
		RuntimeParams runtimeParams = SnakeInjector.getInjectorInstance().getInstance(RuntimeParams.class);

		SystemParams systemParams = systemParamsService.getSystemParams();
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
		catch (UnknownLyingItemTypeException e)
		{
		}

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		_batch.begin();
		snakeService.presentSnake(snake, gameWorld.movementChangesInEffect, _batch);
		gameWorldService.presentAllLyingItems(_batch);
		scoreService.presentScore(_batch, gameWorldService.getScore());
		//_batch.draw(_texture, 0, 0, systemParams.width, systemParams.height);
		_topLeft.draw(_batch, 0, systemParams.height - runtimeParams.layoutParams.gameBoxPaddingTop, runtimeParams.layoutParams.gameBoxPaddingLeft,
				runtimeParams.layoutParams.gameBoxPaddingTop);
		_topCenter.draw(_batch, runtimeParams.layoutParams.gameBoxPaddingLeft, systemParams.height - runtimeParams.layoutParams.gameBoxPaddingTop,
				systemParams.width - 200, systemParams.height);
		_batch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		ISystemParamsService systemParametersService = SnakeInjector.getInjectorInstance().getInstance(ISystemParamsService.class);
		systemParametersService.newResolutionWereSet(width, height);

		IPresentationService presentationService = SnakeInjector.getInjectorInstance().getInstance(IPresentationService.class);
		presentationService.graphicContextCanBeLostResolutionCanBeChanged();
	}

	@Override
	public void resume()
	{
	}
}