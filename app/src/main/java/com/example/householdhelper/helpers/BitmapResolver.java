package com.example.householdhelper.helpers;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

/*
* authored by t7bdh3hdhb on stackoverflow
* sourced from https://stackoverflow.com/a/58300461
*
* */

/**
 * Finds a Bitmap in device storage from a Uri
 *
 * @author t7bdh3hdhb
 * @version 1.0
 * @since 2021-02-06
 */
public class BitmapResolver {

    /**
     * Returns a Bitmap for version codes below 28
     * @param contentResolver
     * @param fileUri the Uri of the file to return
     * @return
     */
    @SuppressWarnings("deprecation")
    private static Bitmap getBitmapLegacy(@NonNull ContentResolver contentResolver, @NonNull Uri fileUri){
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * Returns a Bitmap for version codes at and above 28
     * @param contentResolver
     * @param fileUri the Uri of the file to return
     * @return
     */
    @TargetApi(Build.VERSION_CODES.P)
    private static Bitmap getBitmapImageDecoder(@NonNull ContentResolver contentResolver, @NonNull Uri fileUri){
        Bitmap bitmap = null;

        try {
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * Returns a Bitmap from a given Uri
     * @param contentResolver
     * @param fileUri the Uri of the file to return
     * @return
     */
    public static Bitmap getBitmap(@NonNull ContentResolver contentResolver, Uri fileUri){
        if (fileUri == null){
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            return getBitmapImageDecoder(contentResolver, fileUri);
        } else{
            return getBitmapLegacy(contentResolver, fileUri);
        }
    }
}