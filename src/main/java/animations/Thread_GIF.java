package animations;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.graphics.Movie;

import java.io.IOException;

/**
 * This class is used to display a GIF.
 *
 * @author Andrea Graziani and Leonardo Ratto
 * @see Movie
 */
public class Thread_GIF extends Animation {

    private Canvas myCanvas;
    private SurfaceHolder mySurfaceHolder;
    private Movie myMovie;
    private Handler myHandle;

    /**
     * Construct a newly allocated "Thread_GIF" object
     *
     * @param arg0 - Represents a {@link SurfaceHolder} object.
     * @param arg1 - Represents a {@link Handler} object.
     * @param arg2 - Represents a {@link Activity} object.
     */
    public Thread_GIF(SurfaceHolder arg0, Handler arg1, Context arg2) {

        this.mySurfaceHolder = arg0;
        this.myHandle = arg1;

        try {
            this.myMovie = Movie.decodeStream(arg2.getResources().getAssets().open("owl.gif"));
        } catch (IOException e) {
            Log.e(String.valueOf(Thread.currentThread().getId()) + ":", "Error during loading asset:\n" + e.getMessage());
        }
    }

    public void run() {

        try {

            // This instruction return a "Canvas" object that is used to draw into the surface. It can be "null".
            myCanvas = this.mySurfaceHolder.lockCanvas();

            if (myCanvas != null) {
                float scaleX = myCanvas.getWidth() / (1f * myMovie.width());
                float scaleY = myCanvas.getHeight() / (1f * myMovie.height());

                // Set size of GIF
                myCanvas.scale(scaleX, scaleY);

                // Set position of GIF
                myMovie.draw(myCanvas, 0, 0);
            }
        } catch (IllegalArgumentException e) {
            Log.e(String.valueOf(Thread.currentThread().getId()) + ":", "Error during surfaceHolder.lockCanvas():\n" + e.getMessage());
            this.myHandle.removeCallbacks(this);
        } finally {
            if (myCanvas != null) {
                try {
                    // Finish editing pixels in the surface.
                    // After this call, the surface's current pixels will be shown on the screen
                    this.mySurfaceHolder.unlockCanvasAndPost(myCanvas);

                    // Set current frame of GIF to display
                    myMovie.setTime((int) (System.currentTimeMillis() % myMovie.duration()));
                    this.myHandle.postDelayed(this, FPS);

                } catch (IllegalArgumentException e) {
                    Log.e(String.valueOf(Thread.currentThread().getId()) + ":", "Error during unlockCanvasAndPost():\n" + e.getMessage());
                    this.myHandle.removeCallbacks(this);
                }
            }
        }
    }
}



