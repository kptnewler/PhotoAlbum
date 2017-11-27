
package com.liule.photoalbum.ui.widget.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


import com.liule.photoalbum.ui.widget.photoview.scrollerproxy.ScrollerProxy;

import java.lang.ref.WeakReference;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * @author 刺雒
 * @time 2017/10/29
 */
public class PhotoViewAttacher implements IPhotoView, View.OnTouchListener{

    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    int ZOOM_DURATION = DEFAULT_ZOOM_DURATION;

    private float mMinScale = DEFAULT_MIN_SCALE;


    private static boolean hasDrawable(ImageView imageView) {
        return null != imageView && null != imageView.getDrawable();
    }



    private static void setImageViewScaleTypeMatrix(ImageView imageView) {

        if (null != imageView && !(imageView instanceof IPhotoView)) {
            if (!ScaleType.MATRIX.equals(imageView.getScaleType())) {
                imageView.setScaleType(ScaleType.MATRIX);
            }
        }
    }

    private WeakReference<ImageView> mImageView;


    private final Matrix mBaseMatrix = new Matrix();
    private final Matrix mDrawMatrix = new Matrix();
    private final Matrix mSuppMatrix = new Matrix();
    private final RectF mDisplayRect = new RectF();
    private final float[] mMatrixValues = new float[9];

    private FlingRunnable mCurrentFlingRunnable;

    private boolean mZoomEnabled;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    public PhotoViewAttacher(ImageView imageView) {
        this(imageView, true);
    }

    public PhotoViewAttacher(ImageView imageView, boolean zoomable) {
        mImageView = new WeakReference<>(imageView);

        imageView.setDrawingCacheEnabled(true);
        imageView.setOnTouchListener(this);

        // Make sure we using MATRIX Scale Type
        setImageViewScaleTypeMatrix(imageView);

        if (imageView.isInEditMode()) {
            return;
        }

        setZoomable(zoomable);
    }


    public void cleanup() {
        if (null == mImageView) {
            return; // cleanup already done
        }

        final ImageView imageView = mImageView.get();

        if (null != imageView) {

            imageView.setOnTouchListener(null);

            cancelFling();
        }

        mImageView = null;
    }

