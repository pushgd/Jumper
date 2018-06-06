package com.libGDX.engine.Base.gameComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.libGDX.engine.Base.render.Bitmap;
import com.libGDX.engine.Debug.Debug;

import java.util.ArrayList;

import game.GameManager;
import game.scenes.GameScene;

/**
 * Created by newto on 03-01-2018.
 */

public class Tilemap
{
    private int visibleMapHeight;
    private ArrayList<GameObjectInfo> gameObjectInfos;
    public int width, height, tileWidth, tileHeight;
    public int noOfLayers;

    private short map[][][];
    private short visiblemap[][][];
    private int firstLayer = -1;
    private int startTileX = 0, startTileY = 0, endTileX, endTileY;

    Tileset tileset;
    int screenWidthInTiles, screenHeightInTiles;
    int topPointer = 0, basePointer = 0, mapHeightInTiles;

    Level levelList[];
    Level currentLevel, previousLevel;

    public Tilemap(String mapPath)
    {
        levelList = new Level[3];
        XmlReader.Element element = null;
        XmlReader xmlreader = new XmlReader();
        Debug.print("Loading Map " + mapPath);
        try
        {
            levelList[0] = new Level(mapPath);
            levelList[1] = new Level(mapPath);
            FileHandle handle = Gdx.files.internal(mapPath);

            element = xmlreader.parse(handle);
            width = Integer.parseInt(element.getAttribute("width"));
            height = Integer.parseInt(element.getAttribute("height"));
            tileWidth = Integer.parseInt(element.getAttribute("tilewidth"));
            tileHeight = Integer.parseInt(element.getAttribute("tileheight"));
            screenWidthInTiles = GameManager.SCREEN_WIDTH / tileWidth;
            screenHeightInTiles = GameManager.SCREEN_HEIGHT / tileHeight;
//            String dir = mapPath.lastIndexOf("/");
            mapHeightInTiles = (int) (screenHeightInTiles * 1.5f);
            String imagePath = element.getChild(0).getAttribute("source").replace("tsx", "png");
            imagePath = mapPath.substring(0, mapPath.lastIndexOf("/") + 1) + imagePath;
            for (int i = 0; i < element.getChildCount(); i++)
            {
                if (element.getChild(i).getName().contains("layer"))
                {
                    if (firstLayer < 0)
                    {
                        firstLayer = i;
                    }
                    noOfLayers++;
                }
            }
            map = new short[noOfLayers][width][height];
            visibleMapHeight = (int) (screenHeightInTiles * 1.25f);
            visiblemap = new short[noOfLayers][visibleMapHeight][screenWidthInTiles];
            for (int i = 0; i < noOfLayers; i++)
            {
                String temp[] = element.getChild(firstLayer + i).getChildByName("data").getText().split(",");
                for (int j = 0; j < temp.length; j++)
                {
                    try
                    {
                        map[i][j % width][j / width] = (short) (Short.parseShort(temp[j].trim()) - 1);
                    }
                    catch (Exception e)
                    {
                        Debug.print("Error parsing j = " + j + " temp " + temp[j]);
                    }
                }
            }
            tileset = new Tileset(imagePath, tileWidth, tileHeight);
            currentLevel = levelList[0];
            currentLevel.reset();
            topPointer = visibleMapHeight - 1;
            copyMap(visibleMapHeight);
            basePointer = 0;
            topPointer = visibleMapHeight - 1;

            createGameObjectInfo(element);


            Debug.print("Done Loading Map ");
        }
        catch (Exception e)
        {
            Debug.print("Error loading Tilemap " + mapPath);
            e.printStackTrace();
        }


    }

    private void createGameObjectInfo(XmlReader.Element element)
    {
        gameObjectInfos = new ArrayList<GameObjectInfo>();
        XmlReader.Element gameObjectData = element.getChildByName("objectgroup");
        int noOfGameObjects = gameObjectData.getChildCount();

        for (int i = 0; i < noOfGameObjects; i++)
        {
            GameObjectInfo temp = new GameObjectInfo();
            XmlReader.Element child = gameObjectData.getChild(i);
            temp.id = Integer.parseInt(child.getAttribute("id"));
            temp.x = Float.parseFloat(child.getAttribute("x"));
            temp.y = Float.parseFloat(child.getAttribute("y"));
            temp.height = Float.parseFloat(child.getAttribute("height"));
            temp.width = Float.parseFloat(child.getAttribute("width"));
            XmlReader.Element properties = child.getChildByName("properties");
            if (properties != null)
            {
                for (int j = 0; j < properties.getChildCount(); j++)
                {
                    String key = properties.getChild(j).getAttribute("name");
                    String value = properties.getChild(j).getAttribute("value");
                    temp.properties.put(key, value);
                }
            }
            gameObjectInfos.add(temp);
        }
    }

    public void getTile(int layer, float x, float y, TileInfo info)
    {
        int tileX = (int) ((x - GameScene.instance.worldX) / tileWidth);
        int tileY = (int) ((y - GameScene.instance.worldY) / tileHeight);
        info.id = visiblemap[layer][(basePointer + tileY) % visibleMapHeight][tileX];
        info.x = tileX * tileWidth;
        info.y = tileY * tileHeight;
//        return map[layer][(int) (x/tileWidth)][(int) (y/tileHeight)];
    }

