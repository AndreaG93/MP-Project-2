package animations;

import android.util.Log;
import android.view.MotionEvent;

/**
 * This interface represent a generic animation
 */
public abstract class Animation implements Runnable {
    protected static int FPS;
    protected static boolean isTouched = false;
    protected static float touchPosX;
    protected static float touchPosY;

    /**
     * This method is used to set animation's FPS.
     *
     * @param arg0 Represents an integer
     */
    public void setFPS(int arg0) {
        FPS = arg0;
    }

    /**
     * This method is called when the user performs touch-screen interaction with the window that is currently
     * showing this wallpaper.
     *
     * @param arg0 Represent {@link MotionEvent} object.
     */
    public void touchEvent(MotionEvent arg0) {
        Log.e(String.valueOf(Thread.currentThread().getId()) + ":", "perform touch event.");
        isTouched = true;

        touchPosX = arg0.getX();
        touchPosY = arg0.getY();
    }
}
