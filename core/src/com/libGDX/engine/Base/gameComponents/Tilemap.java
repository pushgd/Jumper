package com.libGDX.engine.Base.gameComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.libGDX.engine.Base.render.Bitmap;
import com.libGDX.engine.Debug.Debug;

import java.util.ArrayList;

import game.GameManager;

/**
 * Created by newto on 03-01-2018.
 */

public class Tilemap
{
    short map[][][];
    public int width, height, tileWidth, tileHeight;
    public int noOfLayers;
    private int firstLayer = -1;
    Tileset tileset;
    int screenWidthInTiles, screenHeightInTiles;
    ArrayList<GameObjectInfo> gameObjectInfos;

    public Tilemap(String mapPath)
    {

        XmlReader.Element element = null;
        XmlReader xmlreader = new XmlReader();
        Debug.print("Loading Map " + mapPath);
        try
        {
            FileHandle handle = Gdx.files.internal(mapPath);

            element = xmlreader.parse(handle);
            width = Integer.parseInt(element.getAttribute("width"));
            height = Integer.parseInt(element.getAttribute("height"));
            tileWidth = Integer.parseInt(element.getAttribute("tilewidth"));
            tileHeight = Integer.parseInt(element.getAttribute("tileheight"));
            screenWidthInTiles = GameManager.SCREEN_WIDTH / tileWidth;
            screenHeightInTiles = GameManager.SCREEN_HEIGHT / tileHeight;
//            String dir = mapPath.lastIndexOf("/");
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


            Debug.print("Done Loading Map ");
        }
        catch (Exception e)
        {
            Debug.print("Error loading Tilemap " + mapPath);
            e.printStackTrace();
        }


    }

    public void getTile(int layer, float x, float y, TileInfo info)
    {
        int tileX = (int) (x / tileWidth);
        int tileY = (int) (y / tileHeight);
        info.id = map[layer][tileX][tileY];
        info.x = tileX * tileWidth;
        info.y = tileY * tileHeight;
//        return map[layer][(int) (x/tileWidth)][(int) (y/tileHeight)];
    }

    public void paint(SpriteBatch spriteBatch, int worldX, int worldY)
    {

        int startTileX, startTileY;
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
        int endTileX = startTileX + screenWidthInTiles + 1;
        int endTileY = startTileY + screenHeightInTiles + 1;
        if (endTileX >= map[0].length)
        {
            endTileX = map[0].length - 1;
        }
        if (endTileY >= map[0][0].length)
        {
            endTileY = map[0][0].length - 1;
        }
        int i = 0;
        for (int x = startTileX; x <= endTileX; x++)
        {

            for (int y = startTileY; y <= endTileY; y++)
            {
                i++;
                if (map[0][x][y] < 0)
                {
                    continue;
                }

                tileset.draw(spriteBatch, x * tileWidth - worldX, y * tileHeight - worldY, map[0][x][y]);
            }
        }
//        Debug.print(" i = "+i);

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

    public void draw(SpriteBatch spriteBatch, int x, int y, int id)
    {

        spriteBatch.draw(tiles[id], x, y);

    }
}

