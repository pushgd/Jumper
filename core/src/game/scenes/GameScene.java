package game.scenes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.gameComponents.Scene;
import com.libGDX.engine.Base.gameComponents.Tilemap;
import com.libGDX.engine.Base.render.Bitmap;

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
    public  int worldX=0,worldY = 0;
    public GameScene()
        {
        instance = this;
        GameObjectManager.init();
        GameObjectManager.addGameObject(new Frog());
        tilemap = new Tilemap("map/TileMap.tmx");
        backGround = new Bitmap("bg.png");
        worldX = 64*3;
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

Bitmap.draw(spriteBatch,backGround,0,0);
        GameObjectManager.paint(spriteBatch,worldX,worldY);
//        Bitmap.Debug.drawText(spriteBatch, "X = " + GameManager.getOrientationX(), GameManager.SCREEN_WIDTH * 0.3f, GameManager.SCREEN_HEIGHT / 2);
        tilemap.paint(spriteBatch, worldX, worldY);
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
            worldX-=5;
            }

        if (keycode == Input.Keys.RIGHT)
            {
            worldX+=5;
            }
        if (keycode == Input.Keys.UP)
            {
            worldY-=5;
            }
        if (keycode == Input.Keys.DOWN)
            {
            worldY+=5;
            }
        super.keyDown(keycode);
        }
    }
