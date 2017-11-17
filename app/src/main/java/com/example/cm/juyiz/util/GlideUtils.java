package com.example.cm.juyiz.util;

import android.content.Context;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cm.juyiz.R;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Glide图片加载缓存
 * <p>
 * 依赖:
 * compile 'com.github.bumptech.glide:glide:3.7.0'
 * compile 'com.android.support:support-v4:25.3.0'
 * 如需将图片进行裁剪、模糊、滤镜等处理请依赖：
 * compile 'jp.wasabeef:glide-transformations:2.0.0'
 * <p>
 * 权限:
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <p>
 * 混淆:
 * -keep public class * implements com.bumptech.glide.module.GlideModule
 * -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
 * **[] $VALUES;
 * public *;
 * }
 */

public class GlideUtils {

    /**
     * 普通加载
     *
     * @param context      上下文
     * @param imageUrls    图片路径
     * @param loadingimgID 加载中或者加载失败显示的图片
     * @param mImageView   ImageView
     */
    public static void loadImage(Context context, String imageUrls, int loadingimgID, ImageView mImageView) {
        Glide
                .with(context)
                .load(imageUrls)
                .placeholder(loadingimgID) //加载中显示的图片
                .error(loadingimgID) //加载失败时显示的图片
                .crossFade(500) //淡入淡出,注意:如果设置了这个,则必须要去掉asBitmap
                .centerCrop() //中心裁剪,缩放填充至整个ImageView
                .skipMemoryCache(false) //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) //保存最终图片
                .into(mImageView);
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }


    /**
     * 圆形裁剪加载
     *
     * @param context    上下文
     * @param imageUrls  图片路径
     * @param mImageView ImageView
     */
    public static void loadCircleImage(Context context, String imageUrls, ImageView mImageView) {
        Glide
                .with(context)
                .load(imageUrls)
                .placeholder(R.drawable.login)
                .error(R.drawable.login)
                .crossFade(1000)
                .centerCrop()
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                .into(mImageView);
    }

    /**
     * * 圆角处理加载
     *
     * @param context    上下文
     * @param imageUrls  图片路径
     * @param mImageView ImageView
     * @param radius     圆角半径
     * @param margin     外边距
     */
    public static void loadRoundedImage(Context context, String imageUrls, ImageView mImageView, int radius, int margin) {
        Glide
                .with(context)
                .load(imageUrls)
                .placeholder(R.drawable.homemain_banner)
                .error(R.drawable.homemain_banner)
                .crossFade(1000)
                .centerCrop()
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new RoundedCornersTransformation(Glide.get(context).getBitmapPool(), radius, margin))
                .into(mImageView);
    }

    /**
     * 灰度处理加载
     *
     * @param context    上下文
     * @param imageUrls  图片路径
     * @param mImageView ImageView
     */
    public static void loadGraysImage(Context context, String imageUrls, ImageView mImageView) {
        Glide
                .with(context)
                .load(imageUrls)
                .placeholder(R.drawable.homemain_banner)
                .error(R.drawable.homemain_banner)
                .crossFade(1000)
                .centerCrop()
//                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new GrayscaleTransformation(Glide.get(context).getBitmapPool()))
                .into(mImageView);
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存路径
     *
     * @param context   上下文
     * @param cacheName 缓存文件名 默认请使用: DiskLruCacheFactory.DEFAULT_DISK_CACHE_DIR
     * @return File
     */
    public static File getCacheDirectory(Context context, String cacheName) {

        Glide.getPhotoCacheDir(context, cacheName);

        File cacheDirectory;

        if (GlideCache.Dir == 1) { //SD卡已装入
            cacheDirectory = context.getExternalCacheDir();

        } else {
            cacheDirectory = context.getCacheDir();
        }
        if (cacheDirectory != null) {
            File result = new File(cacheDirectory, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                return null;
            }
            return result;
        }
        return null;
    }
}
