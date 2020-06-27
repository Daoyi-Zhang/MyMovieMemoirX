package com.monash.mymoviememoirx.network.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.monash.mymoviememoirx.pojo.Crew;
import com.monash.mymoviememoirx.util.ImageByter;

import java.util.List;

/**
 * input list of Crews
 */
public class AssignBitmapForCrews extends AsyncTask<List<Crew>, Void, Void> {

    @Override
    protected Void doInBackground(List<Crew>... lists) {
        List<Crew> crews = lists[0];
        for (Crew c :
                crews) {
            byte[] bytes = ImageByter.getImageByte(c.getImageURL());
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            c.setCrewImage(bitmap);
        }
        return null;
    }
}
