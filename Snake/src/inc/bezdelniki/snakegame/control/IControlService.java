package inc.bezdelniki.snakegame.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;
import inc.bezdelniki.snakegame.device.dtos.TouchCoords;
import inc.bezdelniki.snakegame.runtimeparameters.dto.RuntimeParams;
import inc.bezdelniki.snakegame.useraction.dtos.UserAction;

public interface IControlService
{
	public PauseControl CreatePauseControl();
    public ArrowPadControl CreateArrowPadControl();
	public void Present(SpriteBatch batch, Control control);
	public UserAction GetUserActionIfTouched(Control control, TouchCoords touchCoords);
	public void ReleaseTouch(Control control);
	public void adjustLayoutParams(Control control, RuntimeParams runtimeParams);
	public void adjustToLostContextOrChangedResolution(Control control);
}
