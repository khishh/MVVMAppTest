package com.example.dogsappmvvm.util;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Util {

    //  error method can contains the image shown when the url image could not be loaded.
    public static void loadImages(ImageView imageView, String url, CircularProgressDrawable circularProgressDrawable){
        // setting requestOption is not mandatory
        RequestOptions options = new RequestOptions()
                .placeholder(circularProgressDrawable)
                .error(null);
//                .error(R.mipmap.ic_launcher_round);



        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(url)
                .into(imageView);
    }

    public static CircularProgressDrawable getCircularProgressDrawable(Context context){
        CircularProgressDrawable drawable = new CircularProgressDrawable(context);
        drawable.setStrokeWidth(10f);
        drawable.setCenterRadius(50f);
        drawable.start();
        return drawable;
    }

    // android:imageUrl will be the parameter value to loadImage()
    // first parameter is the type of view the adapter is attached to and the second parameter is
    // the value set to android:imageUrl attribute
    @BindingAdapter("android:imageUrl")
    public static void loadImage(ImageView imageView, String imageUrl){
        loadImages(imageView, imageUrl, getCircularProgressDrawable(imageView.getContext()));
    }
}
