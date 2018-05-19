package com.smoowy.xTrade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.TreeMap;

public class AdapterRecyclerBotonesPorcentajes extends RecyclerView.Adapter<AdapterRecyclerBotonesPorcentajes.Holder> {
    private LayoutInflater mInflater;
    Comunicador comunicador;
    int cantBotones = 8;


    public AdapterRecyclerBotonesPorcentajes(Context context) {
        mInflater = LayoutInflater.from(context);
        comunicador = (Comunicador) context;
        crearListas();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.recycler_view_botones, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    TreeMap<Integer, String> listaBotones;
    TreeMap<Integer, Boolean> listaBotonesChecador;
    TreeMap<Integer, Integer> listaMultiplicador;

    void crearListas() {

        String[] nombres = {"10%", "5%", "2.5%", "1%", "0.50%", "0.25%", "0.10%","0.01%"};
        Integer[] multi = {1, 1, 1, 1, 2, 4, 10,100};
        listaBotones = new TreeMap<>();
        listaBotonesChecador = new TreeMap<>();
        listaMultiplicador = new TreeMap<>();
        for (int i = 0; i < cantBotones; i++) {
            listaBotones.put(i, nombres[i]);
            listaBotonesChecador.put(i, false);
            listaMultiplicador.put(i, multi[i]);
        }


    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        if (listaBotonesChecador.get(position)) {
            holder.boton.setBackgroundResource(R.drawable.fondo_boton_forex);
        } else
            holder.boton.setBackgroundResource(R.drawable.fondo_boton_forex_claro);


        holder.boton.setText(listaBotones.get(position));

    }

    @Override
    public int getItemCount() {
        return cantBotones;
    }


    class Holder extends RecyclerView.ViewHolder {

        Button boton;

        Holder(final View itemView) {
            super(itemView);
            boton = itemView.findViewById(R.id.botonPorcentaje);
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boton.setBackgroundResource(R.drawable.fondo_boton_forex);
                    comunicador.cambioPorcentaje(boton.getText().toString(), listaMultiplicador.get(getAdapterPosition()));
                    for (int i = 0; i < cantBotones; i++) {
                        listaBotonesChecador.put(i, false);
                    }
                    listaBotonesChecador.put(getAdapterPosition(), true);
                    notifyDataSetChanged();

                }
            });

        }
    }


}
