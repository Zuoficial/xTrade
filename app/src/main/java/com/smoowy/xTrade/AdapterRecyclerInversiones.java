package com.smoowy.xTrade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRecyclerInversiones extends RecyclerView.Adapter<AdapterRecyclerInversiones.Holder> {


    LayoutInflater inflater;
    int datos;
    ArrayList<DBOpInversiones> lista;
    ArrayList<DBOpInversiones> listaRespaldo;
    String monedaOrigen, monedaDestino;
    String precisionOrigenFormato, precisionDestinoFormato, precisionPrecioFormato;
    ComunicadorInversiones comunicador;
    boolean hayContrato;

    public AdapterRecyclerInversiones(Context context) {

        inflater = LayoutInflater.from(context);
        datos = 0;
        lista = new ArrayList<>();
        listaRespaldo = new ArrayList<>();
        comunicador = (ComunicadorInversiones) context;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_view_inversiones, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        if (datos == 0)
            return;

        DBOpInversiones op = lista.get(position);
        holder.inversion.setText(String.format(precisionOrigenFormato, op.getInversion()) + " " + monedaOrigen);
        holder.cantidad.setText(String.format(precisionDestinoFormato, op.getCantidad()) + " " + monedaDestino);
        holder.precio.setText(String.format(precisionPrecioFormato, op.getPrecio()) + " " + monedaOrigen);

        if (!hayContrato)
            holder.contrato.setVisibility(View.GONE);
        else {
            holder.contrato.setVisibility(View.VISIBLE);
            double precioContratoNum = Double.valueOf(op.getPrecioContrato());
            holder.precio.setText(String.format(precisionPrecioFormato, precioContratoNum) + " " + monedaOrigen);
            holder.b_contrato.setOnClickListener(view -> comunicador.recuperarInformacionContrato(op.getPrecioContrato(), op.getCantidadContrato()));
        }
    }

    @Override
    public int getItemCount() {
        return datos;
    }


    public void agregarDatos(Double precio, Double inversion, Double cantidad,
                             String precioContrato, String cantidadContrato) {
        datos += 1;
        DBOpInversiones op = new DBOpInversiones();
        op.setCantidad(cantidad);
        op.setPrecio(precio);
        op.setInversion(inversion);
        op.setPrecioContrato(precioContrato);
        op.setCantidadContrato(cantidadContrato);
        lista.add(op);
        notifyDataSetChanged();

    }


    public void quitarDatos(Integer position) {
        listaRespaldo = new ArrayList<>(lista);
        datos -= 1;
        DBOpInversiones op = lista.get(position);
        lista.remove(op);
        notifyDataSetChanged();
    }


    public void recuperarDatos() {
        lista = new ArrayList<>(listaRespaldo);
        datos += 1;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView precio, inversion, cantidad, contrato;
        ImageView cerrar, b_contrato;
        View view;

        Holder(View itemView) {
            super(itemView);
            precio = itemView.findViewById(R.id.precioRV);
            inversion = itemView.findViewById(R.id.inversionRV);
            cantidad = itemView.findViewById(R.id.cantidadRV);
            cerrar = itemView.findViewById(R.id.cerrar);
            contrato = itemView.findViewById(R.id.t_contrato_rv_inversiones);
            b_contrato = itemView.findViewById(R.id.b_contrato);
            inversion.setOnClickListener(view -> comunicador.recuperarDatosRecycler(inversion.getText().toString().replace(monedaDestino, "").replace(monedaOrigen, ""), precio.getText().toString()));
            cantidad.setOnClickListener(view -> comunicador.recuperarDatosRecycler(cantidad.getText().toString().replace(monedaDestino, "").replace(monedaOrigen, ""), precio.getText().toString()));
            precio.setOnClickListener(view -> comunicador.recuperarDatosRecycler(String.valueOf(0), precio.getText().toString()));
            cerrar.setOnClickListener(view -> comunicador.borrarInversionRecycler(getLayoutPosition()));
        }
    }


}


