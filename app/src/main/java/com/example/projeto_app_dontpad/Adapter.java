package com.example.projeto_app_dontpad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class Adapter extends BaseAdapter {

    Context context;
    Bitmap[] photos;
    LayoutInflater inflater;

    public Adapter(Context applicationContext, Bitmap[] photos){
        this.context=applicationContext;
        this.photos=photos;
        inflater=(LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return photos.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.display_main,null);
        final ImageView photo=view.findViewById(R.id.ivDisplayMain);
        photo.setImageBitmap( photos[i]);
        return view;
    }


}
