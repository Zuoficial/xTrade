package com.smoowy.xTrade;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
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
    int iPositivo, ajustadorPorcentajes, ajustador, modo, modoLiquidez;
    double invertido, invertidoFinal, ganancia, invertidoDestino,
            precio, precioFinal, porcentajeMostrar, inversionActual,
            porcentaje, liquidezOrigen, liquidezDestino, liquidez,
            comisionEntrada, comisionSalida, inversionLiq, ganadoLiq, actualLiq, gananciaRedFinal;
    boolean enForex, existeReduccion, botonLotesAplanado;
    String monedaOrigenNombre, monedaDestinoNombre;
    Context context;
    String precisionOrigen, precisionDestino, liquidezNombre, precisionLiquidez, precisionPrecio;
    final int modoCazar = 0, modoCorta = 1, modoLarga = 2;
    double inversionDestinoActual;
    double gananciaDestino;
    String precioContrato;
    boolean hayContrato;


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

        if (db.getExisteReduccion() != null) {
            existeReduccion = db.getExisteReduccion();
            if (existeReduccion) {

                invertido = db.getInvertidoRedFinal();
                gananciaRedFinal = db.getGananciaRedFinal();

            } else {
                invertido = Double.parseDouble(db.getInversionInicio());
            }
        } else
            invertido = Double.parseDouble(db.getInversionInicio());


        precio = Double.parseDouble(db.getPrecioIn());
        comisionEntrada = Double.parseDouble(db.getComisionEntrada()) / 100;
        comisionSalida = Double.parseDouble(db.getComisionSalida()) / 100;
        modo = db.getModo();
        precisionOrigen = db.getPrecisionOrigenFormato().replace(".", ",.");
        precisionDestino = db.getPrecisionDestinoFormato().replace(".", ",.");
        liquidezOrigen = Double.parseDouble(db.getLiquidezOrigen());
        liquidezDestino = Double.parseDouble(db.getLiquidezDestino());
        liquidezNombre = db.getLiquidezNombre();
        precisionLiquidez = db.getPrecisionLiquidezFormato().replace(".", ",.");
        precisionPrecio = db.getPrecisionPrecioFormato().replace(".", ",.");
        modo = db.getModo();
        modoLiquidez = db.getModoLiquidez();
        invertidoFinal = invertido;
        invertidoDestino = (invertido / precio);
        if (db.getBotonLotesAplanado() != null)
            botonLotesAplanado = db.getBotonLotesAplanado();
        if (db.getHayContrato() != null)
            hayContrato = db.getHayContrato();
        if (db.getPrecioContrato() != null)
            precioContrato = db.getPrecioContrato();
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

    double comisionEntradaRespaldo, comisionSalidaRespaldo;
    boolean hayComisionCero;
    boolean botonComisionAplanado;

    public void cambioComisiones(Boolean botonComisionAplanado) {
        this.botonComisionAplanado = botonComisionAplanado;

        if (botonComisionAplanado) {
            if (comisionEntradaRespaldo == 0 && comisionSalidaRespaldo == 0) {

                if (comisionEntrada == 0 || comisionSalida == 0)
                    hayComisionCero = true;
                comisionEntradaRespaldo = comisionEntrada;
                comisionSalidaRespaldo = comisionSalida;
            }
            comisionEntrada = 0;
            comisionSalida = 0;
            hacerListas(modo, porcentaje);
            notifyDataSetChanged();
        } else {

            if (hayComisionCero) {

                comisionEntrada = comisionEntradaRespaldo;
                comisionSalida = comisionSalidaRespaldo;

            } else {

                if (comisionEntradaRespaldo > 0 && comisionSalidaRespaldo > 0) {
                    comisionEntrada = comisionEntradaRespaldo;
                    comisionSalida = comisionSalidaRespaldo;
                }

            }
            hacerListas(modo, porcentaje);
            notifyDataSetChanged();
        }
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

        if (botonComisionAplanado)
            holder.textoSinComision.setVisibility(View.VISIBLE);
        else
            holder.textoSinComision.setVisibility(View.GONE);


        if (!liquidezNombre.isEmpty()) {

            switch (modoLiquidez) {

                case 0: {
                    holder.textoIndicadorLiquidez.setText(monedaOrigenNombre + " a " + liquidezNombre);

                    break;
                }
                case 1: {

                    holder.textoIndicadorLiquidez.setText(liquidezNombre + " a " + monedaOrigenNombre);
                    break;
                }
                case 2: {
                    holder.textoIndicadorLiquidez.setText(monedaDestinoNombre + " a " + liquidezNombre);
                    break;
                }
                case 3: {
                    holder.textoIndicadorLiquidez.setText(liquidezNombre + " a " + monedaDestinoNombre);
                    break;
                }


            }
        } else {
            holder.textoIndicadorLiquidez.setVisibility(View.GONE);
        }


        holder.textoPorcentaje.setText(info.getPorcentajeFinal());

        holder.textoPrecio.setText(info.getPrecioFinal());
        holder.textoActual.setText(info.getActualFinal());
        holder.textoInversionLiq.setText(info.getInversionLiqFinal());
        holder.textoActualLiq.setText(info.getActualLiqFinal());


        if (!info.getGananciaFinal().contains("-")) {
            holder.textoGanancia.setText(info.getGananciaFinal());
            holder.textoGanadoLiq.setText(info.getGananciaLiqFinal());
            holder.textoGanadoLetra.setText("Ganado");
            holder.textoGanadoLiqLetra.setText("Ganado Liq");
            holder.textoPorcentaje.setTextColor(context.getResources().getColor(R.color.ganancia));
        } else {
            holder.textoGanancia.setText(info.getGananciaFinal().substring(1));
            holder.textoGanadoLiq.setText(!info.getGananciaLiqFinal().equals("Pendiente")
                    ? info.getGananciaLiqFinal().substring(1) : info.getGananciaLiqFinal());
            holder.textoGanadoLetra.setText("Perdido");
            holder.textoGanadoLiqLetra.setText("Perdido Liq");
            holder.textoPorcentaje.setTextColor(context.getResources().getColor(R.color.perdida));

        }

        if (modo == modoCazar) {

            holder.textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            holder.textoUsando.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
        } else {

            holder.textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            if (botonLotesAplanado)
                holder.textoUsando.setText(String.format(precisionDestino, invertidoDestino / 100000) + " " + monedaDestinoNombre);
            else
                holder.textoUsando.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
        }


    }

    @Override
    public int getItemCount() {
        return 199 * ajustadorPorcentajes;
    }


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
            precioFinal = !hayContrato ? positivo(precio, tablaPorcentajesInvertida.get(posicion)) :
                    positivo(Double.parseDouble(precioContrato), tablaPorcentajesInvertida.get(posicion));
            info.setPrecioFinal(String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);
            precioFinal = positivo(precio, tablaPorcentajesInvertida.get(posicion));


            if (modo == modoCazar) {

                inversionActual = invertidoFinal / precioFinal;
                ganancia = inversionActual - invertidoDestino;
                inversionDestinoActual = invertidoDestino * (1 + tablaPorcentajesInvertida.get(posicion));
                gananciaDestino = inversionDestinoActual - invertidoDestino;
                info.setGananciaFinal(String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                info.setActualFinal(String.format(precisionDestino, inversionActual) + " " + monedaDestinoNombre);
                chequeoLiquidez(info);


            } else if (modo == modoCorta || modo == modoLarga) {

                inversionActual = invertido * (1 + tablaPorcentajesInvertida.get(posicion));
                ganancia = inversionActual - invertidoFinal;
                inversionDestinoActual = invertidoDestino * (1 + tablaPorcentajesInvertida.get(posicion));
                gananciaDestino = inversionDestinoActual - invertidoDestino;

                if (existeReduccion) {
                    inversionActual += gananciaRedFinal;
                    ganancia += gananciaRedFinal;
                    double gananciaDestinoRed = gananciaRedFinal / precio;
                    inversionDestinoActual += gananciaDestinoRed;
                    gananciaDestino += gananciaDestinoRed;
                }


                info.setGananciaFinal(String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                info.setActualFinal(String.format(precisionOrigen, inversionActual) + " " + monedaOrigenNombre);
                chequeoLiquidez(info);

            }

            info.setPorcentajeFinal("+" + String.format("%.2f", porcentajeMostrar) + "%");


        } else {

            iPositivo = posicion - 99 * ajustadorPorcentajes;

            ajustador = 0;

            switch (ajustadorPorcentajes) {

                case 1:
                    ajustador = 0;
                    break;
                case 2:
                    ajustador = 1;
                    break;
                case 4:
                    ajustador = 3;
                    break;
                case 10:
                    ajustador = 9;
                    break;
                case 20:
                    ajustador = 19;
                    break;
                case 100:
                    ajustador = 99;
                    break;

            }

            iPositivo -= ajustador;


            porcentajeMostrar = tablaPorcentajes.get(iPositivo) * 100;
            porcentajeMostrar *= -1;
            precioFinal = !hayContrato ? negativo(precio, tablaPorcentajes.get(iPositivo)) :
                    negativo(Double.parseDouble(precioContrato), tablaPorcentajes.get(iPositivo));

            info.setPrecioFinal(String.format(precisionPrecio, precioFinal) + " " + monedaOrigenNombre);
            negativo(precio, tablaPorcentajes.get(iPositivo));


            if (modo == modoCazar) {

                inversionActual = invertidoFinal / precioFinal;
                ganancia = inversionActual - invertidoDestino;
                inversionDestinoActual = invertidoDestino * (1 - tablaPorcentajes.get(iPositivo));
                gananciaDestino = inversionDestinoActual - invertidoDestino;
                info.setGananciaFinal(String.format(precisionDestino, ganancia) + " " + monedaDestinoNombre);
                info.setActualFinal(String.format(precisionDestino, inversionActual) + " " + monedaDestinoNombre);
                chequeoLiquidez(info);


            } else if (modo == modoLarga || modo == modoCorta) {

                inversionActual = invertido * (1 - tablaPorcentajes.get(iPositivo));
                ganancia = inversionActual - invertidoFinal;
                inversionDestinoActual = invertidoDestino * (1 - tablaPorcentajes.get(iPositivo));
                gananciaDestino = inversionDestinoActual - invertidoDestino;

                if (existeReduccion) {
                    inversionActual += gananciaRedFinal;
                    ganancia += gananciaRedFinal;
                    double gananciaDestinoRed = gananciaRedFinal / precio;
                    inversionDestinoActual += gananciaDestinoRed;
                    gananciaDestino += gananciaDestinoRed;
                }
                info.setGananciaFinal(String.format(precisionOrigen, ganancia) + " " + monedaOrigenNombre);
                info.setActualFinal(String.format(precisionOrigen, inversionActual) + " " + monedaOrigenNombre);
                chequeoLiquidez(info);
            }

            info.setPorcentajeFinal(String.format("%.2f", porcentajeMostrar) + "%");
        }

        tablaInformacion.put(posicion, info);

        return info;

    }

    private void chequeoLiquidez(Informacion info) {

        if (modo == modoCazar) {

            inversionActual *= precio;
            ganancia *= precio;
        }


        switch (modoLiquidez) {
            case 0:
                inversionLiq = invertido * liquidezOrigen;
                ganadoLiq = ganancia * liquidezOrigen;
                actualLiq = inversionActual * liquidezOrigen;
                break;
            case 1:
                inversionLiq = invertido / liquidezOrigen;
                ganadoLiq = ganancia / liquidezOrigen;
                actualLiq = inversionActual / liquidezOrigen;
                break;
            case 2:
                inversionLiq = invertidoDestino * liquidezDestino;
                ganadoLiq = gananciaDestino * liquidezDestino;
                actualLiq = inversionDestinoActual * liquidezDestino;

                break;
            case 3:
                inversionLiq = invertidoDestino / liquidezDestino;
                ganadoLiq = gananciaDestino / liquidezDestino;
                actualLiq = inversionDestinoActual / liquidezDestino;
                break;
        }

        if (inversionLiq != 0 && inversionLiq != Double.POSITIVE_INFINITY) {
            info.setInversionLiqFinal(String.format(precisionLiquidez, inversionLiq) + " " + liquidezNombre);
            info.setGananciaLiqFinal(String.format(precisionLiquidez, ganadoLiq) + " " + liquidezNombre);
            info.setActualLiqFinal(String.format(precisionLiquidez, actualLiq) + " " + liquidezNombre);
        } else {

            info.setInversionLiqFinal("Pendiente");
            info.setGananciaLiqFinal("Pendiente");
            info.setActualLiqFinal("Pendiente");
        }
    }


    double positivo(double precio, double porcentaje) {


        switch (modo) {
            case modoCazar:
                precio = invertidoDestino * (1 + porcentaje);
                precio = invertidoFinal / precio;

                break;
            case modoCorta: {

               /* if (enForex) {

                    precio -= comisionEntrada;
                    precio *= (1 - porcentaje);
                    precio -= comisionSalida;

                } else {


                    double a;

                    a = 1 + porcentaje;
                    a *= (1 + comisionSalida);
                    a += comisionEntrada;
                    a -= 2;
                    a *= -1;

                    precio *= a;

                }*/

                double a;

                a = 1 + porcentaje;
                a *= (1 + comisionSalida);
                a += comisionEntrada;
                a -= 2;
                a *= -1;

                precio *= a;
                break;
            }

            case modoLarga: {


             /*   if (enForex) {

                    precio += comisionEntrada;
                    precio *= (1 + porcentaje);
                    precio += comisionSalida;

                } else {

                    double a;

                    a = 1 + porcentaje;
                    a *= 1 + comisionSalida;
                    a += comisionEntrada;
                    precio *= a;
                }*/

                double a;

                a = 1 + porcentaje;
                a *= 1 + comisionSalida;
                a += comisionEntrada;
                precio *= a;

                break;
            }
        }

        return precio;
    }

    double negativo(double precio, double porcentaje) {


        switch (modo) {
            case modoCazar:
                precio = invertidoDestino * (1 - porcentaje);
                precio = invertidoFinal / precio;

                break;
            case modoCorta: {

                /*if (enForex) {

                    precio -= comisionEntrada;
                    precio *= (1 + porcentaje);
                    precio -= comisionSalida;


                } else {

                    double a;

                    a = 1 - porcentaje;
                    a *= (1 + comisionSalida);
                    a += comisionEntrada;
                    a -= 2;
                    a *= -1;

                    precio *= a;

                }*/
                double a;

                a = 1 - porcentaje;
                a *= (1 + comisionSalida);
                a += comisionEntrada;
                a -= 2;
                a *= -1;

                precio *= a;
                break;
            }
            case modoLarga: {

              /*  if (enForex) {

                    precio += comisionEntrada;
                    precio *= (1 - porcentaje);
                    precio += comisionSalida;


                } else {

                    double a;

                    a = 1 - porcentaje;
                    a *= 1 + comisionSalida;
                    a += comisionEntrada;
                    precio *= a;

                }*/

                double a;

                a = 1 - porcentaje;
                a *= 1 + comisionSalida;
                a += comisionEntrada;
                precio *= a;
                break;
            }
        }

        return precio;
    }

    class Holder extends RecyclerView.ViewHolder {

        public TextView textoPorcentaje, textoGanancia, textoPrecio, textoInvertido, textoInvertidoLetra,
                textoActual, textoUsando, textoGanadoLetra, textoGanadoLiqLetra,
                textoBase, textoInversionLiq, textoGanadoLiq, textoActualLiq, textoIndicadorLiquidez,
                textoSinComision;
        ImageView fondo;
        boolean vibracionActivada;


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
            textoInversionLiq = itemView.findViewById(R.id.textoInversionLiq);
            textoGanadoLiqLetra = itemView.findViewById(R.id.textoGanadoLiqLetra);
            textoGanadoLiq = itemView.findViewById(R.id.textoGanadoLiq);
            textoActualLiq = itemView.findViewById(R.id.textoActualLiq);
            textoIndicadorLiquidez = itemView.findViewById(R.id.indicadorLiquidez);
            textoSinComision = itemView.findViewById(R.id.sinComision);
            textoBase = itemView.findViewById(R.id.textoBase);
            textoPrecio.setOnClickListener(onClickListener);
            textoBase.setOnClickListener(onClickListener);
            textoInvertido.setOnClickListener(onClickListener);
            textoGanancia.setOnClickListener(onClickListener);
            textoActual.setOnClickListener(onClickListener);
            textoInversionLiq.setOnClickListener(onClickListener);
            textoGanadoLiq.setOnClickListener(onClickListener);
            textoActualLiq.setOnClickListener(onClickListener);
            textoUsando.setOnClickListener(onClickListener);
            if (!hayContrato)
                textoBase.setText(String.format(precisionPrecio, precio) + " " + monedaOrigenNombre);
            else
                textoBase.setText(String.format(precisionPrecio, Double.valueOf(precioContrato)) + " " + monedaOrigenNombre);
            // fondo = itemView.findViewById(R.id.fondo);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibracionActivada = false;

            if (existeReduccion)
                itemView.findViewById(R.id.indicadorReduccion).setVisibility(View.VISIBLE);
            else
                itemView.findViewById(R.id.indicadorReduccion).setVisibility(View.GONE);


        }


        Vibrator vibrator;
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibracionActivada)
                    vibrator.vibrate(500);
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);


                switch (view.getId()) {

                    case R.id.textoPrecioMod: {
                        copyToast(clipboard, textoPrecio.getText());
                        break;
                    }
                    case R.id.textoBase: {
                        copyToast(clipboard, textoBase.getText());
                        break;
                    }

                    case R.id.textoInvertidoRV: {
                        copyToast(clipboard, textoInvertido.getText());
                        break;
                    }

                    case R.id.textoGanancia: {
                        copyToast(clipboard, textoGanancia.getText());
                    }

                    case R.id.textoActualRV: {
                        copyToast(clipboard, textoActual.getText());
                        break;
                    }

                    case R.id.textoInversionLiq: {
                        copyToast(clipboard, textoInversionLiq.getText());
                        break;
                    }

                    case R.id.textoGanadoLiq: {
                        copyToast(clipboard, textoGanadoLiq.getText());
                        break;
                    }

                    case R.id.textoActualLiq: {
                        copyToast(clipboard, textoActualLiq.getText());
                        break;
                    }

                    case R.id.textoUsandoRV: {
                        copyToast(clipboard, textoUsando.getText());
                        break;
                    }


                }


            }
        };

        private void copyToast(ClipboardManager clipboard, CharSequence text) {

            ClipData clip = ClipData.newPlainText("Precio", text.toString()
                    .replace(monedaOrigenNombre, "")
                    .replace(monedaDestinoNombre, "")
                    .replace(",", "")
                    .replace(" ", ""));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Precio grabado: " + text.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    class Informacion {

        String precioFinal;
        String porcentajeFinal;
        String gananciaFinal;
        String actualFinal;
        String inversionLiqFinal;
        String gananciaLiqFinal;
        String actualLiqFinal;

        public String getInversionLiqFinal() {
            return inversionLiqFinal;
        }

        public void setInversionLiqFinal(String inversionLiqFinal) {
            this.inversionLiqFinal = inversionLiqFinal;
        }

        public String getGananciaLiqFinal() {
            return gananciaLiqFinal;
        }

        public void setGananciaLiqFinal(String gananciaLiqFinal) {
            this.gananciaLiqFinal = gananciaLiqFinal;
        }

        public String getActualLiqFinal() {
            return actualLiqFinal;
        }

        public void setActualLiqFinal(String actualLiqFinal) {
            this.actualLiqFinal = actualLiqFinal;
        }


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


    }


}
