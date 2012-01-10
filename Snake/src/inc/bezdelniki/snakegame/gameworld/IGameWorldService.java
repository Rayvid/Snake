package inc.bezdelniki.snakegame.gameworld;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.gameworld.dtos.GameWorld;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.gameworld.exceptions.LyingItemNowhereToPlaceException;
import inc.bezdelniki.snakegame.gameworld.exceptions.UnknownLyingItemTypeException;
import inc.bezdelniki.snakegame.lyingitem.dtos.LyingItem;
import inc.bezdelniki.snakegame.lyingitem.enums.ItemType;
import inc.bezdelniki.snakegame.snake.exceptions.SnakeMovementResultedEndOfGameException;
import inc.bezdelniki.snakegame.useraction.dtos.SnakeMovementChange;

public interface IGameWorldService
{
	void initGameWorld();
	GameWorld getGameWorld();
	void applySnakeMovementChange(SnakeMovementChange movementChange);
    void applyLyingItem(LyingItem lyingItem);
	LyingItem createAndApplyLyingItemSomewhere(ItemType itemType) throws LyingItemNowhereToPlaceException;
	void moveSnakeIfItsTime() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException;
	void moveSnake() throws SnakeMovementResultedEndOfGameException, UnknownLyingItemTypeException;
	LyingItem getLyingItemInTile(WorldPosition position);
	void presentAllLyingItems(SpriteBatch batch);
	int getScore();
}
