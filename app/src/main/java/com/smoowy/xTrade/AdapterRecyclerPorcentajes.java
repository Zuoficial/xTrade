package com.smoowy.xTrade;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import io.realm.Realm;

public class AdapterRecyclerPorcentajes extends
        RecyclerView.Adapter<AdapterRecyclerPorcentajes.Holder> {

    private LayoutInflater mInflater;

    ArrayList<Double> tablaPorcentajes,
            tablaPorcentajesInvertida;
    TreeMap<Integer, Informacion> tablaInformacion;
    int iPositivo, ajustadorPorcentajes, ajustador, modo;
    double invertido, invertidoFinal, ganancia, invertidoDestino,
            precio, precioFinal, porcentajeMostrar, invertidoActual,
            porcentaje, liquidezOrigen, liquidezDestino, liquidez,
            comisionEntrada, comisionSalida;
    boolean modoComprar, enForex;
    String monedaOrigenNombre, monedaDestinoNombre;
    Context context;
    String precisionOrigen, precisionDestino, liquidezNombre, precisionLiquidez, precisionPrecio;
    final int modoCazar = 0, modoCorta = 1, modoLarga = 2;


    public AdapterRecyclerPorcentajes(Context context, DB db) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        accederDB(db);
        porcentaje = .01;
        hacerListas(modo, porcentaje);
        ajustadorPorcentajes = 1;

    }


    Realm realm;
    DB db;

    private void accederDB(DB db) {
        monedaOrigenNombre = db.getMonedaOrigen();
        monedaDestinoNombre = db.getMonedaDestino();
        liquidezNombre = db.getLiquidezNombre();
        invertido = Double.parseDouble(db.getInversionInicio());
        precio = Double.parseDouble(db.getPrecioIn());
        if (db.getEnForex() != null) {
            if (db.getEnForex()) {
                enForex = true;

                comisionEntrada = Double.parseDouble(db.getComisionEntrada()) / 10000;
                comisionSalida = Double.parseDouble(db.getComisionSalida()) / 10000;


            } else {

                comisionEntrada = Double.parseDouble(db.getComisionEntrada()) / 100;
                comisionSalida = Double.parseDouble(db.getComisionSalida()) / 100;
            }
        }
        modo = db.getModo();
        precisionOrigen = db.getPrecisionOrigenFormato().replace(".", ",.");
        precisionDestino = db.getPrecisionDestinoFormato().replace(".", ",.");
        liquidezOrigen = Double.parseDouble(db.getLiquidezOrigen());
        liquidezDestino = Double.parseDouble(db.getLiquidezDestino());
        liquidezNombre = db.getLiquidezNombre();
        precisionLiquidez = db.getPrecisionLiquidezFormato().replace(".", ",.");
        precisionPrecio = db.getPrecisionPrecioFormato().replace(".", ",.");
        modo = db.getModo();
        invertidoFinal = invertido;
        invertidoDestino = (invertido / precio);

    }

    public void cambioPorcentaje(String porcentaje, Integer multiplicador) {

        this.porcentaje = Double.parseDouble(porcentaje.substring(0, porcentaje.length() - 1));
        this.porcentaje /= 100;
        this.ajustadorPorcentajes = multiplicador;
        hacerListas(modo, this.porcentaje);
        notifyDataSetChanged();
    }

    public void cambioModo(int modo) {

        hacerListas(modo, porcentaje);
        notifyDataSetChanged();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = mInflater.inflate(R.layout.recycler_view_porcentaje, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Integer positionX = position;
        Informacion info;

        if (tablaInformacion.containsKey(positionX))
            info = tablaInformacion.get(position);
        else {
            info = crearInfoIndividual(positionX);
        }

        holder.textoPorcentaje.setText(info.getPorcentajeFinal());
        holder.textoPrecio.setText(info.getPrecioFinal());
        holder.textoActual.setText(info.getActualFinal());
        holder.textoLiquidez.setText(info.getLiquidezFinal());


        if (info.getGananciaFinal().contains("+")) {
            holder.textoGanancia.setText(info.getGananciaFinal().substring(1));
            holder.textoGanadoLetra.setText("Ganado");
            holder.textoPorcentaje.setTextColor(Color.parseColor("#45c042"));
        } else {
            holder.textoGanancia.setText(info.getGananciaFinal().substring(1));
            holder.textoGanadoLetra.setText("Perdido");
            holder.textoPorcentaje.setTextColor(Color.parseColor("#e53935"));

        }

        if (modo == modoCazar) {

            holder.textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            holder.textoUsando.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
        } else {

            holder.textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            holder.textoUsando.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
        }


    }

    @Override
    public int getItemCount() {
        return 199 * ajustadorPorcentajes;
    }


 /*   void hacerListas(int modo, double porcentaje) {

        tablaPorcentajes = new ArrayList<>();
        tablaInformacion = new TreeMap<>();

        this.modo = modo;

        for (int i = 0; i < 100 * ajustadorPorcentajes; i++) {
            tablaPorcentajes.add(porcentaje * i);
        }

        tablaPorcentajesInvertida = new ArrayList<>(tablaPorcentajes);
        Collections.sort(tablaPorcentajesInvertida, Collections.<Double>reverseOrder());


        for (int i = 0; i < 199 * ajustadorPorcentajes; i++) {
            Informacion info = new Informacion();

            if (i < 100 * ajustadorPorcentajes) {

                porcentajeMostrar = tablaPorcentajesInvertida.get(i) * 100;
                precioFinal = positivo(precio, tablaPorcentajesInvertida.get(i));
                info.setPrecioFinal(String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);


                if (modo == modoCazar) {

                    invertidoActual = invertidoFinal / precioFinal;
                    ganancia = invertidoActual - invertidoDestino;
                    info.setGananciaFinal("+" + String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                    info.setActualFinal(String.format(precisionDestino, invertidoActual) + " " + monedaDestinoNombre);
                    liquidez = invertidoActual * liquidezDestino;
                    info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);
                    info.setPorcentajeFinal("+" + String.format("%.2f", porcentajeMostrar) + "%");


                } else if (modo == modoCorta || modo == modoLarga) {

                    invertidoActual = invertido * (1 + tablaPorcentajesInvertida.get(i));
                    ganancia = invertidoActual - invertidoFinal;
                    info.setGananciaFinal("+" + String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                    info.setActualFinal(String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);
                    liquidez = invertidoActual * liquidezOrigen;
                    info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);
                    info.setPorcentajeFinal("+" + String.format("%.2f", porcentajeMostrar) + "%");
                }

                tablaInformacion.put(i, info);


            } else {


                iPositivo = i - 99 * ajustadorPorcentajes;

                ajustador = 0;

                if (ajustadorPorcentajes == 2)
                    ajustador = 1;
                else if (ajustadorPorcentajes == 4)
                    ajustador = 3;
                else if (ajustadorPorcentajes == 10)
                    ajustador = 9;

                iPositivo -= ajustador;


                porcentajeMostrar = tablaPorcentajes.get(iPositivo) * 100;
                porcentajeMostrar *= -1;
                precioFinal = negativo(
                        precio, tablaPorcentajes.get(iPositivo));
                info.setPrecioFinal(String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);


                if (modo == modoCazar) {

                    invertidoActual = invertidoFinal / precioFinal;
                    ganancia = invertidoActual - invertidoDestino;
                    info.setGananciaFinal(String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                    info.setActualFinal(String.format(precisionDestino, invertidoActual) + " " + monedaDestinoNombre);
                    liquidez = invertidoActual * liquidezDestino;
                    info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);

                } else if (modo == modoLarga || modo == modoCorta) {

                    invertidoActual = invertido * (1 - tablaPorcentajes.get(iPositivo));
                    ganancia = invertidoActual - invertidoFinal;
                    info.setGananciaFinal(String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                    info.setActualFinal(String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);
                    liquidez = invertidoActual * liquidezOrigen;
                    info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);
                }

                info.setPorcentajeFinal(String.format("%.2f", porcentajeMostrar) + "%");

                tablaInformacion.put(i, info);

            }
        }


    }

*/

    void hacerListas(int modo, double porcentaje) {

        tablaPorcentajes = new ArrayList<>();
        tablaInformacion = new TreeMap<>();

        this.modo = modo;

        for (int i = 0; i < 100 * ajustadorPorcentajes; i++) {
            tablaPorcentajes.add(porcentaje * i);
        }

        tablaPorcentajesInvertida = new ArrayList<>(tablaPorcentajes);
        Collections.sort(tablaPorcentajesInvertida, Collections.<Double>reverseOrder());

    }


    Informacion crearInfoIndividual(Integer posicion) {

        Informacion info = new Informacion();

        if (posicion < 100 * ajustadorPorcentajes) {

            porcentajeMostrar = tablaPorcentajesInvertida.get(posicion) * 100;
            precioFinal = positivo(precio, tablaPorcentajesInvertida.get(posicion));
            info.setPrecioFinal(String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);


            if (modo == modoCazar) {

                invertidoActual = invertidoFinal / precioFinal;
                ganancia = invertidoActual - invertidoDestino;
                info.setGananciaFinal("+" + String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                info.setActualFinal(String.format(precisionDestino, invertidoActual) + " " + monedaDestinoNombre);
                liquidez = invertidoActual * liquidezDestino;
                info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);


            } else if (modo == modoCorta || modo == modoLarga) {

                invertidoActual = invertido * (1 + tablaPorcentajesInvertida.get(posicion));
                ganancia = invertidoActual - invertidoFinal;
                info.setGananciaFinal("+" + String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                info.setActualFinal(String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);
                liquidez = invertidoActual * liquidezOrigen;
                info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);
            }

            info.setPorcentajeFinal("+" + String.format("%.2f", porcentajeMostrar) + "%");


        } else {

            iPositivo = posicion - 99 * ajustadorPorcentajes;

            ajustador = 0;

            if (ajustadorPorcentajes == 2)
                ajustador = 1;
            else if (ajustadorPorcentajes == 4)
                ajustador = 3;
            else if (ajustadorPorcentajes == 10)
                ajustador = 9;
            else if (ajustadorPorcentajes == 100)
                ajustador = 99;

            iPositivo -= ajustador;


            porcentajeMostrar = tablaPorcentajes.get(iPositivo) * 100;
            porcentajeMostrar *= -1;
            precioFinal = negativo(precio, tablaPorcentajes.get(iPositivo));
            info.setPrecioFinal(String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);


            if (modo == modoCazar) {

                invertidoActual = invertidoFinal / precioFinal;
                ganancia = invertidoActual - invertidoDestino;
                info.setGananciaFinal(String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                info.setActualFinal(String.format(precisionDestino, invertidoActual) + " " + monedaDestinoNombre);
                liquidez = invertidoActual * liquidezDestino;
                info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);

            } else if (modo == modoLarga || modo == modoCorta) {

                invertidoActual = invertido * (1 - tablaPorcentajes.get(iPositivo));
                ganancia = invertidoActual - invertidoFinal;
                info.setGananciaFinal(String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                info.setActualFinal(String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);
                liquidez = invertidoActual * liquidezOrigen;
                info.setLiquidezFinal(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);
            }

            info.setPorcentajeFinal(String.format("%.2f", porcentajeMostrar) + "%");
        }

        tablaInformacion.put(posicion, info);

        return info;

    }


    double a, b;

    double positivo(double precio, double porcentaje) {


        if (modo == modoCazar) {
            precio = invertidoDestino * (1 + porcentaje);
            precio = invertidoFinal / precio;

        } else if (modo == modoCorta) {


            if (enForex) {
                precio -= comisionEntrada;
                precio *= (1 - porcentaje);
                precio -= comisionSalida;
            } else {
                a = invertidoDestino * (1 + porcentaje);
                a *= (1 + comisionSalida);
                a += (invertidoDestino * comisionEntrada);
                precio = invertidoFinal / a;
            }


        } else if (modo == modoLarga) {


            if (enForex) {

                precio += comisionEntrada;
                precio *= (1 + porcentaje);
                precio += comisionSalida;


            } else {
                b = invertido * (1 + porcentaje);
                b *= (1 + comisionSalida);
                b += (invertido * comisionEntrada);
                precio = b / invertidoDestino;
            }

        }


        return precio;
    }

    double negativo(double precio, double porcentaje) {


        if (modo == modoCazar) {
            precio = invertidoDestino * (1 - porcentaje);
            precio = invertidoFinal / precio;
        } else if (modo == modoCorta) {

            if (enForex) {

                precio -= comisionEntrada;
                precio *= (1 + porcentaje);
                precio -= comisionSalida;


            } else {

                a = invertidoDestino * (1 - porcentaje);
                a *= (1 + comisionSalida);
                a += (invertidoDestino * comisionEntrada);
                precio = invertidoFinal / a;
            }
        } else if (modo == modoLarga) {

            if (enForex) {

                precio += comisionEntrada;
                precio *= (1 - porcentaje);
                precio += comisionSalida;


            } else {

                b = invertido * (1 - porcentaje);
                b *= (1 + comisionSalida);
                b += (invertido * comisionEntrada);
                precio = b / invertidoDestino;
            }

        }

        return precio;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView textoPorcentaje, textoGanancia, textoPrecio, textoInvertido, textoInvertidoLetra,
                textoActual, textoUsando, textoGanadoLetra, textoLiquidez, textoLiquidezLetra,
                textoBase;
        ImageView fondo;


        Holder(View itemView) {
            super(itemView);
            textoPorcentaje = itemView.findViewById(R.id.textoPorcentaje);
            textoGanancia = itemView.findViewById(R.id.textoGanancia);
            textoPrecio = itemView.findViewById(R.id.textoPrecioMod);
            textoInvertido = itemView.findViewById(R.id.textoInvertidoRV);
            textoInvertidoLetra = itemView.findViewById(R.id.textoInvertidoLetra);
            textoActual = itemView.findViewById(R.id.textoActualRV);
            textoUsando = itemView.findViewById(R.id.textoUsandoRV);
            textoGanadoLetra = itemView.findViewById(R.id.textoGanadoLetraRV);
            textoLiquidez = itemView.findViewById(R.id.textoLiquidez);
            textoLiquidezLetra = itemView.findViewById(R.id.textoLiquidezLetra);
            textoBase = itemView.findViewById(R.id.textoBase);
            textoPrecio.setOnClickListener(onClickListener);
            textoInvertido.setOnClickListener(onClickListener);
            textoActual.setOnClickListener(onClickListener);
            textoUsando.setOnClickListener(onClickListener);
            textoLiquidez.setOnClickListener(onClickListener);
            textoBase.setOnClickListener(onClickListener);
            textoBase.setText(String.format(precisionPrecio, precio) + " " + monedaOrigenNombre);
            // fondo = itemView.findViewById(R.id.fondo);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        }


        Vibrator vibrator;
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(500);
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);


                switch (view.getId()) {

                    case R.id.textoPrecioMod: {
                        ClipData clip = ClipData.newPlainText("Precio", textoPrecio.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoPrecio.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.textoActualRV: {
                        ClipData clip = ClipData.newPlainText("Precio", textoActual.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoActual.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.textoInvertidoRV: {
                        ClipData clip = ClipData.newPlainText("Precio", textoInvertido.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoInvertido.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.textoUsandoRV: {
                        ClipData clip = ClipData.newPlainText("Precio", textoUsando.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoUsando.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.textoLiquidez: {
                        ClipData clip = ClipData.newPlainText("Precio", textoLiquidez.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoLiquidez.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.textoBase: {
                        ClipData clip = ClipData.newPlainText("Precio", textoBase.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Precio grabado: " + textoBase.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }


            }
        };

    }

    class Informacion {

        String precioFinal;
        String porcentajeFinal;
        String gananciaFinal;
        String actualFinal;
        String liquidezFinal;

        public String getPrecioFinal() {
            return precioFinal;
        }

        public void setPrecioFinal(String precioFinal) {
            this.precioFinal = precioFinal;
        }

        public String getPorcentajeFinal() {
            return porcentajeFinal;
        }

        public void setPorcentajeFinal(String porcentajeFinal) {
            this.porcentajeFinal = porcentajeFinal;
        }

        public String getGananciaFinal() {
            return gananciaFinal;
        }

        public void setGananciaFinal(String gananciaFinal) {
            this.gananciaFinal = gananciaFinal;
        }

        public String getActualFinal() {
            return actualFinal;
        }

        public void setActualFinal(String actualFinal) {
            this.actualFinal = actualFinal;
        }

        public String getLiquidezFinal() {
            return liquidezFinal;
        }

        public void setLiquidezFinal(String liquidezFinal) {
            this.liquidezFinal = liquidezFinal;
        }


    }


}
