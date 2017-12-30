package game.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.collision.Collision;
import com.libGDX.engine.Base.gameComponents.GameObject;
import com.libGDX.engine.Base.render.Bitmap;
import com.libGDX.engine.Debug.Debug;
import com.libGDX.engine.Utility.Vector2D;

import game.BitmapCacher;
import game.GameManager;

/**
 * Created by newto on 29-12-2017.
 */

public class Frog extends GameObject
    {
        private static final float GRAVITY = 0.2f;
        Bitmap image;

        public Frog()
            {
                image = BitmapCacher.loadFrog();
                position = new Vector2D(GameManager.SCREEN_WIDTH / 2, GameManager.SCREEN_HEIGHT * 0.9f);
                velocity = new Vector2D(3, 13);
            }


        @Override
        public void onAnimationStateComplete(int animationID, int completedState)
            {

            }

        @Override
        public void onAnimationCycleComplete(int animationID, int completedState, int noOfCyclesRemaining)
            {

            }

        @Override
        public void onAnimationEvent(float eventNumber, float animationState)
            {

            }

        @Override
        public void update()
            {
                position.y -= velocity.y;
                position.x +=velocity.x*-getOrientationX();

                velocity.y -= GRAVITY;
                if (velocity.y < -15)
                    {
                        velocity.y = -15;
                    }
                if (position.y > GameManager.SCREEN_HEIGHT * 0.9f)
                    {
                        position.y = GameManager.SCREEN_HEIGHT * 0.9f;
                        velocity.y = 10;
                    }

                if (position.x - image.getWidth() / 2 > GameManager.SCREEN_WIDTH)
                    {
                        position.x = -image.getWidth() / 2;
                    }

                if (position.x + image.getWidth() / 2 < 0)
                    {
                        position.x = GameManager.SCREEN_WIDTH + image.getWidth() / 2;
                    }
            }

        @Override
        public void paint(SpriteBatch spriteBatch)
            {
                Bitmap.draw(spriteBatch, image, position.x - image.getWidth() / 2, position.y - image.getHeight() / 2);
            }

        @Override
        public void onCollision(Collision collision, Collision otherCollision)
            {

            }

        @Override
        public void deallocate()
            {

            }
    }
