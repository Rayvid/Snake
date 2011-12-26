package inc.bezdelniki.snakegame.device;

import inc.bezdelniki.snakegame.device.dtos.DeviceCoords;
import inc.bezdelniki.snakegame.device.dtos.DeviceDeltas;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.gameworld.dtos.WorldPosition;
import inc.bezdelniki.snakegame.runtimeparameters.dto.LayoutParams;

public interface IDeviceService {
	DeviceCoords WorldPositionToDeviceCoords(WorldPosition position, LayoutParams layoutParams);
	WorldPosition DeviceCoordsToWorldPosition(DeviceCoords coords, LayoutParams layoutParams);
	DeviceCoords TouchCoordsToDeviceCoords(TouchCoords touchCoords);
	TouchCoords DeviceCoordsToTouchCoords(DeviceCoords deviceCoords);
	
	DeviceDeltas getDeltas();
	int getTileSize(LayoutParams layoutParams);
}
