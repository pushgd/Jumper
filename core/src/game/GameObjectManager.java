package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.gameComponents.GameObject;

import java.util.ArrayList;

/**
 * Created by Dhande on 01-03-2017.
 */

public class GameObjectManager
{
    private static ArrayList<GameObject> gameObjectList;

    private GameObjectManager()
    {


    }

    public static void init()
    {
        gameObjectList = new ArrayList<GameObject>(10);
    }

    public static void update()
    {
        GameObject g;
        for (int i = 0; i < gameObjectList.size(); i++)
        {
            g = gameObjectList.get(i);
            if (g.remove)
            {
                gameObjectList.remove(g);
                i--;
                continue;
            }
            g.update();
        }
    }


    public static void paint(SpriteBatch spriteBatch, float worldx, float worldY)
    {
        for (GameObject g : gameObjectList)
        {
            g.paint(spriteBatch, worldx, worldY);
        }
    }

    public static void addGameObject(GameObject gameObject)
    {
        gameObjectList.add(gameObject);
    }

}
