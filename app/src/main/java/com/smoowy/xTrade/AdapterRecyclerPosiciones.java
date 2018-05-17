package com.smoowy.xTrade;

import android.content.Context;
import android.content.Intent;
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

    public AdapterRecyclerPosiciones(Context context, ArrayList<DB> listaDatos) {
        mInflater = LayoutInflater.from(context);
        this.listaDatos = listaDatos;
        listaParaBorrar = new ArrayList<>();
        this.context = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_posiciones, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        DB db = listaDatos.get(position);


        switch (db.modo) {

            case 0: {
                holder.textoPosicion.setText("Cazar");
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
        if (db.getReferencia() != null) {
            holder.textoReferencia.setText(db.getReferencia());
        }
        else
            holder.textoReferencia.setVisibility(View.GONE);


        holder.textoParidad.setText(db.getMonedaDestino() + " con " + db.getMonedaOrigen());

        String precisionOrigenFormato = db.getPrecisionOrigenFormato().replace(".", ",.");
        String precisionPrecioFormato = db.getPrecisionPrecioFormato().replace(".",",.");

        if (db.getPrecioIn() == null)
            holder.textoPrecioIn.setText("Pendiente");
        else {

            double b = Double.parseDouble(db.getPrecioIn());
            holder.textoPrecioIn.setText(String.format(precisionPrecioFormato, b) + " " + db.getMonedaOrigen());

        }

        if (db.getPrecioOut() == null)
            holder.textoPrecioOut.setText("Pendiente");
        else {

            double b = Double.parseDouble(db.getPrecioOut());
            holder.textoPrecioOut.setText(String.format(precisionPrecioFormato, b) + " " + db.getMonedaOrigen());


        }


        if (db.getInversionInicio() == null)
            holder.textoInversion.setText("Pendiente");
        else {

            double a = Double.parseDouble(db.getInversionInicio());

            holder.textoInversion.setText(String.format(precisionOrigenFormato, a) + " " + db.getMonedaOrigen());


        }


        if (db.getGanadoInicio() == null)
            holder.textoGanado.setText("Pendiente");
        else {

            double a = Double.parseDouble(db.getGanadoInicio());

            if (a < 0) {
                holder.textoGanadoLetra.setText("Perdido");
                a *= -1;

            } else
                holder.textoGanadoLetra.setText("Ganado");


            holder.textoGanado.setText(String.format(precisionOrigenFormato, a) + " " + db.getMonedaOrigen());

        }


        if (db.getActualInicio() == null)
            holder.textoActual.setText("Pendiente");
        else {

            double a = Double.parseDouble(db.getActualInicio());

            holder.textoActual.setText(String.format(precisionOrigenFormato, a) + " " + db.getMonedaOrigen());

        }


        if (db.getInversionLiqInicio() == null)
            holder.textoInversionLiq.setText("Pendiente");
        else
            holder.textoInversionLiq.setText(db.getInversionLiqInicio());


        if (db.liquidezOrigen != null || db.getInvertido() != null || db.getGanadoInicio() != null) {

            String precisionLiquidezFormato = db.getPrecisionLiquidezFormato().replace(".", ",.");

            double inversion = Double.parseDouble(db.getInversionInicio());
            double ganado = Double.parseDouble(db.getGanadoInicio());
            double actual = Double.parseDouble(db.getActualInicio());
            double liquidez = Double.parseDouble(db.getLiquidezOrigen());


            inversion *= liquidez;
            ganado *= liquidez;
            actual *= liquidez;

            holder.textoInversionLiq.setText(String.format(precisionLiquidezFormato, inversion) + " " + db.getLiquidezNombre());


            if (ganado < 0) {
                holder.textoGanadoLiqLetra.setText("Perdido");
                ganado *= -1;

            } else
                holder.textoGanadoLiqLetra.setText("Ganado");


            holder.textoGanadoLiq.setText(String.format(precisionLiquidezFormato, ganado) + " " + db.getLiquidezNombre());
            holder.textoActualLiq.setText(String.format(precisionLiquidezFormato, actual) + " " + db.getLiquidezNombre());


        } else {

            holder.textoInversionLiq.setText("Pendiente");
            holder.textoGanadoLiq.setText("Pendiente");
            holder.textoActualLiq.setText("Pendiente");

        }
        if (db.getUsando() == null)
            holder.textoUsando.setText("Pendiente");
        else
            holder.textoUsando.setText(db.getUsando() + " " + db.getMonedaDestino());


    }


    @Override
    public int getItemCount() {
        return listaDatos.size();
    }


    void irTransaccion(Integer position) {

        Intent intent = new Intent(context, Calculos.class);
        intent.putExtra("idOperacion", listaDatos.get(position).getId());
        context.startActivity(intent);
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

    public void recuperarDatos() {
        listaDatos = new ArrayList<>(listaDatosRespaldo);
        listaParaBorrar.remove(listaParaBorrar.size() - 1);
        notifyItemInserted(posiciona);
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView textoPosicion, textoParidad, textoPrecioIn, textoPrecioOut,
                textoInversion, textoGanado, textoGanadoLetra,
                textoGanadoLiqLetra, textoGanadoLiq,
                textoActual, textoActualLiq, textoInversionLiq, textoUsando,textoReferencia;
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
            contenedor = itemView.findViewById(R.id.fondo);
            contenedor.setOnClickListener(onClickListener);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irTransaccion(getLayoutPosition());
            }
        };

    }
}