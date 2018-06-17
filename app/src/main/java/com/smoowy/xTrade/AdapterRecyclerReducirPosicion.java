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

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.TreeMap;

public class AdapterRecyclerReducirPosicion extends RecyclerView.Adapter<AdapterRecyclerReducirPosicion.Holder> {
    private LayoutInflater mInflater;
    String liquidezNombre, precisionLiquidez, precisionOrigen, precisionPrecio, precisionDestino,
            monedaDestinoNombre, monedaOrigenNombre;
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
            precisionOrigen = bundle.getString("precisionOrigen");
            precisionPrecio = bundle.getString("precisionPrecio");
            precisionDestino = bundle.getString("precisionDestino");
            monedaDestinoNombre = bundle.getString("monedaDestinoNombre");
            monedaOrigenNombre = bundle.getString("monedaOrigenNombre");


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

        if (dB.ganadoRedNumero >= 0) {
            holder.indicadorGanancia.setImageResource(R.drawable.ic_expand_less);
            holder.indicadorGanancia.setBackgroundResource(R.drawable.fondo_boton_reduccion_positivo);
        } else {
            holder.indicadorGanancia.setImageResource(R.drawable.ic_expand_more);
            holder.indicadorGanancia.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        }


        if (dB.getTipo() == 0) {

            holder.tipoInversion.setVisibility(View.VISIBLE);
            holder.tipoGanancia.setVisibility(View.GONE);


            holder.textoInversion.setText(String.format(precisionOrigen, dB.getInversionR()) + " " + monedaOrigenNombre);
            holder.textoPrecio.setText(String.format(precisionPrecio, dB.getPrecioRedR()) + " " + monedaOrigenNombre);
            holder.textoBase2.setText(String.format(precisionPrecio, dB.getPrecioBaseR()) + " " + monedaOrigenNombre);


            if (dB.getGanadoRedR() >= 0) {
                holder.textoGanancia2.setText(String.format(precisionOrigen, dB.getGanadoRedR()) + " " + monedaOrigenNombre);
                holder.textoGanadoLetra2.setText("Ganado");
                holder.textoGanadoLiqLetra3.setText("Ganado liq");
            } else {

                holder.textoGanancia2.setText(String.format(precisionOrigen, dB.getGanadoRedR()).substring(1) + " " + monedaOrigenNombre);
                holder.textoGanadoLetra2.setText("Perdido");
                holder.textoGanadoLiqLetra3.setText("Perdido liq");
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

                    if (dB.ganadoRedNumero > 0)
                        holder.textoGanadoLiq3.setText(String.format(precisionLiquidez, ganadoLiq) + " " + liquidezNombre);
                    else
                        holder.textoGanadoLiq3.setText(String.format(precisionLiquidez, ganadoLiq).substring(1) + " " + liquidezNombre);

                } else {

                    holder.textoGanadoLiq3.setText("Pendiente");
                }


            } else
                holder.textoGanadoLiq3.setText("Pendiente");


