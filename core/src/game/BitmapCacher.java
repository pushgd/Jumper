package game;

import com.libGDX.engine.Base.render.Bitmap;

/**
 * Created by newto on 29-12-2017.
 */

public class BitmapCacher
    {

        private static Bitmap frog;

        public static Bitmap loadFrog()
            {
                if (frog == null)
                    {
                        frog = new Bitmap("frog.png");
                    }

                return frog;
            }

    }
