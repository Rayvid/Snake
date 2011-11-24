package inc.bezdelniki.snakegame.presentation;

import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterCoords;
import inc.bezdelniki.snakegame.presentation.dtos.PresenterDeltas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IPresentationService {
	void presentSnakesHead(SpriteBatch batch, WorldPosition position);
	PresenterCoords WorldCoordsToPresenterCoords(WorldPosition position);
	PresenterDeltas getMovementDeltas();
	int getTileSize();
}
