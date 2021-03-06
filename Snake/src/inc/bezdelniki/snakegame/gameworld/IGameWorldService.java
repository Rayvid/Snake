package inc.bezdelniki.snakegame.gameworld;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChangeAction;

public interface IGameWorldService
{
	void initGameWorld();
	GameWorld getGameWorld();
	void applySnakeMovementChange(SnakeMovementChangeAction movementChange);
	LyingItem createAndApplyLyingItemSomewhere(ItemType itemType) throws LyingItemNowhereToPlaceException;
	void moveSnakeIfItsTime() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException;
	void moveSnake() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException;
	void presentAllLyingItems(SpriteBatch batch);
	int getScore();
}
