package com.example.cm.juyiz.util;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * 定制Glide缓存
 *
 * 依赖：
 * 使用OkHttp请求框架
 * compile 'com.squareup.okhttp3:okhttp:3.6.0'
 * compile 'com.github.bumptech.glide:okhttp3-integration:1.4.0@aar'
 *
 * 使用:
 * 必须在 AndroidManifest.xml 的 <application> 标签内去声明这个GlideCache
 * <meta-data
 *      android:name="包名.GlideCache"
 *      android:value="GlideModule">
 * </meta-data>
 */

public class GlideCache implements GlideModule {

    private int cacheSize = 50*1024*1024;

    public static int Dir = 0;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片的显示格式,默认使用PREFER_RGB_565
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        //自定义内存缓存
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        //自定义磁盘缓存
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //SD卡已装入
            //定义外置磁盘缓存大小
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSize));
            Dir = 1;
        }else {
            //定义内置磁盘缓存大小
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize));
            Dir = 0;
        }

        //自定义外置磁盘缓存路径与大小
//        String downloadDirectoryPath = context.getExternalCacheDir() + "/GlideCache";
//        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //配置使用OKHttp来请求网络
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
