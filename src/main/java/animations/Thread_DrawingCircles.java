package animations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.graphics.Movie;

/**
 * This class is used to display a simple animation.
 *
 * @author Andrea Graziani and Leonardo Ratto
 * @version 1.5
 * @see Movie
 */
public class Thread_DrawingCircles extends Animation {

    private static final int MAX_CIRCLES = 25;
    private Canvas myCanvas;
    private SurfaceHolder mySurfaceHolder;
    private Handler myHandle;
    private Paint myPaint;

    /**
     * Construct a newly allocated "Thread_GIF" object
     *
     * @param arg0 - Represents a {@link SurfaceHolder} object.
     * @param arg1 - Represents a {@link Handler} object.
     */
    public Thread_DrawingCircles(SurfaceHolder arg0, Handler arg1)
    {
        this.mySurfaceHolder = arg0;
        this.myHandle = arg1;

        this.myPaint = new Paint();
        this.myPaint.setAntiAlias(true);
        this.myPaint.setStyle(Paint.Style.STROKE);
        this.myPaint.setStrokeJoin(Paint.Join.ROUND);
        this.myPaint.setStrokeWidth(10f);
    }

    /**
     * This method is called when current {@code Thread} starts
     */
    public void run() {

        try {
            // This instruction return a "Canvas" object that is used to draw into the surface. It can be "null".
            this.myCanvas = this.mySurfaceHolder.lockCanvas();

            if (this.myCanvas != null) {

                // Draw background
                this.myCanvas.drawColor(randomColor());

                if(isTouched)
                {
                    Paint p = new Paint();
                    p.setAntiAlias(true);
                    p.setColor(randomColor());
                    this.myCanvas.drawCircle(touchPosX, touchPosY, 150f, p);
                    isTouched = false;
                }

                // Draw circles...
                for(int i = 0; i < MAX_CIRCLES; i++)
                {
                    // These instruction calc a random position
                    int x = (int) (myCanvas.getWidth() * Math.random());
                    int y = (int) (myCanvas.getHeight() * Math.random());

                    // Set paint with a random color
                    myPaint.setColor(randomColor());

                    // Draw a circle
                    myCanvas.drawCircle(x, y, 45f, this.myPaint);
                }
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
                    this.myHandle.postDelayed(this, FPS);

                } catch (IllegalArgumentException e) {
                    Log.e(String.valueOf(Thread.currentThread().getId()) + ":", "Error during unlockCanvasAndPost():\n" + e.getMessage());
                    this.myHandle.removeCallbacks(this);
                }
            }
        }
    }

    /**
     * This method is used to calc a random color.
     */
    private int randomColor() {
        return Color.argb(255, (int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()));
    }
}