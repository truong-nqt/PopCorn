package com.truongnqt.popcorn.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter({"imageUrl", "placeHolder"})
    public static void loadImage(ImageView imageView, String url, Drawable placeHolder) {
        Glide.with(imageView.getContext())
                .load(Constants.IMAGE_LOADING_BASE_URL_1280 + url)
                .placeholder(placeHolder)
                .into(imageView);
    }


    @androidx.databinding.BindingAdapter({"image", "place"})
    public static void loadImageVideo(ImageView imageView, String url, Drawable placeHolder) {
        Glide.with(imageView.getContext())
                .load(Constants.YOUTUBE_THUMBNAIL_BASE_URL + url + Constants.YOUTUBE_THUMBNAIL_IMAGE_QUALITY)
                .placeholder(placeHolder)
                .into(imageView);
        Log.d("TAG", "loadImageVideo: " + Constants.YOUTUBE_THUMBNAIL_BASE_URL + url + Constants.YOUTUBE_THUMBNAIL_IMAGE_QUALITY);
    }
}
