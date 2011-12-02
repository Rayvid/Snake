package inc.bezdelniki.snakegame.device;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;

public interface IDeviceService {
	DeviceCoords WorldCoordsToDeviceCoords(WorldPosition position);
	DeviceDeltas getDeltas();
	int getTileSize();
}
