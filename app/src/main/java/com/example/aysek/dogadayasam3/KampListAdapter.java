package com.example.aysek.dogadayasam3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Quoc Nguyen on 13-Dec-16.
 */

public class KampListAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Kamp> kampList;

    public KampListAdapter(Context context, int layout, ArrayList<Kamp> kampList) {
        this.context = context;
        this.layout = layout;
        this.kampList = kampList;
    }

    @Override
    public int getCount() {
        return kampList.size();
    }

    @Override
    public Object getItem(int position) {
        return kampList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtBaslik, txtMetin,txtKsayisi;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtBaslik = (TextView) row.findViewById(R.id.txtbaslik);
            holder.txtMetin = (TextView) row.findViewById(R.id.txtmetin);
            holder.txtKsayisi = (TextView) row.findViewById(R.id.txtksayisi);
            holder.imageView = (ImageView) row.findViewById(R.id.imgKamp);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Kamp kamp = kampList.get(position);

        holder.txtBaslik.setText(kamp.getBaslik());
        holder.txtMetin.setText(kamp.getMetin());
        holder.txtKsayisi.setText(kamp.getKsayisi());
        byte[] kampImage = kamp.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(kampImage, 0, kampImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
