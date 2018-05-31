package com.smoowy.xTrade;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

public class AdapterRecyclerReducirPosicion extends RecyclerView.Adapter<AdapterRecyclerReducirPosicion.Holder> {
    private LayoutInflater mInflater;
    String liquidezNombre, precisionLiquidez;
    double liquidezOrigen;
    double liquidezDestino;
    double modoLiquidez;
    double precio;
    Context context;


    public AdapterRecyclerReducirPosicion(Context context, Bundle bundle) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        crearLista();
        if (bundle != null) {
            liquidezNombre = bundle.getString("liquidezNombre");
            liquidezOrigen = bundle.getDouble("liquidezOrigen");
            liquidezDestino = bundle.getDouble("liquidezDestino");
            modoLiquidez = bundle.getInt("modoLiquidez");
            precio = bundle.getDouble("precio");
            precisionLiquidez = bundle.getString("precisionLiquidez");
        }

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

        if (dB.ganadoRedNumero > 0) {
            holder.indicadorGanancia.setImageResource(R.drawable.ic_expand_less);
            holder.indicadorGanancia.setBackgroundResource(R.drawable.fondo_boton_reduccion_positivo);
        } else {
            holder.indicadorGanancia.setImageResource(R.drawable.ic_expand_more);
            holder.indicadorGanancia.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        }

        if (liquidezNombre != null) {

            double ganadoLiq = dB.getGanadoRedNumero();
            double ganadoDestinoLiq = ganadoLiq / precio;

            if (modoLiquidez == 0) {

                ganadoLiq *= liquidezOrigen;

            } else if (modoLiquidez == 1) {

                ganadoLiq /= liquidezOrigen;

            } else if (modoLiquidez == 2) {

                ganadoLiq = ganadoDestinoLiq * liquidezDestino;


            } else if (modoLiquidez == 3) {

                ganadoLiq = ganadoDestinoLiq / liquidezDestino;

            }


            if (ganadoLiq != 0 && ganadoLiq != Double.POSITIVE_INFINITY) {
                holder.textoGanadoLiq3.setText(String.format(precisionLiquidez, ganadoLiq) + " " + liquidezNombre);
            } else {

                holder.textoGanadoLiq3.setText("Pendiente");
            }


        } else
            holder.textoGanadoLiq3.setText("Pendiente");
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
        ImageView indicadorGanancia;

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
            indicadorGanancia = itemView.findViewById(R.id.indicadorGanancia);

        }
    }


}
