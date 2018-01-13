package com.example.user.navigationdrawerexample.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by user on 1/11/2018.
 *
 */
//this class is used to display an Image in a circular fashion
public class CircleTransformation extends BitmapTransformation {
    public CircleTransformation(Context context) {
        super(context);
    }

    public CircleTransformation(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap toTransform) {
        if (toTransform == null)
            return null;
        int size = Math.min(toTransform.getWidth(),toTransform.getHeight());
        int x = (toTransform.getWidth())/2;
        int y = (toTransform.getHeight())/2;
        Bitmap squared = Bitmap.createBitmap(toTransform, x ,y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null){
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size/2f;
        canvas.drawCircle(r, r, r, paint );
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
