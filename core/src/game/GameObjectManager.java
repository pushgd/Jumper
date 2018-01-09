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
                for (GameObject g : gameObjectList)
                    {
                        g.update();
                    }
            }


        public static void paint(SpriteBatch spriteBatch,float worldx,float worldY)
            {
                for (GameObject g : gameObjectList)
                    {
                        g.paint(spriteBatch,worldx ,worldY );
                    }
            }

        public static void addGameObject(GameObject gameObject)
            {
                gameObjectList.add(gameObject);
            }

    }
