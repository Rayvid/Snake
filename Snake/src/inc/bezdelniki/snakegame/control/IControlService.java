package inc.bezdelniki.snakegame.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inc.bezdelniki.snakegame.control.dtos.ArrowPadControl;
import inc.bezdelniki.snakegame.control.dtos.Control;
import inc.bezdelniki.snakegame.control.dtos.PauseControl;

public interface IControlService
{
	public PauseControl CreatePauseControl();
    public ArrowPadControl CreateArrowPadControl();
	public void Present(SpriteBatch batch, Control control);
}
