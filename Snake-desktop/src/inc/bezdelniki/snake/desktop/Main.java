package inc.bezdelniki.snake.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Main {
        public static void main (String[] args) {
                new LwjglApplication(new inc.bezdelniki.snake.Main(), "Snake", 480, 320, false);
        }
}