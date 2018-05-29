package com.smoowy.xTrade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

public class AdapterRecyclerReducirPosicion extends RecyclerView.Adapter<AdapterRecyclerReducirPosicion.Holder> {
    private LayoutInflater mInflater;


    public AdapterRecyclerReducirPosicion(Context context) {
        mInflater = LayoutInflater.from(context);
        crearLista();

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.recycler_view_reducir_posicion, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    ArrayList<DBReductor> lista;
    ArrayList<DBReductor> listaRespaldo;


    void crearLista() {
        lista = new ArrayList<>();
    }

    void agregarReduccion(DBReductor dB) {

        lista.add(dB);
        notifyDataSetChanged();
    }


    void removerReduccion(int position) {
        listaRespaldo = new ArrayList<>(lista);
        lista.remove(position);
        notifyDataSetChanged();
    }

    void recuperarReduccion() {
        lista = new ArrayList<>(listaRespaldo);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {

        DBReductor dB = lista.get(position);

        holder.textoInversion.setText(dB.getInversionRed());
        holder.textoPrecio.setText(dB.getPrecioRed());
        holder.textoBase2.setText(dB.getPrecioBase());
        holder.textoGanancia2.setText(dB.getGanadoRed());
        holder.textoGanadoLiq3.setText(dB.getGanadoLiqRed());
        holder.textoUsandoRV3.setText(dB.getTextoUsando());
        holder.textoGanadoLetra2.setText(dB.getTextoGanadoRed());
        holder.textoGanadoLiqLetra3.setText(dB.getTextoGanandoLiqRed());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView textoInversion, textoPrecio, textoBase2, textoGanancia2,
                textoGanadoLiq3, textoUsandoRV3, textoGanadoLetra2,
                textoGanadoLiqLetra3;

        Holder(final View itemView) {
            super(itemView);
            textoInversion = itemView.findViewById(R.id.textoInversion);
            textoPrecio = itemView.findViewById(R.id.textoPrecio);
            textoBase2 = itemView.findViewById(R.id.textoBase2);
            textoGanancia2 = itemView.findViewById(R.id.textoGanancia2);
            textoGanadoLiq3 = itemView.findViewById(R.id.textoGanadoLiq3);
            textoUsandoRV3 = itemView.findViewById(R.id.textoUsandoRV3);
            textoGanadoLetra2 = itemView.findViewById(R.id.textoGanadoLetra2);
            textoGanadoLiqLetra3 = itemView.findViewById(R.id.textoGanadoLiqLetra3);

        }
    }


}
