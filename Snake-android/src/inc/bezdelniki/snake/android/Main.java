package inc.bezdelniki.snake.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
//import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;

public class Main extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        //config.useCompass = false;
        //config.useAccelerometer = false;
        //config.useWakelock = true;
        
        initialize(new inc.bezdelniki.snakegame.Main(), false);
    }
}