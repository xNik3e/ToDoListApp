package com.example.todoplaceholder.utils.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.todoplaceholder.utils.view_services.App;
import com.github.javafaker.Faker;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Locale;

/*
CODE FROM:
https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android

ADDITIONAL LIBRARIES:
//GUAVA FOR HASHING
    implementation("com.google.guava:guava:31.1-android")
//FAKER FOR DIFFERENT FILE NAMES
    implementation 'com.github.javafaker:javafaker:1.0.2'
 */

public class PhotoHelper {

    private static File directory;
    private static String directoryPath;


    public static File getDirectory() {
        return directory;
    }

    public static String getDirectoryPath() {
        return directoryPath;
    }

    private static void setDirectory() {
        ContextWrapper cw = new ContextWrapper(App.getContext());
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        directoryPath = directory.getAbsolutePath();
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, String filename) {
        setDirectory();
        String newFilename;
        if (filename == null)
            newFilename = getRandomFileNameString();
        else
            newFilename = hashFileName(filename);

        File myPath = new File(directory, newFilename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 75, fos);
        } catch (Exception e) {
            //do nothing
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                //
            }
        }
        return newFilename;
    }

    public static Bitmap loadImageFromStorage(String imageName) {
        setDirectory();
        Bitmap b;
        try {
            File f = new File(directoryPath, imageName);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            //
            return null;
        }
    }

    public static void deleteImageFromStorage(String filename) {
        setDirectory();
        File f = new File(directoryPath, filename);
        if(f.exists())
            f.delete();
    }

    //MY UNIQUE FILE NAME GENERATOR
    public static String getRandomFileNameString() {
        Faker faker = new Faker();
        String randomHashedName = faker.random().hex(32).toLowerCase(Locale.ROOT);

        return randomHashedName;
    }

    private static String hashFileName(String filename) {
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(filename, Charset.defaultCharset());
        String result = hasher.hash().toString();

        return result;
    }
}