    @Override
    public RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }


    public ImageView getImageView() {
        ImageView imageView = null;

        if (null != mImageView) {
            imageView = mImageView.get();
        }

        if (null == imageView) {
            cleanup();
        }

        return imageView;
    }


    @Override
    public float getScale() {
        return (float) Math.sqrt((float) Math.pow(getValue(mSuppMatrix, Matrix.MSCALE_X), 2) + (float) Math.pow(getValue
                (mSuppMatrix, Matrix.MSKEW_Y), 2));
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        boolean handled = false;

        if (mZoomEnabled && hasDrawable((ImageView) v)) {
            ViewParent parent = v.getParent();
            switch (ev.getAction()) {
                case ACTION_DOWN:
                    if (null != parent) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }

                    cancelFling();
                    break;

                case ACTION_CANCEL:
                case ACTION_UP:
                    if (getScale() < mMinScale) {
                        RectF rect = getDisplayRect();
                        if (null != rect) {
                            v.post(new AnimatedZoomRunnable());
                            handled = true;
                        }
                    }
                    break;
            }


        }

        return handled;
    }



    @Override
    public void setScale(float scale, float focalX, float focalY,
                         boolean animate) {
        ImageView imageView = getImageView();

        if (null != imageView) {
            if (scale < mMinScale ) {
                return;
            }

            if (animate) {
                imageView.post(new AnimatedZoomRunnable());
            } else {
                mSuppMatrix.setScale(scale, scale, focalX, focalY);
                checkAndDisplayMatrix();
            }
        }
    }


    @Override
    public void setZoomable(boolean zoomable) {
        mZoomEnabled = zoomable;
        update();
    }

    public void update() {
        ImageView imageView = getImageView();

        if (null != imageView) {
            if (mZoomEnabled) {
                setImageViewScaleTypeMatrix(imageView);

                updateBaseMatrix(imageView.getDrawable());
            } else {
                resetMatrix();
            }
        }
    }


    private Matrix getDrawMatrix() {
        mDrawMatrix.set(mBaseMatrix);
        mDrawMatrix.postConcat(mSuppMatrix);
        return mDrawMatrix;
    }

    private void cancelFling() {
        if (null != mCurrentFlingRunnable) {
            mCurrentFlingRunnable.cancelFling();
            mCurrentFlingRunnable = null;
        }
    }


    private void checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(getDrawMatrix());
        }
    }

    private void checkImageViewScaleType() {
        ImageView imageView = getImageView();

        if (null != imageView && !(imageView instanceof IPhotoView)) {
            if (!ScaleType.MATRIX.equals(imageView.getScaleType())) {
                throw new IllegalStateException(
                        "The ImageView's ScaleType has been changed since attaching a PhotoViewAttacher. You should call " +
                                "setScaleType on the PhotoViewAttacher instead of on the ImageView");
            }
        }
    }

    private boolean checkMatrixBounds() {
        final ImageView imageView = getImageView();
        if (null == imageView) {
            return false;
        }

        final RectF rect = getDisplayRect(getDrawMatrix());
        if (null == rect) {
            return false;
        }

        final float height = rect.height(), width = rect.width();
        float deltaX = 0, deltaY = 0;

        final int viewHeight = getImageViewHeight(imageView);
        if (height <= viewHeight) {
            switch (mScaleType) {
                case FIT_START:
                    deltaY = -rect.top;
                    break;
                case FIT_END:
                    deltaY = viewHeight - height - rect.top;
                    break;
                default:
                    deltaY = (viewHeight - height) / 2 - rect.top;
                    break;
            }
        } else if (rect.top > 0) {
            deltaY = -rect.top;
        } else if (rect.bottom < viewHeight) {
            deltaY = viewHeight - rect.bottom;
        }

        final int viewWidth = getImageViewWidth(imageView);
        if (width <= viewWidth) {
            switch (mScaleType) {
                case FIT_START:
                    deltaX = -rect.left;
                    break;
                case FIT_END:
                    deltaX = viewWidth - width - rect.left;
                    break;
                default:
                    deltaX = (viewWidth - width) / 2 - rect.left;
                    break;
            }
        }

        mSuppMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private RectF getDisplayRect(Matrix matrix) {
        ImageView imageView = getImageView();

        if (null != imageView) {
            Drawable d = imageView.getDrawable();
            if (null != d) {
                mDisplayRect.set(0, 0, d.getIntrinsicWidth(),
                        d.getIntrinsicHeight());
                matrix.mapRect(mDisplayRect);
                return mDisplayRect;
            }
        }
        return null;
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }


    private void resetMatrix() {
        mSuppMatrix.reset();
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
    }

    private void setImageViewMatrix(Matrix matrix) {
        ImageView imageView = getImageView();
        if (null != imageView) {

            checkImageViewScaleType();
            imageView.setImageMatrix(matrix);

        }
    }

    private void updateBaseMatrix(Drawable d) {
        ImageView imageView = getImageView();
        if (null == imageView || null == d) {
            return;
        }

        final float viewWidth = getImageViewWidth(imageView);
        final float viewHeight = getImageViewHeight(imageView);
        final int drawableWidth = d.getIntrinsicWidth();
        final int drawableHeight = d.getIntrinsicHeight();

        mBaseMatrix.reset();

        final float widthScale = viewWidth / drawableWidth;
        final float heightScale = viewHeight / drawableHeight;

        if (mScaleType == ScaleType.CENTER) {
            mBaseMatrix.postTranslate((viewWidth - drawableWidth) / 2F,
                    (viewHeight - drawableHeight) / 2F);

        } else if (mScaleType == ScaleType.CENTER_CROP) {
            float scale = Math.max(widthScale, heightScale);
            mBaseMatrix.postScale(scale, scale);
            mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F,
                    (viewHeight - drawableHeight * scale) / 2F);

        } else if (mScaleType == ScaleType.CENTER_INSIDE) {
            float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
            mBaseMatrix.postScale(scale, scale);
            mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F,
                    (viewHeight - drawableHeight * scale) / 2F);

        } else {
            RectF mTempSrc = new RectF(0, 0, drawableWidth, drawableHeight);
            RectF mTempDst = new RectF(0, 0, viewWidth, viewHeight);

            switch (mScaleType) {
                case FIT_CENTER:
                    mBaseMatrix
                            .setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER);
                    break;

                case FIT_START:
                    mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.START);
                    break;

                case FIT_END:
                    mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.END);
                    break;

                case FIT_XY:
                    mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.FILL);
                    break;

                default:
                    break;
            }
        }

        resetMatrix();
    }

    private int getImageViewWidth(ImageView imageView) {
        if (null == imageView) {
            return 0;
        }
        return imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
    }

    private int getImageViewHeight(ImageView imageView) {
        if (null == imageView) {
            return 0;
        }
        return imageView.getHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom();
    }




    private class AnimatedZoomRunnable implements Runnable {

        private final long mStartTime;

        public AnimatedZoomRunnable() {

            mStartTime = System.currentTimeMillis();

        }

        @Override
        public void run() {
            ImageView imageView = getImageView();
            if (imageView == null) {
                return;
            }

            float t = interpolate();

            if (t < 1f) {
                Compat.postOnAnimation(imageView, this);
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / ZOOM_DURATION;
            t = Math.min(1f, t);
            t = mInterpolator.getInterpolation(t);
            return t;
        }
    }

    private class FlingRunnable implements Runnable {

        private final ScrollerProxy mScroller;
        private int mCurrentX, mCurrentY;

        public FlingRunnable(Context context) {
            mScroller = ScrollerProxy.getScroller(context);
        }

        public void cancelFling() {
            mScroller.forceFinished(true);
        }

        @Override
        public void run() {
            if (mScroller.isFinished()) {
                return; // remaining post that should not be handled
            }

            ImageView imageView = getImageView();
            if (null != imageView && mScroller.computeScrollOffset()) {

                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();

                mSuppMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
                setImageViewMatrix(getDrawMatrix());

                mCurrentX = newX;
                mCurrentY = newY;

                Compat.postOnAnimation(imageView, this);
            }
        }
    }
}
