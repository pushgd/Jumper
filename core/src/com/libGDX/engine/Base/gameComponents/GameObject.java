package com.libGDX.engine.Base.gameComponents;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libGDX.engine.Base.aniamtion.SpriteAnimation;
import com.libGDX.engine.Base.collision.Collision;

import game.CollisionManager;
import game.GameManager;

/**
 * Created by Dhande on 27-02-2017.
 */

public abstract class GameObject implements com.libGDX.engine.Base.aniamtion.AnimationEventListener
    {
    //From BabyStep
    private static int baseId = 0;


    public int ID;
    public final int UID;
    public com.libGDX.engine.Utility.Vector2D position;
    public com.libGDX.engine.Utility.Vector2D velocity;

    public boolean remove;
    public SpriteAnimation animation;


    public GameObject()
    {
        UID = baseId++;
    }


    public final void updateEntity()
    {
        update();
    }

    public final void paintEntity(SpriteBatch spriteBatch, float worldX, float worldY)
    {
        paint(spriteBatch, worldX, worldY);
    }

    public abstract void update();

    public abstract void paint(SpriteBatch spriteBatch, float worldX, float worldY);

    public abstract void onCollision(Collision collision, Collision otherCollision);

    public abstract void deallocate();

    public Collision createCollision(int ID,float localX, float localY, float width, float height)
    {
        Collision newCollision = new Collision(this, ID, localX, localY, width, height);
        CollisionManager.addCollision(newCollision);
        return newCollision;
    }


    public float getOrientationX()
    {
        return GameManager.getOrientationX();
    }

    public float getOrientationY()
    {
        return GameManager.getOrientationY();
    }

    public float getOrientationZ()
    {
        return GameManager.getOrientationZ();
    }

    }
