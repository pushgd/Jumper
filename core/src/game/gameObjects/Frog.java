package game.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.collision.Collision;
import com.libGDX.engine.Base.gameComponents.GameObject;
import com.libGDX.engine.Base.gameComponents.TileInfo;
import com.libGDX.engine.Base.render.Bitmap;
import com.libGDX.engine.Debug.Debug;
import com.libGDX.engine.Utility.Vector2D;

import game.BitmapCacher;
import game.GameManager;
import game.scenes.GameScene;

/**
 * Created by newto on 29-12-2017.
 */

public class Frog extends GameObject
    {
    private static final float GRAVITY = 0.2f;
    Bitmap image;
    TileInfo tileInfo;

    public Frog()
        {
        image = BitmapCacher.loadFrog();
        position = new Vector2D(500, 9472);
        GameScene.instance.worldY = (int) (position.y - GameManager.SCREEN_HEIGHT * 0.7f);
        velocity = new Vector2D(3, 13);
        tileInfo = new TileInfo();
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
//            Debug.print("Here");
        position.y -= velocity.y;
        position.x += velocity.x * -getOrientationX();
        GameScene.instance.tilemap.getTile(0, position.x, position.y + image.getHeight() / 2, tileInfo);
        float top = GameScene.instance.worldY + GameManager.SCREEN_HEIGHT * 0.3f;
        if (position.y < top)
            {
            float dy = position.y - top;
//            Debug.print("DY = " + dy);
            GameScene.instance.worldY += dy;
            }
        if (tileInfo.id > 690 && velocity.y < 0)
            {
            position.y = tileInfo.y - image.getHeight() / 2;
            velocity.y = 10.15f;
            }


        velocity.y -= GRAVITY;
        if (velocity.y < -15)
            {
            velocity.y = -15;
            }
        int worldX = GameScene.instance.worldX;
        int worldY = GameScene.instance.worldY;
        if (position.x - image.getWidth() / 2 > worldX + GameManager.SCREEN_WIDTH)
            {
            position.x = worldX-image.getWidth() / 2;
            }

        if (position.x + image.getWidth() / 2 < worldX)
            {
            position.x =worldX+ GameManager.SCREEN_WIDTH + image.getWidth() / 2;
            }
        }

    @Override
    public void paint(SpriteBatch spriteBatch, float worldX, float worldY)
        {
        Bitmap.draw(spriteBatch, image, position.x - image.getWidth() / 2 - worldX, position.y - image.getHeight() / 2 - worldY);
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