    int changeInWorldY = 0, oldWorldY;

    public void paint(SpriteBatch spriteBatch, int worldX, int worldY)
    {
//        if(paintDy>tileHeight)
//        {
//            paintDy = 0;
//        }

        int pointer = basePointer;
//        dy++;
        float x = 0, y = (screenHeightInTiles * tileHeight + changeInWorldY);
        for (int i = 0; i < noOfLayers; i++)
        {
            while (pointer != topPointer)
            {

                for (int j = 0; j < screenWidthInTiles; j++)
                {

                    if (visiblemap[i][pointer][j] < 0)
                    {
                        x += tileWidth;
                        continue;
                    }
                    tileset.draw(spriteBatch, x, y, visiblemap[i][pointer][j]);
                    x += tileWidth;
                }
                x = 0;
                y -= tileHeight;
                pointer = (pointer + 1);
                if (pointer >= visibleMapHeight)
                {
                    pointer = 0;
                }
            }
            y = 0;
            pointer = basePointer;
        }
//        paintDy=0;
    }

    public static float dy;


    public void update(int worldX, int worldY)
    {

        changeInWorldY += dy;
        if (Math.abs(changeInWorldY) > 500)
        {
            changeInWorldY = 0;
        }

        oldWorldY = worldY;

        startTileX = (worldX / tileWidth - 1);
        if (startTileX < 0)
        {
            startTileX = 0;
        }
        startTileY = (worldY / tileHeight - 1);
        if (startTileY < 0)
        {
            startTileY = 0;
        }

        endTileX = startTileX + screenWidthInTiles + 1;
        endTileY = startTileY + screenHeightInTiles + 1;

        int noOfNewTiles = Math.abs(changeInWorldY) / tileHeight;
        copyMap(noOfNewTiles);
        changeInWorldY = changeInWorldY % tileHeight;

    }

    private void copyMap(int noOfNewTiles)
    {

        for (int j = 0; j < noOfNewTiles; j++)
        {
            currentLevel.base--;
            if (currentLevel.base < 0)
            {
                currentLevel = levelList[1];
                currentLevel.reset();
//                currentLevel.base--;
            }
            for (int i = 0; i < noOfLayers; i++)
            {
                try
                {
                    visiblemap[i][topPointer] = currentLevel.getRow(i);
                }
                catch (Exception e)
                {
                    Debug.print("Stop");
                }
            }

            topPointer = (topPointer + 1) % visibleMapHeight;
            basePointer = (basePointer + 1) % visibleMapHeight;

        }

    }
}

class Tileset
{
    Bitmap tilesetImage;
    TextureRegion tiles[];

    protected Tileset(String path, int tileWidth, int tileHeight)
    {
        tilesetImage = new Bitmap(path);
        TextureRegion temp[][] = new TextureRegion(tilesetImage.getTexture()).split(tileWidth, tileHeight);
        tiles = new TextureRegion[temp.length * temp[0].length];
        for (int i = 0; i < temp.length; i++)
        {
            for (int j = 0; j < temp[i].length; j++)
            {

                tiles[i * temp.length + j] = temp[i][j];
                tiles[i * temp.length + j].flip(false, true);
            }
        }
    }

    public void draw(SpriteBatch spriteBatch, float x, float y, int id)
    {

        spriteBatch.draw(tiles[id], x, y);
        Bitmap.Debug.drawText(spriteBatch, ""+id, x, y, Color.GREEN);
    }
}

class Level
{
    short map[][][];
    int height;
    int width;
    int noOfLayers;
    int base = -1;

    public Level(String mapPath)
    {
        XmlReader.Element element = null;
        XmlReader xmlreader = new XmlReader();
        Debug.print("Loading Map " + mapPath);
        FileHandle handle = Gdx.files.internal(mapPath);
        int firstLayer = -1;
        try
        {
            element = xmlreader.parse(handle);
            height = Integer.parseInt(element.getAttribute("height"));
            width = Integer.parseInt(element.getAttribute("width"));


            for (int i = 0; i < element.getChildCount(); i++)
            {
                if (element.getChild(i).getName().contains("layer"))
                {
                    if (firstLayer < 0)
                    {
                        firstLayer = i;
                    }
                    noOfLayers++;
                }
            }
            map = new short[noOfLayers][height][width];
            for (int i = 0; i < noOfLayers; i++)
            {
                String temp[] = element.getChild(firstLayer + i).getChildByName("data").getText().split(",");
                for (int j = 0; j < temp.length; j++)
                {
                    try
                    {
                        map[i][j / width][j % width] = (short) (Short.parseShort(temp[j].trim()) - 1);
                    }
                    catch (Exception e)
                    {
                        Debug.print("Error parsing j = " + j + " temp " + temp[j]);
                    }
                }
            }
            Debug.print("Done Loading Level");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void reset()
    {
        base = height - 1;
    }


    public short[] getRow(int layers)
    {
        return map[layers][base];
    }
}