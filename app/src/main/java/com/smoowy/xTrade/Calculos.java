package com.smoowy.xTrade;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Calculos extends AppCompatActivity implements ComunicadorBotonPorcentajes {

    RecyclerView recyclerBotonesPorcentajes, recyclerPorcentajes;
    AdapterRecyclerBotonesPorcentajes adapterRecyclerBotonesPorcentajes;
    AdapterRecyclerPorcentajes adapterRecyclerPorcentajes;
    Button botonCazar, botonCorta, botonLarga, botonClear, botonPorcentajes,
            botonDuplicar, botonPorcentajeCalculador,
            botonPorcentajeCalculadorMenos, botonPorcentajeCalculadorMas,
            botonModificar, botonComisiones, botonReducir;
    TextView encabezado, textoGanancia, textoInvertido, textoInvertidoActual, textoUsado, textoGananciaLetra,
            textoPorcentaje, textoPrecio, textoBase, textoInversionLiq,
            textoGanadoLiq, textoActualLiq, textoGanadoLiqLetra, textoSinComision,
            textoIndicadorLiquidez, textoReduccion;
    String monedaOrigenNombre, monedaDestinoNombre, referencia;
    EditText textoPrecioMod, textoPorcentajeMod, textoReferencia;
    double invertido, precio, invertidoDestino, precioFinal,
            precioIngresado, porcentajeFinal, gananciaFinal, invertidoActual, porcentajeIngresado,
            liquidezOrigen, liquidezDestino, comisionEntrada, comisionSalida, invertidoFinal, gananciaRedFinal;
    int ajustadorPorcentajes, modo, modoLiquidez, idOperacion;
    final int modoCazar = 0, modoCorta = 1, modoLarga = 2;
    boolean botonPorcentajesAplanado,
            botonporcentajeCalculadorAplanado, botonPorcentajeCalculadorMasAplanado, enForex,
            botonComisionAplanado, botonReducirAplanado, existeReduccion, cargaInicialTerminada;
    Vibrator vibrator;
    String precisionOrigen, precisionDestino, precisionLiquidez, precisionPrecio,
            liquidezNombre;
    ConstraintLayout calculador;
    View header;
    DrawerLayout drawer;
    double inversionLiq;
    double ganadoLiq;
    double actualLiq;
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_calculos);
        drawer = findViewById(R.id.drawer_layout);
        encabezado = findViewById(R.id.encabezado);
        encabezado.setOnTouchListener(onTouchListener);
        calculador = findViewById(R.id.calculador);
        navigationView = findViewById(R.id.navigation);
        header = navigationView.getHeaderView(0);
        accederDB();
        invertidoFinal = invertido;
        botonporcentajeCalculadorAplanado = false;
        botonPorcentajeCalculadorMasAplanado = true;
        setRecyclerViewRecyclerBotonesPorcentajes();
        setRecyclerViewRecyclerPorcentajes();
        botonCazar = findViewById(R.id.botonCazar);
        botonCorta = findViewById(R.id.botonCorta);
        botonLarga = findViewById(R.id.botonLarga);
        botonClear = findViewById(R.id.botonClear);
        botonPorcentajes = findViewById(R.id.botonPorcentajes);
        botonDuplicar = findViewById(R.id.botonDuplicar);
        botonPorcentajeCalculador = findViewById(R.id.botonPorcentajeCalculador);
        botonPorcentajeCalculadorMas = findViewById(R.id.botonPorcentajeCalculadorMas);
        botonPorcentajeCalculadorMenos = findViewById(R.id.botonPorcentajeCalculadorMenos);
        botonModificar = findViewById(R.id.botonModificar);
        botonComisiones = findViewById(R.id.botonComisiones);
        botonReducir = findViewById(R.id.botonReducir);
        botonCazar.setOnTouchListener(onTouchListener);
        botonCorta.setOnTouchListener(onTouchListener);
        botonLarga.setOnTouchListener(onTouchListener);
        botonDuplicar.setOnTouchListener(onTouchListener);
        botonPorcentajes.setOnTouchListener(onTouchListener);
        botonClear.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculador.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculadorMas.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculadorMenos.setOnTouchListener(onTouchListener);
        botonModificar.setOnTouchListener(onTouchListener);
        botonComisiones.setOnTouchListener(onTouchListener);
        botonReducir.setOnTouchListener(onTouchListener);
        textoPorcentaje = findViewById(R.id.textoPorcentaje);
        textoPorcentajeMod = findViewById(R.id.textoPorcentajeMod);
        textoPorcentajeMod.addTextChangedListener(textWatcher);
        textoGanancia = findViewById(R.id.textoGanancia);
        textoPrecio = findViewById(R.id.textoPrecio);
        textoPrecioMod = findViewById(R.id.textoPrecioMod);
        textoPrecioMod.addTextChangedListener(textWatcher);
        textoReferencia = findViewById(R.id.referencia);
        if (referencia != null)
            textoReferencia.setText(referencia);
        textoInvertido = findViewById(R.id.textoInvertido);
        textoInvertidoActual = findViewById(R.id.textoInvertidoActual);
        textoUsado = findViewById(R.id.textoUsandoRV2);
        textoGananciaLetra = findViewById(R.id.textoGanadoLetra);
        textoBase = findViewById(R.id.textoBase);
        textoBase.setText(String.format(precisionPrecio, precio) + " " + monedaOrigenNombre);
        textoGanadoLiq = findViewById(R.id.textoGanadoLiq2);
        textoGanadoLiqLetra = findViewById(R.id.textoGanadoLiqLetra2);
        textoInversionLiq = findViewById(R.id.textoInversionLiq2);
        textoActualLiq = findViewById(R.id.textoActualLiq2);
        textoSinComision = findViewById(R.id.textoSinComision);
        textoIndicadorLiquidez = findViewById(R.id.textoIndicadorLiquidez);
        textoReduccion = findViewById(R.id.textoReduccion);
        textoPrecio.setOnTouchListener(onTouchListener);
        textoBase.setOnTouchListener(onTouchListener);
        textoInvertido.setOnTouchListener(onTouchListener);
        textoGanancia.setOnTouchListener(onTouchListener);
        textoInvertidoActual.setOnTouchListener(onTouchListener);
        textoActualLiq.setOnTouchListener(onTouchListener);
        textoGanadoLiq.setOnTouchListener(onTouchListener);
        textoInversionLiq.setOnTouchListener(onTouchListener);
        textoUsado.setOnTouchListener(onTouchListener);
        header.setOnTouchListener(onTouchListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        switch (modo) {
            case modoCazar:
                setBotonCazar();
                break;
            case modoCorta:
                setBotonCorta();
                break;
            case modoLarga:
                setBotonLarga();
                break;
        }

        setBotonComision();
        setBotonPorcentajesAplanado();
        setindicadorLiquidez();
        ajustadorPorcentajes = 1;
        ajustadorPosicion(ajustadorPorcentajes);
        cargaInicialTerminada = true;
        limpiarCalculador();

        if (db.getPrecioOut() != null) {
            if (!db.getPrecioOut().isEmpty())
                textoPrecioMod.setText(db.getPrecioOut());
        }
    }

    DB db;

    private void accederDB() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        idOperacion = getIntent().getExtras().getInt("idOperacion");
        db = realm.where(DB.class).equalTo("id", idOperacion).findFirst();
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
        invertidoDestino = invertido / precio;


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
        modoLiquidez = db.getModoLiquidez();
        botonPorcentajesAplanado = !db.getBotonPorcentajesAplanado();
        botonComisionAplanado = !db.getBotonComisionAplanado();
        precisionOrigen = db.getPrecisionOrigenFormato().replace(".", ",.");
        precisionDestino = db.getPrecisionDestinoFormato().replace(".", ",.");
        precisionLiquidez = db.getPrecisionLiquidezFormato().replace(".", ",.");
        precisionPrecio = db.getPrecisionPrecioFormato().replace(".", ",.");
        liquidezOrigen = Double.parseDouble(db.getLiquidezOrigen());
        liquidezDestino = Double.parseDouble(db.getLiquidezDestino());

        if (db.getReferencia() != null) {
            referencia = db.getReferencia();
        }

        realm.close();

    }


    boolean positivo;
    String textoGanadoGuardar;
    String textoActualGuardar;
    boolean enTextWatcher;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (botonporcentajeCalculadorAplanado) {

                if (textoPorcentajeMod.getText().toString().equals(".") ||
                        textoPorcentajeMod.getText().toString().isEmpty()
                        ) {
                    enTextWatcher = true;
                    if (cargaInicialTerminada)
                        limpiarCalculador();
                    enTextWatcher = false;
                    return;
                }

            } else {
                if (textoPrecioMod.getText().toString().equals(".") ||
                        textoPrecioMod.getText().toString().isEmpty()
                        ) {


                    enTextWatcher = true;
                    if (cargaInicialTerminada)
                        limpiarCalculador();
                    enTextWatcher = false;
                    return;
                }
            }

            if (botonporcentajeCalculadorAplanado)
                porcentajeIngresado = (Double.parseDouble(textoPorcentajeMod.getText().toString()));
            else
                precioIngresado = (Double.parseDouble(textoPrecioMod.getText().toString()));


            if (modo == modoCorta || modo == modoLarga) {

                if (modo == modoCorta)
                    calculoModoCorta();
                else
                    calculoModoLarga();


                if (inversionLiq != 0 && inversionLiq != Double.POSITIVE_INFINITY) {
                    textoInversionLiq.setText(String.format(precisionLiquidez, inversionLiq) + " " + liquidezNombre);
                    textoGanadoLiq.setText(positivo ? String.format(precisionLiquidez, ganadoLiq) + " " + liquidezNombre :
                            String.format(precisionLiquidez, ganadoLiq).substring(1) + " " + liquidezNombre);
                    textoActualLiq.setText(String.format(precisionLiquidez, actualLiq) + " " + liquidezNombre);

                } else {

                    textoInversionLiq.setText("Pendiente");
                    textoGanadoLiq.setText("Pendiente");
                    textoActualLiq.setText("Pendiente");
                }


                if (botonporcentajeCalculadorAplanado)
                    textoPrecio.setText(String.format(precisionPrecio, precioFinal));

                if (positivo) {
                    textoGanadoGuardar = String.format(precisionOrigen, gananciaFinal);
                    textoGanancia.setText(textoGanadoGuardar + " " + monedaOrigenNombre);
                    textoPorcentaje.setText("+" + String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Ganado");
                    textoGanadoLiqLetra.setText("Ganado liq");
                } else {
                    textoGanadoGuardar = String.format(precisionOrigen, gananciaFinal);
                    textoGanancia.setText(textoGanadoGuardar.substring(1) + " " + monedaOrigenNombre);
                    textoPorcentaje.setText(String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Perdido");
                    textoGanadoLiqLetra.setText("Perdido liq");

                }
                textoActualGuardar = String.format(precisionOrigen, invertidoActual);
                textoInvertidoActual.setText(textoActualGuardar + " " + monedaOrigenNombre);

            } else {

                calculoModoCazar();


                if (inversionLiq != 0 && inversionLiq != Double.POSITIVE_INFINITY) {
                    textoInversionLiq.setText(String.format(precisionLiquidez, inversionLiq) + " " + liquidezNombre);
                    textoGanadoLiq.setText(positivo ? String.format(precisionLiquidez, ganadoLiq) + " " + liquidezNombre :
                            String.format(precisionLiquidez, ganadoLiq).substring(1) + " " + liquidezNombre);
                    textoActualLiq.setText(String.format(precisionLiquidez, actualLiq) + " " + liquidezNombre);

                } else {

                    textoInversionLiq.setText("Pendiente");
                    textoGanadoLiq.setText("Pendiente");
                    textoActualLiq.setText("Pendiente");
                }


                if (botonporcentajeCalculadorAplanado) {
                    textoPrecio.setText(String.format(precisionPrecio, precioIngresado));

                }

                if (positivo) {
                    textoGanadoGuardar = String.format(precisionDestino, gananciaFinal);
                    textoGanancia.setText(textoGanadoGuardar + " " + monedaDestinoNombre);
                    textoPorcentaje.setText("+" + String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Ganacia");

                } else {
                    textoGanadoGuardar = String.format(precisionDestino, gananciaFinal);
                    textoGanancia.setText(textoGanadoGuardar.substring(1) + " " + monedaDestinoNombre);
                    textoPorcentaje.setText(String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Perdido");
                }

                textoActualGuardar = String.format(precisionDestino, invertidoActual);
                textoInvertidoActual.setText(textoActualGuardar + " " + monedaDestinoNombre);

            }

        }


        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    private void chequeoLiquidez() {

        if (modo == modoCazar) {

            invertidoActual *= precio;
            gananciaFinal *= precio;
        }


        switch (modoLiquidez) {
            case 0:
                inversionLiq = invertido * liquidezOrigen;
                ganadoLiq = gananciaFinal * liquidezOrigen;
                actualLiq = invertidoActual * liquidezOrigen;
                break;
            case 1:
                inversionLiq = invertido / liquidezOrigen;
                ganadoLiq = gananciaFinal / liquidezOrigen;
                actualLiq = invertidoActual / liquidezOrigen;
                break;
            case 2:
                inversionLiq = invertidoDestino * liquidezDestino;
                ganadoLiq = inversionLiq * (porcentajeFinal / 100);
                actualLiq = inversionLiq + ganadoLiq;
                break;
            case 3:
                inversionLiq = invertidoDestino / liquidezDestino;
                ganadoLiq = inversionLiq * (porcentajeFinal / 100);
                actualLiq = inversionLiq + ganadoLiq;
                break;
        }
    }


    void setindicadorLiquidez() {

        if (!liquidezNombre.isEmpty()) {

            switch (modoLiquidez) {

                case 0: {
                    textoIndicadorLiquidez.setText(monedaOrigenNombre + " -> " + liquidezNombre);

                    break;
                }
                case 1: {

                    textoIndicadorLiquidez.setText(liquidezNombre + " -> " + monedaOrigenNombre);
                    break;
                }
                case 2: {
                    textoIndicadorLiquidez.setText(monedaDestinoNombre + " -> " + liquidezNombre);
                    break;
                }
                case 3: {
                    textoIndicadorLiquidez.setText(liquidezNombre + " -> " + monedaDestinoNombre);
                    break;
                }


            }
        } else {
            textoIndicadorLiquidez.setVisibility(View.GONE);
        }

    }


    private void calculoModoCazar() {


        if (botonporcentajeCalculadorAplanado) {

            porcentajeFinal = porcentajeIngresado / 100;

            if (botonPorcentajeCalculadorMasAplanado) {

                precioIngresado = invertidoDestino * (1 + porcentajeFinal);
                precioIngresado = invertidoFinal / precioIngresado;
            } else {
                precioIngresado = invertidoDestino * (1 - porcentajeFinal);
                precioIngresado = invertidoFinal / precioIngresado;
                porcentajeFinal *= -1;
            }


            invertidoActual = invertidoFinal / precioIngresado;
            gananciaFinal = invertidoActual - invertidoDestino;


        } else {

            invertidoActual = invertidoFinal / precioIngresado;
            gananciaFinal = invertidoActual - invertidoDestino;
            porcentajeFinal = invertidoActual / invertidoDestino;
            porcentajeFinal -= 1;
            porcentajeFinal *= 100;
        }


        chequeoLiquidez();
        positivo = gananciaFinal >= 0;


    }


    private void calculoModoCorta() {


        if (botonporcentajeCalculadorAplanado) {


            if (!botonPorcentajeCalculadorMasAplanado)
                porcentajeIngresado *= -1;

            porcentajeIngresado /= 100;


            if (enForex) {

                precioFinal = precio - comisionEntrada;
                precioFinal *= 1 - porcentajeIngresado;
                precioFinal -= comisionSalida;


            } else {

                double b;
                b = 1 + porcentajeIngresado;
                b *= (1 + comisionSalida);
                b += comisionEntrada;
                b -= 2;
                b *= -1;

                precioFinal = precio * b;
            }


            invertidoActual = invertido * (1 + porcentajeIngresado);
            gananciaFinal = invertidoActual - invertidoFinal;
            if (existeReduccion) {
                invertidoActual += gananciaRedFinal;
                gananciaFinal += gananciaRedFinal;
            }
            positivo = gananciaFinal >= 0;


        } else {

            if (enForex) {

                double b = precioIngresado + comisionSalida;
                double a = precio - comisionEntrada;
                porcentajeFinal = b / a;
                porcentajeFinal -= 1;
                porcentajeFinal *= -1;


            } else {

                double b;

                b = precioIngresado / precio;
                b += -2;
                b *= -1;
                b -= comisionEntrada;
                b /= (1 + comisionSalida);

                porcentajeFinal = b - 1;

            }


            invertidoActual = invertido * (1 + porcentajeFinal);
            gananciaFinal = invertidoActual - invertidoFinal;
            if (existeReduccion) {
                invertidoActual += gananciaRedFinal;
                gananciaFinal += gananciaRedFinal;
            }
            porcentajeFinal *= 100;
            positivo = gananciaFinal >= 0;

        }
        chequeoLiquidez();

    }


    private void calculoModoLarga() {


        if (botonporcentajeCalculadorAplanado) {


            if (!botonPorcentajeCalculadorMasAplanado)
                porcentajeIngresado *= -1;

            porcentajeIngresado /= 100;


            if (enForex) {

                precioFinal = precio + comisionEntrada;
                precioFinal *= 1 + porcentajeIngresado;
                precioFinal += comisionSalida;


            } else {

                double a;
                a = 1 + porcentajeIngresado;
                a *= 1 + comisionSalida;
                a += comisionEntrada;

                precioFinal = precio * a;
            }


            invertidoActual = invertido * (1 + porcentajeIngresado);
            gananciaFinal = invertidoActual - invertidoFinal;
            if (existeReduccion) {
                invertidoActual += gananciaRedFinal;
                gananciaFinal += gananciaRedFinal;
            }
            positivo = gananciaFinal >= 0;


        } else {

            if (enForex) {

                double b = precioIngresado - comisionSalida;
                double a = precio + comisionEntrada;
                porcentajeFinal = b / a;
                porcentajeFinal -= 1;


            } else {

                double b;
                b = precioIngresado / precio;
                b -= comisionEntrada;
                b /= (1 + comisionSalida);
                porcentajeFinal = b - 1;


            }

            invertidoActual = invertido * (1 + (porcentajeFinal));
            gananciaFinal = invertidoActual - invertidoFinal;
            if (existeReduccion) {
                invertidoActual += gananciaRedFinal;
                gananciaFinal += gananciaRedFinal;
            }
            porcentajeFinal *= 100;
            positivo = gananciaFinal >= 0;


        }

        chequeoLiquidez();


    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                switch (view.getId()) {


                    case R.id.encabezado: {
                        drawer.openDrawer(Gravity.START);
                        break;
                    }


                    case R.id.botonCazar: {
                        drawer.closeDrawer(Gravity.START);
                        if (modo == modoCazar) {
                            ajustadorPosicion(ajustadorPorcentajes);
                            break;

                        } else {
                            setBotonCazar();
                            ajustadorPosicion(ajustadorPorcentajes);
                        }
                        break;

                    }

                    case R.id.botonCorta: {
                        drawer.closeDrawer(Gravity.START);
                        if (modo == modoCorta) {
                            ajustadorPosicion(ajustadorPorcentajes);
                            break;
                        } else {
                            setBotonCorta();
                            ajustadorPosicion(ajustadorPorcentajes);
                        }
                        break;

                    }

                    case R.id.botonLarga: {
                        drawer.closeDrawer(Gravity.START);
                        if (modo == modoLarga) {
                            ajustadorPosicion(ajustadorPorcentajes);
                            break;
                        } else {
                            setBotonLarga();
                            ajustadorPosicion(ajustadorPorcentajes);
                        }
                        break;

                    }


                    case R.id.botonPorcentajes: {

                        if (botonReducirAplanado) {

                            Intent i = getIntent();
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("idOperacion", idOperacion);
                            startActivity(i);
                            break;
                        }
                        drawer.closeDrawer(Gravity.START);
                        vibrator.vibrate(50);
                        setBotonPorcentajesAplanado();

                        break;
                    }

                    case R.id.botonClear: {
                        vibrator.vibrate(50);
                        limpiarCalculador();
                        break;
                    }

                    case R.id.botonDuplicar: {
                        vibrator.vibrate(50);
                        drawer.closeDrawer(Gravity.START);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("idOperacion", idOperacion);
                        i.putExtra("esDuplicado", true);
                        startActivity(i);
                        break;
                    }


                    case R.id.header: {


                        Intent i = new Intent(getApplicationContext(), MainScreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("salida", true);
                        startActivity(i);
                        vibrator.vibrate(500);
                        finish();

                        break;
                    }

                    case R.id.botonPorcentajeCalculador: {
                        vibrator.vibrate(50);

                        if (!botonporcentajeCalculadorAplanado) {

                            botonporcentajeCalculadorAplanado = true;
                            botonPorcentajeCalculador.setBackgroundResource(R.drawable.fondo_boton_forex);
                            textoPorcentaje.setVisibility(View.GONE);
                            textoPorcentajeMod.setVisibility(View.VISIBLE);
                            textoPrecio.setText("Precio");
                            textoPrecio.setVisibility(View.VISIBLE);
                            textoPrecioMod.setVisibility(View.GONE);
                            botonPorcentajeCalculadorMas.setVisibility(View.VISIBLE);
                            botonPorcentajeCalculadorMenos.setVisibility(View.VISIBLE);
                            if (modo == modoCorta || modo == modoLarga)
                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

                            textoPorcentajeMod.setText("0");


                        } else {
                            botonporcentajeCalculadorAplanado = false;
                            botonPorcentajeCalculador.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                            textoPorcentaje.setVisibility(View.VISIBLE);
                            textoPorcentajeMod.setVisibility(View.GONE);
                            textoPrecio.setVisibility(View.GONE);
                            textoPrecioMod.setVisibility(View.VISIBLE);
                            textoPrecioMod.setText("");
                            botonPorcentajeCalculadorMas.setVisibility(View.GONE);
                            botonPorcentajeCalculadorMenos.setVisibility(View.GONE);

                            if (modo == modoCorta || modo == modoLarga)
                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

                        }
                        textoActualLiq.setText("Pendiente");

                        break;
                    }

                    case R.id.botonPorcentajeCalculadorMas: {
                        vibrator.vibrate(50);

                        if (!botonPorcentajeCalculadorMasAplanado) {
                            botonPorcentajeCalculadorMasAplanado = true;
                            botonPorcentajeCalculadorMas.setBackgroundResource(R.drawable.fondo_boton_forex);
                            botonPorcentajeCalculadorMenos.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoActualLiq.setText("Pendiente");
                            if (modo == modoCorta || modo == modoLarga)

                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
                            positivo = true;
                        }

                        break;
                    }
                    case R.id.botonPorcentajeCalculadorMenos: {

                        vibrator.vibrate(50);

                        if (botonPorcentajeCalculadorMasAplanado) {
                            botonPorcentajeCalculadorMasAplanado = false;
                            botonPorcentajeCalculadorMas.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                            botonPorcentajeCalculadorMenos.setBackgroundResource(R.drawable.fondo_boton_forex);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoActualLiq.setText("Pendiente");
                            if (modo == modoCorta || modo == modoLarga)

                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
                            positivo = false;
                        }

                        break;
                    }

                    case R.id.botonModificar: {

                        vibrator.vibrate(50);
                        drawer.closeDrawer(Gravity.START);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("idOperacion", idOperacion);
                        startActivity(i);
                        break;
                    }

                    case R.id.botonComisiones: {
                        vibrator.vibrate(50);
                        drawer.closeDrawer(Gravity.START);
                        setBotonComision();
                        break;
                    }

                    case R.id.botonReducir: {

                        if (!botonReducirAplanado) {
                            drawer.closeDrawer(Gravity.START);
                            vibrator.vibrate(50);
                            botonReducir.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                            botonReducirAplanado = true;
                            setBotonReducir();

                        } else {

                            Intent i = getIntent();
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("idOperacion", idOperacion);
                            startActivity(i);
                        }

                        break;
                    }

                    case R.id.textoPrecio: {
                        exportarPrecio(textoPrecio);
                        break;
                    }

                    case R.id.textoBase: {
                        exportarPrecio(textoBase);
                        break;
                    }

                    case R.id.textoInvertido: {
                        exportarPrecio(textoInvertido);
                        break;
                    }

                    case R.id.textoGanancia: {
                        exportarPrecio(textoGanancia);
                        break;
                    }

                    case R.id.textoInvertidoActual: {
                        exportarPrecio(textoInvertidoActual);
                        break;
                    }

                    case R.id.textoInversionLiq2: {
                        exportarPrecio(textoInversionLiq);
                        break;
                    }

                    case R.id.textoGanadoLiq2: {
                        exportarPrecio(textoGanadoLiq);
                        break;
                    }

                    case R.id.textoActualLiq2: {
                        exportarPrecio(textoActualLiq);
                        break;
                    }

                    case R.id.textoUsandoRV2: {
                        exportarPrecio(textoUsado);
                        break;
                    }

                }

            }
            return true;
        }
    };

    double comisionEntradaRespaldo, comisionSalidaRespaldo;
    boolean hayComisionCero;

    void setBotonComision() {

        if (botonComisionAplanado) {
            botonComisiones.setBackgroundResource(R.drawable.fondo_botones_superior);
            if (!recyclerBotonesPorcentajes.isShown()) {

                textoSinComision.setVisibility(View.VISIBLE);
                if (botonReducirAplanado)
                    textoSinComision.setVisibility(View.GONE);


            }


            adapterRecyclerPorcentajes.cambioComisiones(botonComisionAplanado);

            if (comisionEntradaRespaldo == 0 && comisionSalidaRespaldo == 0) {

                if (comisionEntrada == 0 || comisionSalida == 0)
                    hayComisionCero = true;

                comisionEntradaRespaldo = comisionEntrada;
                comisionSalidaRespaldo = comisionSalida;
            }
            comisionEntrada = 0;
            comisionSalida = 0;
            botonComisionAplanado = false;


        } else {
            botonComisiones.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
            if (!recyclerBotonesPorcentajes.isShown()) {

                textoSinComision.setVisibility(View.GONE);

            }
            adapterRecyclerPorcentajes.cambioComisiones(botonComisionAplanado);
            if (hayComisionCero) {

                comisionEntrada = comisionEntradaRespaldo;
                comisionSalida = comisionSalidaRespaldo;

            } else {

                if (comisionEntradaRespaldo > 0 && comisionSalidaRespaldo > 0) {
                    comisionEntrada = comisionEntradaRespaldo;
                    comisionSalida = comisionSalidaRespaldo;
                }

            }
            botonComisionAplanado = true;
        }
        ajustadorPosicion(ajustadorPorcentajes);
        limpiarCalculador();
    }

    Realm realm;

    void guardarOrigen() {

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        final DB db = realm.where(DB.class).equalTo("id", idOperacion).findFirst();

        realm.executeTransaction(realm -> {


            db.setBotonPorcentajesAplanado(botonPorcentajesAplanado);
            db.setBotonComisionAplanado(botonComisionAplanado);
            db.setModo(modo);

            if (!textoReferencia.getText().toString().isEmpty())
                db.setReferencia(textoReferencia.getText().toString().toUpperCase());
            else {
                if (db.getReferencia() != null)
                    db.setReferencia(null);
            }


            if (!textoPrecioMod.getText().toString().isEmpty()) {

                db.setPrecioOut(textoPrecioMod.getText().toString());

            } else {

                db.setPrecioOut(null);

            }

            db.setInversionInicioFinal(textoInvertido.getText().toString());
            db.setGanadoInicio(textoGanancia.getText().toString());
            db.setActualInicio(textoInvertidoActual.getText().toString());
            db.setInversionLiqInicio(textoInversionLiq.getText().toString());
            db.setGanadoLiqInicio(textoGanadoLiq.getText().toString());
            db.setActualLiqInicio(textoActualLiq.getText().toString());
            db.setGanadoFinal(gananciaFinal);
            db.setUsandoInicio(String.format(precisionDestino, (invertido / precio)) + " " + monedaDestinoNombre);
            db.setExisteReduccion(existeReduccion);


            if (botonReducirAplanado) {
                db.reducciones.clear();

                if (adapterRecyclerReducirPosicion.lista.size() > 0) {
                    db.reducciones.addAll(adapterRecyclerReducirPosicion.lista);
                    db.setGananciaRedFinal(gananciaRedFinal);

                }
                db.setInvertidoRedFinal(invertido);

            }


            db.setPrecioIn(String.valueOf(precio));


        });
        realm.close();


    }

    private void exportarPrecio(TextView text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Precio", text.getText().toString()
                .replace(monedaOrigenNombre, "")
                .replace(monedaDestinoNombre, "")
                .replace(",", "")
                .replace(" ", ""));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Precio grabado: " + text.getText().toString(), Toast.LENGTH_SHORT).show();
        vibrator.vibrate(500);
    }

    private void limpiarCalculador() {

        if (cargaInicialTerminada && !enTextWatcher) {
            textoPrecioMod.setText("");
            textoPrecioMod.clearFocus();
            textoPorcentajeMod.setText("");
            textoPorcentajeMod.clearFocus();
        }
        textoReferencia.clearFocus();
        textoPorcentaje.setText("+0.00%");
        textoPrecio.setText("Precio");
        textoInversionLiq.setText("Pendiente");
        textoGanadoLiq.setText("Pendiente");
        textoActualLiq.setText("Pendiente");

        if (modo == modoCorta || modo == modoLarga) {


            if (!existeReduccion) {
                textoGanancia.setText(String.format("0.00 " + monedaOrigenNombre));
                textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                textoInvertidoActual.setText(String.format(precisionOrigen, invertido + gananciaRedFinal) + " " + monedaOrigenNombre);
            } else {

                textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                invertidoActual = invertido + gananciaRedFinal;
                textoInvertidoActual.setText(String.format(precisionOrigen, invertidoActual) + " " + monedaOrigenNombre);


                if (gananciaRedFinal >= 0) {

                    textoGananciaLetra.setText("Ganado");
                    textoGanadoLiqLetra.setText("Ganado liq");
                    textoGanancia.setText(String.format(precisionOrigen, gananciaRedFinal) + " " + monedaOrigenNombre);
                    positivo = true;
                } else {
                    textoGananciaLetra.setText("Perdido");
                    textoGanadoLiqLetra.setText("Perdido liq");
                    textoGanancia.setText(String.format(precisionOrigen, gananciaRedFinal).substring(1) + " " + monedaOrigenNombre);
                    positivo = false;
                }


                chequeoLiquidez();


                if (inversionLiq != 0 && inversionLiq != Double.POSITIVE_INFINITY) {
                    textoInversionLiq.setText(String.format(precisionLiquidez, inversionLiq) + " " + liquidezNombre);
                    ganadoLiq = actualLiq - inversionLiq;
                    textoGanadoLiq.setText(positivo ? String.format(precisionLiquidez, ganadoLiq) + " " + liquidezNombre :
                            String.format(precisionLiquidez, ganadoLiq).substring(1) + " " + liquidezNombre);
                    textoActualLiq.setText(String.format(precisionLiquidez, actualLiq) + " " + liquidezNombre);
                }

            }


        } else {

            textoGanancia.setText("0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

        }
    }


    private void setBotonCazar() {
        botonCazar.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);

        modo = modoCazar;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("CZ " + monedaDestinoNombre + " con " +
                monedaOrigenNombre + " " + textoReferencia.getText().toString().toUpperCase());


        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);


        } else {

            textoGanancia.setText("0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }


        limpiarCalculador();
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonCorta() {
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonCorta.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);
        modo = modoCorta;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("CR " + monedaDestinoNombre + " con " +
                monedaOrigenNombre + " " + textoReferencia.getText().toString().toUpperCase());
        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

        } else {

            textoGanancia.setText("0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }

        limpiarCalculador();
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }


    private void setBotonLarga() {
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonLarga.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        modo = modoLarga;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("LR " + monedaDestinoNombre + " con " +
                monedaOrigenNombre + " " + textoReferencia.getText().toString().toUpperCase());
        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

        } else {

            textoGanancia.setText("0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }


        limpiarCalculador();
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonPorcentajesAplanado() {


        if (botonPorcentajesAplanado) {
            calculador.setVisibility(View.VISIBLE);
            recyclerPorcentajes.setVisibility(View.GONE);
            recyclerBotonesPorcentajes.setVisibility(View.GONE);
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior);

            if (botonComisionAplanado) {
                textoSinComision.setVisibility(View.GONE);
            } else {
                textoSinComision.setVisibility(View.VISIBLE);
            }

            if (existeReduccion) {
                textoReduccion.setVisibility(View.VISIBLE);

            } else {
                textoReduccion.setVisibility(View.GONE);
            }

            if (liquidezNombre != null) {

                if (!liquidezNombre.isEmpty())
                    textoIndicadorLiquidez.setVisibility(View.VISIBLE);
            }
            botonPorcentajesAplanado = false;

        } else {
            calculador.setVisibility(View.GONE);
            textoSinComision.setVisibility(View.GONE);
            textoReduccion.setVisibility(View.GONE);
            textoIndicadorLiquidez.setVisibility(View.GONE);
            recyclerPorcentajes.setVisibility(View.VISIBLE);
            recyclerBotonesPorcentajes.setVisibility(View.VISIBLE);
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
            botonPorcentajesAplanado = true;
        }

        vibrator.vibrate(50);
    }

    Group seccionReduccionSuperior, seccionReduccionSuperiorOtros, seccionReduccionInferior;
    EditText inversionReducir, precioReducir, cantidadReducir, precioRedOtros;
    TextView inversionDisponibleRed, encabezadoInversionRed,
            encabezadoPrecioRed, encabezadoRed, encabezadoCantidadRed;
    Button botonReducirR, botonCambioInversiones, botonOtrosRed, botonGastoRed, botonGananciaRed;
    boolean cambioInversionRedAplanado, botonOtrosAplanado,
            botonGananciaRedAplanado, cambioInversionReducirAPorcenajeActivado, cambioAumentarActivado;

    void setBotonReducir() {

        seccionReduccionSuperior = findViewById(R.id.seccionReduccionSuperior);
        seccionReduccionSuperiorOtros = findViewById(R.id.seccionReduccionSuperiorOtros);
        seccionReduccionInferior = findViewById(R.id.seccionReduccionInferior);
        seccionReduccionSuperior.setVisibility(View.VISIBLE);
        seccionReduccionSuperiorOtros.setVisibility(View.GONE);
        encabezadoCantidadRed = findViewById(R.id.encabezadoCantidadRed);
        encabezadoCantidadRed.setVisibility(View.GONE);
        seccionReduccionInferior.setVisibility(View.VISIBLE);
        botonReducirAplanado = true;
        calculador.setVisibility(View.GONE);
        textoSinComision.setVisibility(View.GONE);
        textoReduccion.setVisibility(View.GONE);
        textoIndicadorLiquidez.setVisibility(View.GONE);
        recyclerPorcentajes.setVisibility(View.GONE);
        recyclerBotonesPorcentajes.setVisibility(View.GONE);
        botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior);


        inversionReducir = findViewById(R.id.inversionRed);
        precioReducir = findViewById(R.id.precioRed);
        botonReducirR = findViewById(R.id.botonRed);
        botonOtrosRed = findViewById(R.id.botonOtrosRed);
        cantidadReducir = findViewById(R.id.cantidadReducirRed);
        precioRedOtros = findViewById(R.id.precioRedOtros);
        botonCambioInversiones = findViewById(R.id.botonCambioInversionRed);
        encabezadoCantidadRed = findViewById(R.id.encabezadoCantidadRed);
        encabezadoCantidadRed.setOnClickListener(view -> {
            vibrator.vibrate(100);
            cantidadReducir.getText().clear();
        });
        encabezadoRed = findViewById(R.id.encabezadoRed);
        encabezadoRed.setOnClickListener(view -> {
            if (!cambioInversionReducirAPorcenajeActivado) {
                vibrator.vibrate(100);
                cambioInversionReducirAPorcenajeActivado = true;
                if (!cambioAumentarActivado)
                    encabezadoRed.setText("Inversion a reducir %");
                else
                    encabezadoRed.setText("Inversion a agregar %");

            } else {
                vibrator.vibrate(100);
                cambioInversionReducirAPorcenajeActivado = false;
                if (!cambioAumentarActivado)
                    encabezadoRed.setText("Inversion a reducir");
                else
                    encabezadoRed.setText("Inversion a agregar");
            }


        });
        encabezadoPrecioRed = findViewById(R.id.encabezadoPrecioRed);
        encabezadoInversionRed = findViewById(R.id.encabezadoInversionRed);
        encabezadoInversionRed.setOnClickListener(view -> {
            vibrator.vibrate(50);

            if (!cambioAumentarActivado) {
                cambioAumentarActivado = true;
                encabezadoPrecioRed.setText("Precio de entrada");
                encabezadoRed.setText("Inversion a agregar");
                precioReducir.setHint("Precio de entrada");
                inversionReducir.setHint("Inversion a agregar");
                inversionReducir.getText().clear();
                precioReducir.getText().clear();
                if (cambioInversionReducirAPorcenajeActivado)
                    encabezadoRed.setText("Inversion a agregar %");
                else
                    encabezadoRed.setText("Inversion a agregar");
                botonReducirR.setOnClickListener(onClickListenerRedAgregar);

            } else {
                cambioAumentarActivado = false;
                encabezadoPrecioRed.setText("Precio de cierre");
                encabezadoRed.setText("Inversion a reducir");
                precioReducir.setHint("Precio de cierre");
                inversionReducir.setHint("Inversion a reducir");
                inversionReducir.getText().clear();
                precioReducir.getText().clear();
                if (cambioInversionReducirAPorcenajeActivado)
                    encabezadoRed.setText("Inversion a reducir %");
                else
                    encabezadoRed.setText("Inversion a reducir");
                botonReducirR.setOnClickListener(onClickListenerReducir);
            }


        });

        botonGananciaRed = findViewById(R.id.botonGananciaRed);
        botonGastoRed = findViewById(R.id.botonGastoRed);
        inversionDisponibleRed = findViewById(R.id.inversionDisponibleRed);
        inversionDisponibleRed.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
        setRecyclerViewRecyclerReducir();
    }

    LinearLayoutManager layoutManagerReducir;
    RecyclerView recyclerPosicionReducida;
    AdapterRecyclerReducirPosicion adapterRecyclerReducirPosicion;
    View.OnClickListener onClickListenerReducir = view -> {

        {

            if (modo == modoCazar) {
                crearSnackBar("No se puede reducir en modo cazar");
                return;
            }

            vibrator.vibrate(500);

            if (inversionReducir.getText().toString().isEmpty() ||
                    precioReducir.getText().toString().isEmpty()) {
                crearSnackBar("Faltan datos");
                return;
            } else
                crearSnackBar("Inversion reduccida con exito!");


            double inversionRed, precioRed, porcentajeFinalRed;
            double porcentaje;

            if (!cambioInversionReducirAPorcenajeActivado) {
                inversionRed = Double.parseDouble(inversionReducir.getText().toString());


            } else {
                porcentaje = Double.parseDouble(inversionReducir.getText().toString()) / 100;

                if (!cambioInversionRedAplanado) {

                    inversionRed = invertido * porcentaje;


                } else {
                    inversionRed = invertidoDestino * porcentaje;

                }
            }


            precioRed = Double.parseDouble(precioReducir.getText().toString());

            if (!cambioInversionRedAplanado) {

                if (inversionRed > invertido) {
                    crearSnackBar("No es posible reducir mas de lo disponible");
                    return;
                }


            } else {

                if (inversionRed > invertidoDestino) {
                    crearSnackBar("No es posible reducir mas de lo disponible");
                    return;
                }

                inversionRed *= precio;
            }
            porcentajeFinalRed = inversionRed / invertido;


            if (modo == modoCorta) {

                if (enForex) {

                    double b = precioRed + comisionSalida;
                    double a = precio - comisionEntrada;
                    porcentajeFinal = b / a;
                    porcentajeFinal -= 1;
                    porcentajeFinal *= -1;


                } else {

                    double b;

                    b = precioRed / precio;
                    b += -2;
                    b *= -1;
                    b -= comisionEntrada;
                    b /= (1 + comisionSalida);

                    porcentajeFinal = b - 1;

                }

                invertidoActual = invertido * (1 + porcentajeFinal);
                gananciaFinal = invertidoActual - invertido;
                porcentajeFinal *= 100;
                positivo = gananciaFinal >= 0;
            } else if (modo == modoLarga) {

                if (enForex) {

                    double b = precioRed - comisionSalida;
                    double a = precio + comisionEntrada;
                    porcentajeFinal = b / a;
                    porcentajeFinal -= 1;


                } else {

                    double b;
                    b = precioRed / precio;
                    b -= comisionEntrada;
                    b /= (1 + comisionSalida);
                    porcentajeFinal = b - 1;


                }

                invertidoActual = invertido * (1 + (porcentajeFinal));
                gananciaFinal = invertidoActual - invertido;
                porcentajeFinal *= 100;
                positivo = gananciaFinal >= 0;
            }


            DBReductor reductorDB = new DBReductor();


            reductorDB.setInversionR(inversionRed);
            reductorDB.setPrecioRedR(precioRed);
            reductorDB.setPrecioBaseR(precio);
            reductorDB.setGanadoRedR(gananciaFinal * porcentajeFinalRed);
            reductorDB.setInversionRedNumero(inversionRed);
            reductorDB.setGanadoRedNumero(gananciaFinal * porcentajeFinalRed);
            reductorDB.setUsandoR(invertidoDestino * porcentajeFinalRed);
            reductorDB.setTipo(0);


            invertido -= inversionRed;
            invertidoDestino -= invertidoDestino * porcentajeFinalRed;


            if (!cambioInversionRedAplanado) {
                inversionDisponibleRed.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                reductorDB.setInversionDisponibleRedR(invertido);
            } else {
                inversionDisponibleRed.setText(String.format(precisionDestino, invertido / precio) + " " + monedaDestinoNombre);
                reductorDB.setInversionDisponibleRedR(invertido);
            }


            inversionReducir.getText().clear();
            precioReducir.getText().clear();


            adapterRecyclerReducirPosicion.agregarReduccion(reductorDB);

            if (adapterRecyclerReducirPosicion.lista.size() > 0) {

                double gananaciaFinalAcumulada = 0;

                for (DBReductor db : adapterRecyclerReducirPosicion.lista) {

                    gananaciaFinalAcumulada += db.ganadoRedNumero;
                }

                gananciaRedFinal = gananaciaFinalAcumulada;
            }
            existeReduccion = adapterRecyclerReducirPosicion.lista.size() > 0;

            if (textoPrecioMod.getText().toString().isEmpty())
                limpiarCalculador();
            else {
                String precioT = textoPrecioMod.getText().toString();
                limpiarCalculador();
                textoPrecioMod.setText(precioT);
            }

        }


    };
    View.OnClickListener onClickListenerRedOtros = view -> {

        if (cantidadReducir.getText().toString().isEmpty() ||
                precioRedOtros.getText().toString().isEmpty()) {
            crearSnackBar("Faltan datos por llenar");
            return;
        } else
            crearSnackBar("Inversion reduccida con exito!");

        double cantidadExtraido = Double.parseDouble(cantidadReducir.getText().toString());
        double precioExtraido = Double.parseDouble(precioRedOtros.getText().toString());
        double ganadoOrigen, ganadoDestino;


        if (!botonGananciaRedAplanado)
            cantidadExtraido *= -1;

        DBReductor dbReductor = new DBReductor();
        dbReductor.setTipo(1);

        if (!cambioInversionRedAplanado) {
            ganadoOrigen = cantidadExtraido;
            ganadoDestino = ganadoOrigen / precioExtraido;
        } else {
            ganadoOrigen = cantidadExtraido * precioExtraido;
            ganadoDestino = cantidadExtraido;
        }


        dbReductor.setGanadoRedNumero(ganadoOrigen);
        dbReductor.setUsandoR(ganadoDestino);
        dbReductor.setPrecioNumero(precioExtraido);

        cantidadReducir.getText().clear();
        precioRedOtros.getText().clear();
        adapterRecyclerReducirPosicion.agregarReduccion(dbReductor);
        if (adapterRecyclerReducirPosicion.lista.size() > 0) {

            double gananaciaFinalAcumulada = 0;

            for (DBReductor db : adapterRecyclerReducirPosicion.lista) {

                gananaciaFinalAcumulada += db.ganadoRedNumero;
            }

            gananciaRedFinal = gananaciaFinalAcumulada;
        }

        existeReduccion = adapterRecyclerReducirPosicion.lista.size() > 0;

        if (textoPrecioMod.getText().toString().isEmpty())
            limpiarCalculador();
        else {
            String precioT = textoPrecioMod.getText().toString();
            limpiarCalculador();
            textoPrecioMod.setText(precioT);
        }


    };
    View.OnClickListener onClickListenerRedAgregar = view -> {

        if (modo == modoCazar) {
            crearSnackBar("No se puede aumentar en modo cazar");
            return;
        }

        vibrator.vibrate(500);

        if (inversionReducir.getText().toString().isEmpty() ||
                precioReducir.getText().toString().isEmpty()) {
            crearSnackBar("Faltan datos");
            return;
        } else
            crearSnackBar("Inversion aumentada con exito!");

        double inversionRed, precioRed, porcentajeFinalRed;
        double porcentaje;

        precioRed = Double.parseDouble(precioReducir.getText().toString());

        if (!cambioInversionReducirAPorcenajeActivado) {

            if (!cambioInversionRedAplanado)
                inversionRed = Double.parseDouble(inversionReducir.getText().toString());
            else {
                inversionRed = Double.parseDouble(inversionReducir.getText().toString());
                inversionRed *= precioRed;
            }


        } else {
            porcentaje = Double.parseDouble(inversionReducir.getText().toString()) / 100;

            if (!cambioInversionRedAplanado) {

                inversionRed = invertido * porcentaje;


            } else {
                inversionRed = invertidoDestino * porcentaje;

            }
        }


        double precioAntiguo = precio;
        double inversionAntigua = invertido;


        invertido += inversionRed;

        invertidoDestino += inversionRed / precioRed;

        precio = invertido / invertidoDestino;


        if (!cambioInversionRedAplanado) {
            inversionDisponibleRed.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        } else {
            inversionDisponibleRed.setText(String.format(precisionDestino, invertido / precio) + " " + monedaDestinoNombre);
        }


        inversionReducir.getText().clear();
        precioReducir.getText().clear();


        DBReductor reductorDB = new DBReductor();

        reductorDB.setInversionR(inversionRed);
        reductorDB.setPrecioRedR(precioRed);
        reductorDB.setPrecioBaseR(precioAntiguo);
        reductorDB.setGanadoRedR(inversionRed / precioRed);
        reductorDB.setUsandoR(invertidoDestino);
        reductorDB.setPrecioNumero(precio);
        reductorDB.setTipo(2);

        adapterRecyclerReducirPosicion.agregarReduccion(reductorDB);
        existeReduccion = adapterRecyclerReducirPosicion.lista.size() > 0;

        if (textoPrecioMod.getText().toString().isEmpty())
            limpiarCalculador();
        else {
            String precioT = textoPrecioMod.getText().toString();
            limpiarCalculador();
            textoPrecioMod.setText(precioT);
        }


    };

    private void setRecyclerViewRecyclerReducir() {
        recyclerPosicionReducida = findViewById(R.id.recyclerPosicionReducida);

        if (liquidezNombre != null) {
            Bundle bundle = new Bundle();
            bundle.putDouble("liquidezDestino", liquidezDestino);
            bundle.putDouble("liquidezOrigen", liquidezOrigen);
            bundle.putInt("modoLiquidez", modoLiquidez);
            bundle.putString("liquidezNombre", liquidezNombre);
            bundle.putDouble("precio", precio);
            bundle.putString("precisionLiquidez", precisionLiquidez);
            bundle.putString("precisionOrigen", precisionOrigen);
            bundle.putString("precisionPrecio", precisionPrecio);
            bundle.putString("precisionDestino", precisionDestino);
            bundle.putString("monedaDestinoNombre", monedaDestinoNombre);
            bundle.putString("monedaOrigenNombre", monedaOrigenNombre);
            adapterRecyclerReducirPosicion = new AdapterRecyclerReducirPosicion(this, bundle);


        } else {
            adapterRecyclerReducirPosicion = new AdapterRecyclerReducirPosicion(this, null);
        }


        recyclerPosicionReducida.setAdapter(adapterRecyclerReducirPosicion);

        layoutManagerReducir = new LinearLayoutManager(this);
        recyclerPosicionReducida.setLayoutManager(layoutManagerReducir);
        adapterRecyclerReducirPosicion.lista.addAll(db.reducciones);
        adapterRecyclerReducirPosicion.notifyDataSetChanged();
        botonReducirR.setOnClickListener(onClickListenerReducir);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP, ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int posicion = viewHolder.getLayoutPosition();
                double invertidoRespaldo = 0, invertidoDestinoRespaldo = 0,
                        gananciaRedFinalRespaldo = 0;
                int tipo = adapterRecyclerReducirPosicion.lista.get(posicion).getTipo();

                if (tipo == 0) {
                    invertidoRespaldo = invertido;
                    invertidoDestinoRespaldo = invertidoDestino;
                    gananciaRedFinalRespaldo = gananciaRedFinal;

                    invertido += adapterRecyclerReducirPosicion.lista.get(posicion).getInversionRedNumero();
                    invertidoDestino = invertido / precio;
                    gananciaRedFinal -= adapterRecyclerReducirPosicion.lista.get(posicion).getGanadoRedNumero();
                    if (!cambioInversionRedAplanado)
                        inversionDisponibleRed.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                    else
                        inversionDisponibleRed.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

                } else if (tipo == 2) {
                    invertidoRespaldo = invertido;
                    invertidoDestinoRespaldo = invertidoDestino;


                    invertido -= adapterRecyclerReducirPosicion.lista.get(posicion).getInversionR();
                    invertidoDestino -= adapterRecyclerReducirPosicion.lista.get(posicion).getGanadoRedR();
                    precio = invertido / invertidoDestino;
                    if (!cambioInversionRedAplanado)
                        inversionDisponibleRed.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                    else
                        inversionDisponibleRed.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

                }


                adapterRecyclerReducirPosicion.removerReduccion(posicion);

                existeReduccion = adapterRecyclerReducirPosicion.lista.size() > 0;


                Snackbar snackbar =
                        Snackbar.make(findViewById(R.id.encabezado), "Reduccion borrada!", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
                double finalInvertidoRespaldo = invertidoRespaldo;
                double finalInvertidoDestinoRespaldo = invertidoDestinoRespaldo;
                double finalGananciaRedFinalRespaldo = gananciaRedFinalRespaldo;
                snackbar.setAction("Regresar", view -> {

                    vibrator.vibrate(250);

                    if (tipo == 0) {

                        invertido = finalInvertidoRespaldo;
                        invertidoDestino = finalInvertidoDestinoRespaldo;
                        gananciaRedFinal = finalGananciaRedFinalRespaldo;
                        if (!cambioInversionRedAplanado)
                            inversionDisponibleRed.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                        else
                            inversionDisponibleRed.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
                    } else if (tipo == 2) {
                        invertido = finalInvertidoRespaldo;
                        invertidoDestino = finalInvertidoDestinoRespaldo;
                        precio = invertido / invertidoDestino;
                        if (!cambioInversionRedAplanado)
                            inversionDisponibleRed.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                        else
                            inversionDisponibleRed.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
                    }
                    adapterRecyclerReducirPosicion.recuperarReduccion();

                    existeReduccion = adapterRecyclerReducirPosicion.lista.size() > 0;


                }).show();
                vibrator.vibrate(500);


            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerPosicionReducida);

        botonCambioInversiones.setOnClickListener(view -> {
            vibrator.vibrate(200);

            if (!cambioInversionRedAplanado) {

                inversionDisponibleRed.setText(String.format(precisionDestino, invertidoDestino)
                        + " " + monedaDestinoNombre);
                botonCambioInversiones.setBackgroundResource(R.drawable.fondo_boton_forex);
                cambioInversionRedAplanado = true;

                if (botonOtrosAplanado) {

                    encabezadoCantidadRed.setText("Cantidad en " + monedaDestinoNombre);
                }


            } else {

                inversionDisponibleRed.setText(String.format(precisionOrigen, invertido)
                        + " " + monedaOrigenNombre);
                botonCambioInversiones.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                cambioInversionRedAplanado = false;

                if (botonOtrosAplanado) {

                    encabezadoCantidadRed.setText("Cantidad en " + monedaOrigenNombre);
                }

            }

        });

        botonOtrosRed.setOnClickListener(view -> {
            vibrator.vibrate(200);
            if (!botonOtrosAplanado) {
                seccionReduccionSuperior.setVisibility(View.GONE);
                seccionReduccionSuperiorOtros.setVisibility(View.VISIBLE);
                botonOtrosRed.setBackgroundResource(R.drawable.fondo_boton_forex);
                botonReducirR.setOnClickListener(onClickListenerRedOtros);
                encabezadoCantidadRed.setVisibility(View.VISIBLE);
                if (!cambioInversionRedAplanado) {
                    encabezadoCantidadRed.setText("Cantidad en " + monedaOrigenNombre);
                } else {

                    encabezadoCantidadRed.setText("Cantidad en " + monedaDestinoNombre);
                }
                botonOtrosAplanado = true;
            } else {

                seccionReduccionSuperior.setVisibility(View.VISIBLE);
                seccionReduccionSuperiorOtros.setVisibility(View.GONE);
                botonOtrosRed.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                botonReducirR.setOnClickListener(onClickListenerReducir);
                encabezadoCantidadRed.setVisibility(View.GONE);
                botonOtrosAplanado = false;
            }
        });

        botonGastoRed.setOnClickListener(view -> {
            vibrator.vibrate(200);
            botonGastoRed.setBackgroundResource(R.drawable.fondo_boton_forex);
            botonGananciaRed.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
            botonGananciaRedAplanado = false;
        });

        botonGananciaRed.setOnClickListener(view -> {
            vibrator.vibrate(200);
            botonGastoRed.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
            botonGananciaRed.setBackgroundResource(R.drawable.fondo_boton_forex);
            botonGananciaRedAplanado = true;
        });


    }

    void crearSnackBar(String texto) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.encabezado),
                texto, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
        snackbar.show();
    }


    LinearLayoutManager layoutManagerPorcentajes;

    private void setRecyclerViewRecyclerPorcentajes() {
        recyclerPorcentajes = findViewById(R.id.recyclerCambioDePorcentajes);
        adapterRecyclerPorcentajes = new AdapterRecyclerPorcentajes(this, db);
        recyclerPorcentajes.setAdapter(adapterRecyclerPorcentajes);
        layoutManagerPorcentajes = new LinearLayoutManager(this);
        recyclerPorcentajes.setLayoutManager(layoutManagerPorcentajes);
        layoutManagerPorcentajes.scrollToPosition(97);
    }

    LinearLayoutManager layoutManagerBotonesPorcentajes;

    private void setRecyclerViewRecyclerBotonesPorcentajes() {
        recyclerBotonesPorcentajes = findViewById(R.id.recyclerBotonesPorcentaje);
        adapterRecyclerBotonesPorcentajes = new AdapterRecyclerBotonesPorcentajes(this);
        layoutManagerBotonesPorcentajes = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerBotonesPorcentajes.setAdapter(adapterRecyclerBotonesPorcentajes);
        recyclerBotonesPorcentajes.setLayoutManager(layoutManagerBotonesPorcentajes);
        layoutManagerBotonesPorcentajes.scrollToPosition(2);
    }


    @Override
    public void cambioPorcentaje(String porcentaje, Integer ajustadorPorcentajes) {
        adapterRecyclerPorcentajes.cambioPorcentaje(porcentaje, ajustadorPorcentajes);

        this.ajustadorPorcentajes = ajustadorPorcentajes;

        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);

    }

    private void ajustadorPosicion(Integer ajustadorPorcentajes) {
        switch (ajustadorPorcentajes) {
            case 1: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(99, 99);
                break;
            }
            case 2: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(199, 200);
                break;
            }
            case 4: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(399, 399);
                break;
            }
            case 10: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(999, 999);
                break;
            }

            case 100: {
                layoutManagerPorcentajes.scrollToPositionWithOffset(10005, 10005);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.START)) {

            drawer.closeDrawer(Gravity.START);
        } else {

            Intent i = getBaseContext().getPackageManager().
                    getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onPause() {
        guardarOrigen();
        super.onPause();
    }
}
