package com.together.app.ui;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class UrlImageView extends ImageView {
    private int mImageWidth = -1;
    private int mImageHeight = -1;
    private String mUrl;
    private Drawable mDefaultDrawable;
    private boolean isImageLoaded = false;
    private UPUrlImageLoadCallback mCallback;

    public interface UPUrlImageLoadCallback {
        public void onLoadStarted();

        public void onLoadFinished(boolean successed);
    }

    public UrlImageView(Context context) {
        this(context, null);
    }

    public UrlImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UrlImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageFromUrl(String url) {
        if (null == url || url.equalsIgnoreCase(mUrl) && isImageLoaded) {
            return;
        }
        setImageDrawable(mDefaultDrawable);
        mUrl = url;
        isImageLoaded = false;
        new ImageLoadTask().execute(url);
    }

    public void setImageWidth(int width) {
        mImageWidth = width;
    }

    public void setImageHeight(int height) {
        mImageHeight = height;
    }

    public void setImageLoadCallback(UPUrlImageLoadCallback callback) {
        mCallback = callback;
    }

    public void setDefaultDrawable(Drawable drawable) {
        mDefaultDrawable = drawable;
        if (!isImageLoaded) {
            setImageDrawable(mDefaultDrawable);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, Integer, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                synchronized (UrlImageView.class) {
                    if (null != mCallback) {
                        mCallback.onLoadStarted();
                    }
                    InputStream is = (InputStream) new URL(params[0])
                            .getContent();
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    if (-1 != mImageWidth && -1 != mImageHeight) {
                        Bitmap target = Bitmap.createScaledBitmap(bmp,
                                mImageWidth, mImageHeight, true);
                        if (bmp != target) {
                            bmp.recycle();
                            bmp = null;
                        }
                        return new BitmapDrawable(target);
                    } else {
                        return new BitmapDrawable(bmp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (null != result) {
                isImageLoaded = true;
                setImageDrawable(result);
            }

            if (null != mCallback) {
                mCallback.onLoadFinished(null == result);
            }
        }
    }
}
