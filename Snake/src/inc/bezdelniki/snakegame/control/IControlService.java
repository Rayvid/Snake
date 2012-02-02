package inc.bezdelniki.snakegame.control;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;

public interface IControlService
{
	public PauseControl CreatePauseControl();
    public ArrowPadControl CreateArrowPadControl();
}
