package mb.livewallpaper;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import animations.Animation;
import animations.Thread_DrawingCircles;
import animations.Thread_GIF;
import settings.MySettingActivity;

/**
 * The {@code MyLiveWallpaper} class extend {@code WallpaperService} abstract class that is
 * is responsible for showing a live wallpaper behind applications that would like to sit on top of it.
 * <p>
 * Implementing a wallpaper thus involves subclassing from this, subclassing an Engine implementation,
 * and implementing onCreateEngine() to return a new instance of your engine.
 *
 * @author Andrea Graziani and Leonardo Ratto
 * @version 1.0
 * @see WallpaperService
 */
public class MyLiveWallpaper extends WallpaperService {

    /**
     * This method is used to create a new {@link WallpaperService.Engine} object.
     *
     * @return A {@link WallpaperService.Engine} object.
     */
    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    /**
     * MyWallpaperEngine
     */
    class MyWallpaperEngine extends WallpaperService.Engine implements WallpaperObserver {

        private final String TAG = "Wallpaper Service";
        private final SharedPreferences mySharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        private final Handler handler = new Handler();

        private Animation myThread;

        /**
         * This method is called when when the {@code WallpaperService.Engine} is first created
         *
         * @param surfaceHolder Represent {@code surfaceHolder} object.
         */
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // This instruction is used to register to the observer list.
            MySettingActivity.registerToObserverList(this);
            update();
        }

        /**
         * This method Called to inform you of the wallpaper becoming visible or hidden.
         * It is very important that a wallpaper only use CPU while it is visible..
         *
         * @param visible Represent {@code boolean} object.
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.d(TAG, "Visibility Status: " + (visible ? "Visible" : "Invisible"));
            if (visible) {
                handler.post(myThread);
                Log.d(TAG, "Added " + myThread.getClass().getName() + " into message queue.");
            } else {
                handler.removeCallbacks(myThread);
                Log.d(TAG, "Removed " + myThread.getClass().getName() + " from message queue.");
            }
        }

        /**
         * This method is called when the user performs touch-screen interaction with the window that is currently
         * showing this wallpaper.
         *
         * @param event Represent {@link MotionEvent} object.
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            boolean isTouchEnabled = this.mySharedPreference.getBoolean("TouchEnabled", true);

            Log.d(TAG, "Touch-Events: " + (isTouchEnabled ? "Enabled" : "Disabled"));
            if (isTouchEnabled)
                myThread.touchEvent(event);
        }

        /**
         * This method is called immediately before a surface is being destroyed.
         * After returning from this call, you should no longer try to access this surface.
         * If you have a rendering thread that directly accesses the surface, you must ensure that thread
         * is no longer touching the Surface before returning from this function.
         *
         * @param holder TRepresents the {@link SurfaceHolder} whose surface is being destroyed.
         */
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

            Log.d(TAG, "Removed " + myThread.getClass().getName() + " from message queue.");
            handler.removeCallbacks(myThread);
        }

        /**
         * This method is called immediately after any structural changes (format or size) have been made to the surface.
         * Therefore is necessary to update the imagery in the surface.
         *
         * @param holder Represents the {@link SurfaceHolder} whose surface has changed.
         * @param format Represents the new PixelFormat of the surface.
         * @param width  Represents the new width of the surface.
         * @param height Represents the new height of the surface.
         */
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.d(TAG, "onSurfaceChanged: width: " + width + ", height: " + height);
        }

        /**
         * This method is called to start the Thread that displays chosen 'Live Wallpaper'.
         */
        public synchronized void update()
        {
            handler.removeCallbacks(myThread);

            Log.d(TAG, "Updating Live Wallpaper...");

            switch (Integer.valueOf(this.mySharedPreference.getString("chosenLiveWallpaper", "0" )))
            {
                case 0:{
                    myThread = new Thread_GIF(this.getSurfaceHolder(), handler, getApplicationContext());
                    break;
                }
                case 1:{
                    myThread = new Thread_DrawingCircles(this.getSurfaceHolder(), handler);
                    break;
                }
            }

            // Set FPS
            myThread.setFPS(Integer.valueOf(this.mySharedPreference.getString("fps", "30")));

            // Start animation thread
            handler.post(myThread);
            Log.d(TAG, "Updating 'Live Wallpaper' complete.");
        }
    }
}

