package inc.bezdelniki.snakegame.device;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;

public interface IDeviceService {
	DeviceCoords WorldPositionToDeviceCoords(WorldPosition position);
	WorldPosition DeviceCoordsToWorldPosition(DeviceCoords coords);
	DeviceCoords TouchCoordsToDeviceCoords(TouchCoords touchCoords);
	TouchCoords DeviceCoordsToTouchCoords(DeviceCoords deviceCoords);
	
	DeviceDeltas getDeltas();
	int getTileSize();
}
