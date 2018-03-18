package com.libGDX.engine.Base.gameComponents;

import java.util.HashMap;

/**
 * Created by Dhande on 18/03/2018.
 */

public class GameObjectInfo
{
    public int id;
    public float x,y,height,width;
    HashMap<String,String>properties;
    public GameObjectInfo()
    {
        properties = new HashMap<String, String>();
    }
}