            holder.textoUsandoRV3.setText(String.format(precisionDestino, dB.getUsandoR()) + " " + monedaDestinoNombre);

        } else if (dB.getTipo() == 1) {


            holder.tipoInversion.setVisibility(View.GONE);
            holder.tipoGanancia.setVisibility(View.VISIBLE);


            holder.cantidadOrigenO.setText(dB.getGanadoRed());
            holder.cantidadDestinoO.setText(dB.getTextoUsando());
            holder.precioO.setText(String.format(precisionPrecio, dB.getPrecioNumero()) + " " + monedaOrigenNombre);

            if (dB.getGanadoRedNumero() >= 0) {
                holder.encabezadoOrigenO.setText("Ganancia en origen");
                holder.encabezadoDestinoO.setText("Ganancia en destino");
                holder.encabezadoLiquidezO.setText("Ganancia en liquidez");
                holder.cantidadOrigenO.setText(String.format(precisionOrigen, dB.getGanadoRedNumero()) + " " + monedaOrigenNombre);
                holder.cantidadDestinoO.setText(String.format(precisionDestino, dB.getUsandoR()) + " " + monedaDestinoNombre);


            } else {
                holder.cantidadOrigenO.setText(String.format(precisionOrigen, dB.getGanadoRedNumero()).substring(1) + " " + monedaOrigenNombre);
                holder.cantidadDestinoO.setText(String.format(precisionDestino, dB.getUsandoR()).substring(1) + " " + monedaDestinoNombre);
                holder.encabezadoOrigenO.setText("Gasto en origen");
                holder.encabezadoDestinoO.setText("Gasto en destino");
                holder.encabezadoLiquidezO.setText("Gasto en liquidez");
            }


            if (liquidezNombre != null) {

                double ganadoLiq = dB.getGanadoRedNumero();
                double ganadoDestinoLiq = ganadoLiq / dB.getPrecioNumero();

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

                    if (dB.ganadoRedNumero > 0)
                        holder.cantidadLiquidezO.setText(String.format(precisionLiquidez, ganadoLiq) + " " + liquidezNombre);
                    else
                        holder.cantidadLiquidezO.setText(String.format(precisionLiquidez, ganadoLiq).substring(1) + " " + liquidezNombre);

                } else {

                    holder.cantidadLiquidezO.setText("Pendiente");
                }


            }

        } else if (dB.getTipo() == 2) {

            holder.tipoInversion.setVisibility(View.VISIBLE);
            holder.tipoGanancia.setVisibility(View.GONE);


            holder.encabezadoPBaseRV.setText("Precio An");
            holder.textoGanadoLetra2.setText("Precio Act");
            holder.textoGanancia2.setText(String.format(precisionPrecio, dB.getPrecioNumero()) + " " + monedaOrigenNombre);
            holder.encabezadoInversionRV.setText("Inversion agregada");
            holder.encabezadoPrecioRV.setText("Precio de entrada");


            holder.textoInversion.setText(String.format(precisionOrigen, dB.getInversionR()) + " " + monedaOrigenNombre);
            holder.textoPrecio.setText(String.format(precisionPrecio, dB.getPrecioRedR()) + " " + monedaOrigenNombre);
            holder.textoBase2.setText(String.format(precisionPrecio, dB.getPrecioBaseR()) + " " + monedaOrigenNombre);
           holder.textoGanadoLiqLetra3.setText("Agregado");
            holder.textoGanadoLiq3.setText(String.format(precisionDestino, dB.getGanadoRedR()) + " " + monedaDestinoNombre);
            holder.textoUsandoRV3.setText(String.format(precisionDestino, dB.getUsandoR()) + " " + monedaDestinoNombre);

        }

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
        android.support.constraint.Group tipoInversion, tipoGanancia;
        TextView encabezadoOrigenO, cantidadOrigenO, encabezadoDestinoO,
                cantidadDestinoO, encabezadoLiquidezO, cantidadLiquidezO,
                encabezadoPrecioO, precioO, encabezadoInversionRV, encabezadoPrecioRV,
                encabezadoPBaseRV;

        Holder(final View itemView) {
            super(itemView);
            encabezadoInversionRV = itemView.findViewById(R.id.encabezadoInversionRV);
            encabezadoPrecioRV = itemView.findViewById(R.id.encabezadoPrecioRV);
            encabezadoPBaseRV = itemView.findViewById(R.id.encabezadoPBaseRV);
            textoInversion = itemView.findViewById(R.id.textoInversion);
            textoPrecio = itemView.findViewById(R.id.textoPrecio);
            textoBase2 = itemView.findViewById(R.id.textoBase2);
            textoGanancia2 = itemView.findViewById(R.id.textoGanancia2);
            textoGanadoLiq3 = itemView.findViewById(R.id.textoGanadoLiq3);
            textoUsandoRV3 = itemView.findViewById(R.id.textoUsandoRV3);
            textoGanadoLetra2 = itemView.findViewById(R.id.textoGanadoLetra2);
            textoGanadoLiqLetra3 = itemView.findViewById(R.id.textoGanadoLiqLetra3);
            indicadorGanancia = itemView.findViewById(R.id.indicadorGanancia);
            tipoInversion = itemView.findViewById(R.id.tipoInversion);
            tipoGanancia = itemView.findViewById(R.id.tipoGanancia);
            encabezadoOrigenO = itemView.findViewById(R.id.encabezadoOrigenO);
            cantidadOrigenO = itemView.findViewById(R.id.cantidadOrigenO);
            encabezadoDestinoO = itemView.findViewById(R.id.encabezadoDestinoO);
            cantidadDestinoO = itemView.findViewById(R.id.cantidadDestinoO);
            encabezadoLiquidezO = itemView.findViewById(R.id.encabezadoLiquidezO);
            cantidadLiquidezO = itemView.findViewById(R.id.cantidadLiquidezO);
            encabezadoPrecioO = itemView.findViewById(R.id.encabezadoPrecioO);
            precioO = itemView.findViewById(R.id.precioO);

        }
    }


}
