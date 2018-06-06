package com.libGDX.engine.Base.collision;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.gameComponents.GameObject;
import com.libGDX.engine.Utility.Rectangle;
import com.libGDX.engine.Utility.Vector2D;

/**
 * Created by Dhande on 28-04-2017.
 */

public class Collision
{
    private static int baseID;
    public final int UID;
    public final int ID;
    public final GameObject owner;


    private Rectangle worldRect;
    public Vector2D localPosition;

    public boolean remove;


    public Collision(GameObject owner, int ID, float localX, float localY, float width, float height)
    {
        UID = baseID++;
        this.ID = ID;
        this.owner = owner;
        localPosition = new Vector2D(localX, localY);
        worldRect = new Rectangle();
        worldRect.calculateBounds(owner.position.x + localPosition.x, owner.position.y + localPosition.y, width, height);
    }

    public float getWidth()
    {
        return worldRect.width;
    }

    public float getHeight()
    {
        return worldRect.height;
    }

    public float getLeft()
    {
        return worldRect.left;
    }

    public float getRight()
    {
        return worldRect.right;
    }

    public float getTop()
    {
        return worldRect.top;
    }

    public float getbottom()
    {
        return worldRect.bottom;
    }

    private int getUID()
    {
        return UID;
    }

    public void update()
    {
        update(localPosition.x, localPosition.y, worldRect.width, worldRect.height);
    }

    public void update(float positionX, float positionY, float width, float height)
    {

        worldRect.calculateBounds(owner.position.x + positionX, owner.position.y + positionY, width, height);
    }

    public boolean isColliding(Collision other)
    {
        return worldRect.left <= other.worldRect.right && worldRect.right >= other.worldRect.left && worldRect.top <= other.worldRect.bottom && worldRect.bottom >= other.worldRect.top;
    }


    public void paint(SpriteBatch batch)
    {
        worldRect.paint(batch);
    }

    public void paint(SpriteBatch batch, Color c)
    {
        worldRect.paint(batch, c);
    }


    @Override
    public String toString()
    {
        return " collision {" + UID + " Owner " + owner.UID + " }";
    }
}
