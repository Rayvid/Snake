package inc.bezdelniki.snakegame.snake;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;

import inc.bezdelniki.snakegame.GameWorld;
import inc.bezdelniki.snakegame.appsettings.IAppSettingsService;
import inc.bezdelniki.snakegame.appsettings.dtos.AppSettings;
import inc.bezdelniki.snakegame.model.dtos.WorldPosition;
import inc.bezdelniki.snakegame.model.enums.Direction;
import inc.bezdelniki.snakegame.snake.dtos.Snake;

public class SnakeService implements ISnakeService {
	private IAppSettingsService _appSettingsService;

	@Inject
	SnakeService (IAppSettingsService appSettingsService) {
		_appSettingsService = appSettingsService;
	}
	
	@Override
	public void createSnake(GameWorld world) {
		AppSettings settings = _appSettingsService.getAppSettings();
		
		WorldPosition position = new WorldPosition();
		position.tileX = settings.initialHeadPositionX;
		position.tileY = settings.initialHeadPositionY;
		
		Snake snake = new Snake();
		snake.currLength = 1;
		snake.headPosition = position;
		snake.newLength = settings.initialSnakeLength;
		snake.direction = settings.initialDirection;
		
		world.setSnake(snake);
	}
	
	@Override
	public void growSnake(GameWorld world) {
		AppSettings settings = _appSettingsService.getAppSettings();
		
		world.getSnake().newLength += settings.growSnakeBy;
	}
	
	@Override
	public boolean moveSnake(GameWorld world) {
		Snake snake = world.getSnake();
		
		switch (snake.direction) {
			case RIGHT:
				snake.headPosition.tileX++;
				break;
				
			case LEFT:
				snake.headPosition.tileX--;
				break;
				
			case UP:
				snake.headPosition.tileY--;
				break;
				
			case DOWN:
				snake.headPosition.tileY++;
				break;
		}
		
		if (snake.headPosition.tileX < 0
				|| snake.headPosition.tileY < 0
				|| snake.headPosition.tileX >= world.getGameWorldWidth()
				|| snake.headPosition.tileY >= world.getGameWorldHeight()
				|| doesTileBelongToSnake(world, snake.headPosition)) {
			return true;
		}
		
		if (snake.currLength < snake.newLength)
		{
			snake.currLength++;
		}
		
		world.AdjustWorldAfterSnakesMovement();
		
		return false;
	}

	@Override
	public void removeSnake(GameWorld world) {
		world.setSnake(null);
	}

	@Override
	public void drawSnake(SpriteBatch batch, GameWorld world) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean doesTileBelongToSnake(GameWorld world, WorldPosition tile) {
		return false;
	}
	
	private WorldPosition traverseBackTroughSnakesTrail(WorldPosition position, Direction snakesDirection)
	{
		WorldPosition newPosition = null;
		
		try {
			newPosition = (WorldPosition)position.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		
		switch (snakesDirection)
		{
			case LEFT:
				newPosition.tileX++;
				break;
				
			case RIGHT:
				newPosition.tileX--;
				break;
				
			case UP:
				newPosition.tileY++;
				break;
				
			case DOWN:
				newPosition.tileY--;
				break;
		}
		
		return newPosition;
	}

	@Override
	public List<WorldPosition> generateSnakesTrail(GameWorld world) {
		List<WorldPosition> snakesTrailList = new ArrayList<WorldPosition>();
		Snake snake = world.getSnake();
		
		WorldPosition position = snake.headPosition;
		Direction direction = snake.direction;
		
		snakesTrailList.add(position);
		for (int i = 1; i < snake.currLength; i++) {
			position = traverseBackTroughSnakesTrail(position, direction);
			snakesTrailList.add(position);
		}
		
		return snakesTrailList;
	}
}
