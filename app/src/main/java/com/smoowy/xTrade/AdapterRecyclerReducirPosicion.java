package com.smoowy.xTrade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.TreeMap;

public class AdapterRecyclerReducirPosicion extends RecyclerView.Adapter<AdapterRecyclerReducirPosicion.Holder> {
    private LayoutInflater mInflater;


    public AdapterRecyclerReducirPosicion(Context context) {
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.recycler_view_reducir_posicion, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }




    @Override
    public void onBindViewHolder(Holder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 2;
    }


    class Holder extends RecyclerView.ViewHolder {

        Holder(final View itemView) {
            super(itemView);

        }
    }


}
