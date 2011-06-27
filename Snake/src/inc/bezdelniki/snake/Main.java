package inc.bezdelniki.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class Main implements ApplicationListener {
        @Override
        public void create() {
        }

        @Override
        public void dispose() { }

        @Override
        public void pause() { }

        @Override
        public void render() {
        	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void resize(int width, int height) { }

        @Override
        public void resume() { }
}