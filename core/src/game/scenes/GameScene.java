package game.scenes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.gameComponents.Scene;
import com.libGDX.engine.Base.gameComponents.TileInfo;
import com.libGDX.engine.Base.gameComponents.Tilemap;
import com.libGDX.engine.Base.render.Bitmap;
import com.libGDX.engine.Debug.Debug;

import game.GameManager;
import game.GameObjectManager;
import game.gameObjects.Frog;

/**
 * Created by Dhande on 01-03-2017.
 */

public class GameScene extends Scene
{
    public static GameScene instance;
    float rotationX, rotationY, rotationZ;
    public Tilemap tilemap;
    Bitmap backGround;
    public int worldX = 0, worldY = 0;
    Frog frog;

    public GameScene()
    {
        instance = this;
        GameObjectManager.init();
        frog = new Frog();
        GameObjectManager.addGameObject(frog);
        tilemap = new Tilemap("map/TileMap.tmx");
        backGround = new Bitmap("bg.png");
        worldX = 64 * 3;
    }

    @Override
    public void update()
    {
        tilemap.update(worldX, worldY);
        GameObjectManager.update();

    }

    @Override
    public void pause()
    {

    }


    @Override
    public void paint(SpriteBatch spriteBatch)
    {

        Bitmap.draw(spriteBatch, backGround, 0, 0);
        GameObjectManager.paint(spriteBatch, worldX, worldY);
//        Bitmap.Debug.drawText(spriteBatch, "X = " + GameManager.getOrientationX(), GameManager.SCREEN_WIDTH * 0.3f, GameManager.SCREEN_HEIGHT / 2);
        tilemap.paint(spriteBatch, worldX, worldY);
        Bitmap.Debug.drawLine(spriteBatch, 0, GameManager.SCREEN_HEIGHT * 0.3f, GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT * 0.3f, 3);
        Bitmap.Debug.drawLine(spriteBatch, 0, 64, GameManager.SCREEN_WIDTH, 64, 3);
//        try
//        {
//            Thread.sleep(100);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
    }

    @Override
    public void phoneOrientation(float x, float y, float z)
    {
        rotationZ = z;
        rotationX = x;
        rotationY = y;

    }

    @Override
    public void keyDown(int keycode)
    {
        if (keycode == Input.Keys.LEFT)
        {
//            worldX -= 5;
        }

        if (keycode == Input.Keys.RIGHT)
        {
//            worldX += 5;
        }
        if (keycode == Input.Keys.UP)
        {
            frog.position.y-=5;
//            worldY -= 5;
        }
        if (keycode == Input.Keys.DOWN)
        {
//            worldY += 5;
            frog.position.y+=5;
        }
        super.keyDown(keycode);
    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button)
    {
        super.touchDown(screenX, screenY, pointer, button);
        Debug.print("X " + screenX + " Y = " + screenY);
        TileInfo ti = new TileInfo();
        tilemap.getTile(0, worldX + screenX, worldY + screenY, ti);
        Debug.print(ti + "");
    }
}
