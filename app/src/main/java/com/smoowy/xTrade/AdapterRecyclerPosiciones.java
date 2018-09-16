package com.smoowy.xTrade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRecyclerPosiciones extends RecyclerView.Adapter<AdapterRecyclerPosiciones.Holder> {
    private LayoutInflater mInflater;
    ArrayList<DB> listaDatos;
    ArrayList<DB> listaDatosRespaldo;
    Context context;
    ComunicadorPosiciones comunicador;

    public AdapterRecyclerPosiciones(Context context, ArrayList<DB> listaDatos) {
        mInflater = LayoutInflater.from(context);
        this.listaDatos = listaDatos;
        listaParaBorrar = new ArrayList<>();
        this.context = context;
        comunicador= (ComunicadorPosiciones) context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_posiciones, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        DB db = listaDatos.get(position);


        if (db.getExisteReduccion() != null) {

            if (db.getExisteReduccion())
                holder.indicadorReduccion.setVisibility(View.VISIBLE);
            else
                holder.indicadorReduccion.setVisibility(View.GONE);
        }


        switch (db.modo) {

            case 0: {
                holder.textoPosicion.setText("Cazar");
                holder.textoPosicion.setTextColor(context.getColor(R.color.blanco));
                break;
            }
            case 1: {
                holder.textoPosicion.setText("Corta");
                holder.textoPosicion.setTextColor(context.getColor(R.color.perdida));
                break;
            }
            case 2: {
                holder.textoPosicion.setText("Larga");
                break;
            }
        }

        if (db.getInversionLiqInicio() != null && !db.getInversionLiqInicio().equals("Pendiente")) {

            switch (db.modoLiquidez) {

                case 0: {
                    holder.textoIndicadorLiquidez.setText(db.getMonedaOrigen() + " -> " + db.getLiquidezNombre());

                    break;
                }
                case 1: {

                    holder.textoIndicadorLiquidez.setText(db.getLiquidezNombre() + " -> " + db.getMonedaOrigen());
                    break;
                }
                case 2: {
                    holder.textoIndicadorLiquidez.setText(db.getMonedaDestino() + " -> " + db.getLiquidezNombre());
                    break;
                }
                case 3: {
                    holder.textoIndicadorLiquidez.setText(db.getLiquidezNombre() + " -> " + db.getMonedaDestino());
                    break;
                }


            }
        } else {
            holder.textoIndicadorLiquidez.setVisibility(View.GONE);
        }


        if (db.getReferencia() != null)
            holder.textoReferencia.setText(db.getReferencia());
        else
            holder.textoReferencia.setVisibility(View.GONE);


        holder.textoParidad.setText(db.getMonedaDestino() + " con " + db.getMonedaOrigen());

        String precisionOrigenFormato = db.getPrecisionOrigenFormato().replace(".", ",.");
        String precisionPrecioFormato = db.getPrecisionPrecioFormato().replace(".", ",.");


        double precioIn = Double.parseDouble(db.getPrecioIn());
        holder.textoPrecioIn.setText(String.format(precisionPrecioFormato, precioIn)
                + " " + db.getMonedaOrigen());


        boolean existeReduccion = false;
        boolean existePrecioOut = false;

        if (db.getExisteReduccion() != null)
            existeReduccion = db.getExisteReduccion();


        if (db.getPrecioOut() != null || existeReduccion) {
            if (db.getPrecioOut() != null) {
                existePrecioOut = true;

                double precioOut = Double.parseDouble(db.getPrecioOut());
                holder.textoPrecioOut.setText(String.format(precisionPrecioFormato, precioOut)
                        + " " + db.getMonedaOrigen());
            } else
                holder.textoPrecioOut.setText("Pendiente");
            holder.textoInversion.setText(db.getInversionInicioFinal());
            holder.textoGanado.setText(db.getGanadoInicio());
            holder.textoActual.setText(db.getActualInicio());
            holder.textoInversionLiq.setText(db.getInversionLiqInicio());
            holder.textoGanadoLiq.setText(db.getGanadoLiqInicio());
            holder.textoActualLiq.setText(db.getActualLiqInicio());
            holder.textoUsando.setText(db.getUsandoInicio());


            if (!existeReduccion) {

                if (db.getGanadoFinal() < 0) {
                    holder.textoGanadoLetra.setText("Perdido");
                    holder.textoGanadoLiqLetra.setText("Perdido liq");

                } else {
                    holder.textoGanadoLetra.setText("Ganado");
                    holder.textoGanadoLiqLetra.setText("Ganado liq");
                }
            } else {

                if (existePrecioOut) {

                    if (db.getGanadoFinal() < 0) {
                        holder.textoGanadoLetra.setText("Perdido");
                        holder.textoGanadoLiqLetra.setText("Perdido liq");
                    } else if (db.getGanadoFinal() >= 0) {
                        holder.textoGanadoLetra.setText("Ganado");
                        holder.textoGanadoLiqLetra.setText("Ganado liq");
                    }
                } else {


                    if (db.getGananciaRedFinal() < 0) {
                        holder.textoGanadoLetra.setText("Perdido");
                        holder.textoGanadoLiqLetra.setText("Perdido liq");
                    } else if (db.getGananciaRedFinal() >= 0) {
                        holder.textoGanadoLetra.setText("Ganado");
                        holder.textoGanadoLiqLetra.setText("Ganado liq");
                    }

                }


            }


        } else {

            double inversion = Double.parseDouble(db.getInversionInicio());
            holder.textoInversion.setText(String.format(precisionOrigenFormato, inversion)
                    + " " + db.getMonedaOrigen());
            holder.textoPrecioOut.setText("Pendiente");
            holder.textoGanado.setText("Pendiente");
            holder.textoActual.setText("Pendiente");
            holder.textoInversionLiq.setText("Pendiente");
            holder.textoGanadoLiq.setText("Pendiente");
            holder.textoActualLiq.setText("Pendiente");
            holder.textoUsando.setText(db.getUsando() + " " + db.getMonedaDestino());

        }


    }


    @Override
    public int getItemCount() {
        return listaDatos.size();
    }


    void irTransaccion(Integer position) {

        Intent intent = new Intent(context, Calculos.class);
        intent.putExtra("idOperacion", listaDatos.get(position).getId());
        context.startActivity(intent);
        comunicador.borrarDesdeRecycler();
    }

    int posiciona;
    ArrayList<DB> listaParaBorrar;

    public void quitarDatos(Integer posicion) {

        listaDatosRespaldo = new ArrayList<>(listaDatos);
        posiciona = posicion;
        listaParaBorrar.add(listaDatos.get(posiciona));
        listaDatos.remove(posiciona);
        notifyDataSetChanged();

    }

    public void borrarDatos() {
        listaDatosRespaldo = new ArrayList<>(listaDatos);
        listaParaBorrar.clear();
        listaParaBorrar.addAll(listaDatos);
        listaDatos.clear();
        notifyDataSetChanged();
    }

    public void recuperarDatos() {
        listaDatos = new ArrayList<>(listaDatosRespaldo);
        listaParaBorrar.remove(listaParaBorrar.size() - 1);
        notifyItemInserted(posiciona);
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView textoPosicion, textoParidad, textoPrecioIn, textoPrecioOut,
                textoInversion, textoGanado, textoGanadoLetra,
                textoGanadoLiqLetra, textoGanadoLiq,
                textoActual, textoActualLiq, textoInversionLiq,
                textoUsando, textoReferencia, textoIndicadorLiquidez, indicadorReduccion;
        View contenedor;

        Holder(final View itemView) {
            super(itemView);
            textoPosicion = itemView.findViewById(R.id.textoPosicion);
            textoParidad = itemView.findViewById(R.id.textoParidad);
            textoPrecioIn = itemView.findViewById(R.id.textoPrecioIn);
            textoPrecioOut = itemView.findViewById(R.id.textoPrecioOut);
            textoInversion = itemView.findViewById(R.id.textoInversion);
            textoGanado = itemView.findViewById(R.id.textoGanado);
            textoGanadoLiq = itemView.findViewById(R.id.textoGanadoLiq);
            textoActual = itemView.findViewById(R.id.textoActual);
            textoInversionLiq = itemView.findViewById(R.id.textoInversionLiq);
            textoGanadoLetra = itemView.findViewById(R.id.textoGanadoLetra);
            textoActualLiq = itemView.findViewById(R.id.textoActualLiq);
            textoGanadoLiqLetra = itemView.findViewById(R.id.textoGanadoLiqLetra);
            textoUsando = itemView.findViewById(R.id.textoUsando);
            textoReferencia = itemView.findViewById(R.id.referencia);
            textoIndicadorLiquidez = itemView.findViewById(R.id.indicadorLiquidez);
            contenedor = itemView.findViewById(R.id.fondo);
            contenedor.setOnClickListener(onClickListener);
            indicadorReduccion = itemView.findViewById(R.id.indicadorReduccion);

        }

        View.OnClickListener onClickListener = view -> irTransaccion(getLayoutPosition());

    }
}