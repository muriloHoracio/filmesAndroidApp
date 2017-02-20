package com.filmes.murilo.filmes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 17/02/17.
 */

public class FilmeAdapter extends ArrayAdapter<Filme>{
    Context context;
    int layoutResourceId;
    ArrayList<Filme> data = null;

    public FilmeAdapter(Context context, int layoutResourceId, ArrayList<Filme> data){
        super(context,layoutResourceId,data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        FilmeHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);
            holder = new FilmeHolder();
            holder.txtNumber = (TextView) row.findViewById(R.id.txtNumber);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.txtGenre = (TextView) row.findViewById(R.id.txtGenre);
            holder.cbNet = (CheckBox) row.findViewById(R.id.cbNet);
            holder.cbAtHome = (CheckBox) row.findViewById(R.id.cbAtHome);
            row.setTag(holder);
        } else {
            holder = (FilmeHolder)row.getTag();
        }
        Filme filme = data.get(position);
        holder.txtTitle.setText(filme.getTitle());
        holder.txtNumber.setText(filme.getNumber()+"");
        holder.txtGenre.setText(filme.getGenre());
        holder.cbNet.setChecked(filme.isNet());
        holder.cbAtHome.setChecked(filme.isAthome());
        if(position%2==0){
            row.setBackgroundResource(R.color.zebra_even);
        } else {
            row.setBackgroundResource(R.color.zebra_odd);
        }
        return row;
    }

    static class FilmeHolder{
        TextView txtTitle;
        TextView txtNumber;
        TextView txtGenre;
        CheckBox cbNet;
        CheckBox cbAtHome;
    }
}
