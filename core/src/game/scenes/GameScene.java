package game.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.gameComponents.GameObject;
import com.libGDX.engine.Base.gameComponents.Scene;
import com.libGDX.engine.Base.render.Bitmap;

import game.GameManager;
import game.GameObjectManager;
import game.gameObjects.Frog;

/**
 * Created by Dhande on 01-03-2017.
 */

public class GameScene extends Scene
    {
        float rotationX, rotationY, rotationZ;

        public GameScene()
            {
                GameObjectManager.init();
                GameObjectManager.addGameObject(new Frog());
            }

        @Override
        public void update()
            {
                GameObjectManager.update();
            }

        @Override
        public void pause()
            {

            }

        @Override
        public void paint(SpriteBatch spriteBatch)
            {
                GameObjectManager.paint(spriteBatch);
                Bitmap.Debug.drawText(spriteBatch, "X = " + GameManager.getOrientationX() , GameManager.SCREEN_WIDTH * 0.3f, GameManager.SCREEN_HEIGHT / 2);
            }

        @Override
        public void phoneOrientation(float x, float y, float z)
            {
                rotationZ = z;
                rotationX = x;
                rotationY = y;

            }
    }
