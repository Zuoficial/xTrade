package com.smoowy.xTrade;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class Calculos extends AppCompatActivity implements Comunicador {

    RecyclerView recyclerBotonesPorcentajes, recyclerPorcentajes;
    AdapterRecyclerBotonesPorcentajes adapterRecyclerBotonesPorcentajes;
    AdapterRecyclerPorcentajes adapterRecyclerPorcentajes;
    Button botonCazar, botonCorta, botonLarga, botonClear, botonPorcentajes,
            botonCerrar, botonPorcentajeCalculador,
            botonPorcentajeCalculadorMenos, botonPorcentajeCalculadorMas,
            botonGuardar, botonModificar;
    TextView encabezado, textoGanancia, textoInvertido, textoInvertidoActual, textoUsado, textoGananciaLetra,
            textoPorcentaje, textoPrecio, textoBase, textoLiquidez;
    String monedaOrigenNombre, monedaDestinoNombre,referencia;
    EditText textoPrecioMod, textoPorcentajeMod,textoReferencia;
    double invertido, precio, invertidoDestino, precioFinal,
            precioIngresado, porcentajeFinal, gananciaFinal, invertidoActual, porcentajeIngresado,
            liquidezOrigen, liquidezDestino, comisionEntrada, comisionSalida, invertidoFinal, liquidez,
            resultadoComisionEntrada, resultadoComisionSalida;
    int ajustadorPorcentajes, modo, idOperacion;
    final int modoCazar = 0, modoCorta = 1, modoLarga = 2;
    boolean botonPorcentajesAplanado,
            botonporcentajeCalculadorAplanado, botonPorcentajeCalculadorMasAplanado, enForex;
    Vibrator vibrator;
    String precisionOrigen, precisionDestino, precisionLiquidez,precisionPrecio,
            liquidezNombre;
    ConstraintLayout calculador;
    DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_calculos);
        drawer = findViewById(R.id.drawer_layout);
        encabezado = findViewById(R.id.encabezado);
        encabezado.setOnTouchListener(onTouchListener);
        calculador = findViewById(R.id.calculador);
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
        botonCerrar = findViewById(R.id.botonCerrar);
        botonPorcentajeCalculador = findViewById(R.id.botonPorcentajeCalculador);
        botonPorcentajeCalculadorMas = findViewById(R.id.botonPorcentajeCalculadorMas);
        botonPorcentajeCalculadorMenos = findViewById(R.id.botonPorcentajeCalculadorMenos);
        botonGuardar = findViewById(R.id.botonGuardar);
        botonModificar = findViewById(R.id.botonModificar);
        botonCazar.setOnTouchListener(onTouchListener);
        botonCorta.setOnTouchListener(onTouchListener);
        botonLarga.setOnTouchListener(onTouchListener);
        botonCerrar.setOnTouchListener(onTouchListener);
        botonPorcentajes.setOnTouchListener(onTouchListener);
        botonClear.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculador.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculadorMas.setOnTouchListener(onTouchListener);
        botonPorcentajeCalculadorMenos.setOnTouchListener(onTouchListener);
        botonGuardar.setOnTouchListener(onTouchListener);
        botonModificar.setOnTouchListener(onTouchListener);
        textoPorcentaje = findViewById(R.id.textoPorcentaje);
        textoPorcentajeMod = findViewById(R.id.textoPorcentajeMod);
        textoPorcentajeMod.addTextChangedListener(textWatcher);
        textoGanancia = findViewById(R.id.textoGanancia);
        textoPrecio = findViewById(R.id.textoPrecio);
        textoPrecio.setOnTouchListener(onTouchListener);
        textoPrecioMod = findViewById(R.id.textoPrecioMod);
        textoPrecioMod.addTextChangedListener(textWatcher);
        textoReferencia = findViewById(R.id.referencia);
        if(referencia != null)
            textoReferencia.setText(referencia);
        textoInvertido = findViewById(R.id.textoInvertido);
        textoInvertido.setOnTouchListener(onTouchListener);
        textoInvertidoActual = findViewById(R.id.textoInvertidoActual);
        textoInvertidoActual.setOnTouchListener(onTouchListener);
        textoUsado = findViewById(R.id.textoUsado);
        textoUsado.setOnTouchListener(onTouchListener);
        textoGananciaLetra = findViewById(R.id.textoGanadoLetra);
        textoBase = findViewById(R.id.textoBase);
        textoBase.setText(String.format(precisionPrecio, precio) + " " + monedaOrigenNombre);
        textoBase.setOnTouchListener(onTouchListener);
        textoLiquidez = findViewById(R.id.textoLiquidez);
        textoLiquidez.setText("0.00 " + liquidezNombre);
        textoLiquidez.setOnTouchListener(onTouchListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (modo == modoCazar) {
            setBotonCazar();
        } else if (modo == modoCorta) {
            setBotonCorta();
        } else if (modo == modoLarga) {
            setBotonLarga();
        }


        setBotonPorcentajesAplanado();
        ajustadorPorcentajes = 1;
        ajustadorPosicion(ajustadorPorcentajes);
        textoPrecioMod.setText(db.getPrecioOut());
        textoPrecioMod.clearFocus();
        textoReferencia.clearFocus();

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
        invertido = Double.parseDouble(db.getInversionInicio());
        precio = Double.parseDouble(db.getPrecioIn());
        invertidoDestino = Double.parseDouble(db.getUsando());

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
        botonPorcentajesAplanado = db.getBotonPorcentajesAplanado();
        precisionOrigen = db.getPrecisionOrigenFormato().replace(".", ",.");
        precisionDestino = db.getPrecisionDestinoFormato().replace(".", ",.");
        precisionLiquidez = db.getPrecisionLiquidezFormato().replace(".", ",.");
        precisionPrecio = db.getPrecisionPrecioFormato().replace(".",",.");
        liquidezOrigen = Double.parseDouble(db.getLiquidezOrigen());
        liquidezDestino = Double.parseDouble(db.getLiquidezDestino());

        if(db.getReferencia() != null) {
           referencia = db.getReferencia();
        }

        realm.close();

    }


    boolean positivo;
    String textoGanadoGuardar;
    String textoActualGuardar;
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

                    if (modo == modoCorta || modo == modoLarga)
                        textoGanancia.setText("0.00 " + monedaOrigenNombre);
                    else
                        textoGanancia.setText("0.00 " + monedaDestinoNombre);
                    return;
                }

            } else {
                if (textoPrecioMod.getText().toString().equals(".") ||
                        textoPrecioMod.getText().toString().isEmpty()
                        ) {

                    textoPorcentaje.setText("+0.00%");
                    if (modo == modoCorta || modo == modoLarga)
                        textoGanancia.setText("0.00 " + monedaOrigenNombre);
                    else
                        textoGanancia.setText("0.00 " + monedaDestinoNombre);
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

                textoLiquidez.setText(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);


                if (botonporcentajeCalculadorAplanado)
                    textoPrecio.setText(String.format(precisionPrecio, precioFinal));

                if (positivo) {
                    textoGanadoGuardar = String.format(precisionOrigen, gananciaFinal);
                    textoGanancia.setText(textoGanadoGuardar + " " + monedaOrigenNombre);
                    textoPorcentaje.setText("+" + String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Ganado");
                } else {
                    textoGanadoGuardar = String.format(precisionOrigen, gananciaFinal);
                    textoGanancia.setText(textoGanadoGuardar.substring(1) + " " + monedaOrigenNombre);
                    textoPorcentaje.setText(String.format("%.2f", porcentajeFinal) + "%");
                    textoGananciaLetra.setText("Perdido");

                }
                textoActualGuardar = String.format(precisionOrigen, invertidoActual);
                textoInvertidoActual.setText(textoActualGuardar + " " + monedaOrigenNombre);

            } else {

                calculoModoCazar();

                textoLiquidez.setText(String.format(precisionLiquidez, liquidez) + " " + liquidezNombre);


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


        liquidez = invertidoActual * liquidezDestino;
        positivo = gananciaFinal > 0;


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

                double a;
                a = invertidoDestino * (1 + porcentajeIngresado);
                resultadoComisionSalida = a * comisionSalida;
                resultadoComisionEntrada = invertidoDestino * comisionEntrada;
                a += resultadoComisionSalida + resultadoComisionEntrada;
                precioFinal = invertido / a;
            }


            invertidoActual = invertido * (1 + porcentajeIngresado);
            gananciaFinal = invertidoActual - invertidoFinal;


        } else {

            if (enForex) {

                double x = precioIngresado + comisionSalida;
                double y = precio - comisionEntrada;
                porcentajeFinal = x / y;
                porcentajeFinal -= 1;
                porcentajeFinal *= -1;


            } else {

                double a;
                a = invertido / precioIngresado;
                a -= invertidoDestino * comisionEntrada;
                a /= 1 + comisionSalida;
                porcentajeFinal = a / invertidoDestino;
                porcentajeFinal -= 1;

            }


            invertidoActual = invertido * (1 + porcentajeFinal);
            gananciaFinal = invertidoActual - invertidoFinal;
            porcentajeFinal *= 100;
            positivo = gananciaFinal > 0;

        }
        liquidez = invertidoActual * liquidezOrigen;

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
                a = invertido * (1 + porcentajeIngresado);
                a *= (1 + comisionSalida);
                a += (invertido * comisionEntrada);
                precioFinal = a / invertidoDestino;
            }


            invertidoActual = invertido * (1 + porcentajeIngresado);
            gananciaFinal = invertidoActual - invertidoFinal;


        } else {

            if (enForex) {

                double x = precioIngresado - comisionSalida;
                double y = precio + comisionEntrada;
                porcentajeFinal = x / y;
                porcentajeFinal -= 1;


            } else {

                double a = invertidoDestino;
                a *= precioIngresado;
                a -= invertido * comisionEntrada;
                a /= 1 + comisionSalida;
                porcentajeFinal = a / invertido;
                porcentajeFinal -= 1;

            }

            invertidoActual = invertido * (1 + (porcentajeFinal));
            gananciaFinal = invertidoActual - invertidoFinal;
            porcentajeFinal *= 100;
            positivo = gananciaFinal > 0;


        }
        liquidez = invertidoActual * liquidezOrigen;

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
                        drawer.closeDrawer(Gravity.START);
                        setBotonPorcentajesAplanado();


                        break;
                    }

                    case R.id.botonClear: {
                        vibrator.vibrate(50);
                        limpiarCalculador();
                        break;
                    }

                    case R.id.textoInvertido: {
                        exportarPrecio(textoInvertido);
                        break;
                    }

                    case R.id.textoInvertidoActual: {
                        exportarPrecio(textoInvertidoActual);
                        break;
                    }

                    case R.id.textoUsado: {
                        exportarPrecio(textoUsado);
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

                    case R.id.textoLiquidez: {
                        exportarPrecio(textoLiquidez);
                        break;
                    }

                    case R.id.botonCerrar: {
                        Intent i = getBaseContext().getPackageManager().
                                getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        vibrator.vibrate(500);
                        finish();

                        break;
                    }

                    case R.id.botonPorcentajeCalculador: {
                        vibrator.vibrate(50);

                        if (!botonporcentajeCalculadorAplanado) {

                            botonporcentajeCalculadorAplanado = true;
                            botonPorcentajeCalculador.setBackgroundResource(R.drawable.fondo_botones_presionado);
                            textoPorcentaje.setVisibility(View.GONE);
                            textoPorcentajeMod.setVisibility(View.VISIBLE);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoPrecio.setVisibility(View.VISIBLE);
                            textoPrecioMod.setVisibility(View.GONE);
                            botonPorcentajeCalculadorMas.setVisibility(View.VISIBLE);
                            botonPorcentajeCalculadorMenos.setVisibility(View.VISIBLE);
                            if (modo == modoCorta || modo == modoLarga)
                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);


                        } else {
                            botonporcentajeCalculadorAplanado = false;
                            botonPorcentajeCalculador.setBackgroundResource(R.drawable.fondo_botones);
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
                        textoLiquidez.setText("0.00 " + liquidezNombre);

                        break;
                    }

                    case R.id.botonPorcentajeCalculadorMas: {
                        vibrator.vibrate(50);

                        if (!botonPorcentajeCalculadorMasAplanado) {
                            botonPorcentajeCalculadorMasAplanado = true;
                            botonPorcentajeCalculadorMas.setBackgroundResource(R.drawable.fondo_botones_presionado);
                            botonPorcentajeCalculadorMenos.setBackgroundResource(R.drawable.fondo_botones);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoLiquidez.setText("0.00 " + liquidezNombre);
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
                            botonPorcentajeCalculadorMas.setBackgroundResource(R.drawable.fondo_botones);
                            botonPorcentajeCalculadorMenos.setBackgroundResource(R.drawable.fondo_botones_presionado);
                            textoPorcentajeMod.setText("");
                            textoPrecio.setText("Precio");
                            textoLiquidez.setText("0.00 " + liquidezNombre);
                            if (modo == modoCorta || modo == modoLarga)

                                textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
                            else
                                textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
                            positivo = false;
                        }

                        break;
                    }

                    case R.id.botonGuardar: {
                        if (textoPrecioMod.getText().toString().isEmpty())
                            break;
                        Snackbar.make(findViewById(R.id.calculos), "Guardado", Snackbar.LENGTH_LONG)
                                .show();
                        vibrator.vibrate(500);
                        guardarOrigen();
                        break;
                    }

                    case R.id.botonModificar: {
                        vibrator.vibrate(500);
                        drawer.closeDrawer(Gravity.START);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("idOperacion", idOperacion);
                        startActivity(i);
                        //finish();

                    }

                }

            }
            return true;
        }
    };


    Realm realm;

    void guardarOrigen() {

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        final DB db = realm.where(DB.class).equalTo("id", idOperacion).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                db.setPrecioOut(textoPrecioMod.getText().toString().replace(",", ""));
                db.setGanadoInicio(textoGanadoGuardar.replace(",", ""));
                db.setActualInicio(textoActualGuardar.replace(",", ""));
                db.setBotonPorcentajesAplanado(!botonPorcentajesAplanado);
                db.setModo(modo);

                if(!textoReferencia.getText().toString().isEmpty())
                    db.setReferencia(textoReferencia.getText().toString().toUpperCase());
                else {
                    if (db.getReferencia() != null)
                        db.setReferencia(null);
                }

            }
        });
        realm.close();


    }

    private void exportarPrecio(TextView text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Precio", text.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Precio grabado: " + text.getText().toString(), Toast.LENGTH_SHORT).show();
        vibrator.vibrate(500);
    }

    private void limpiarCalculador() {


        textoPrecioMod.setText("");
        textoPorcentajeMod.setText("");
        textoPorcentaje.setText("+0.00%");
        textoPrecio.setText("Precio");
        textoLiquidez.setText("0.00 " + liquidezNombre);

        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("+0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        } else {

            textoGanancia.setText("+0.00 " + monedaDestinoNombre);
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
        encabezado.setText("cazar " + monedaDestinoNombre + " con " + monedaOrigenNombre);


        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("+0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);


        } else {

            textoGanancia.setText("+0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }
        textoPrecioMod.setText("");
        textoPrecioMod.clearFocus();
        textoPorcentaje.setText("+0.00%");
        textoLiquidez.setText("0.00 " + liquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonCorta() {
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonCorta.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);
        modo = modoCorta;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("corta " + monedaDestinoNombre + " con " + monedaOrigenNombre);
        if (modo == modoCorta || modo == modoLarga) {

            textoGanancia.setText("+0.00 " + monedaOrigenNombre);
            textoInvertido.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoInvertidoActual.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);
            textoUsado.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);

        } else {

            textoGanancia.setText("+0.00 " + monedaDestinoNombre);
            textoInvertido.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoInvertidoActual.setText(String.format(precisionDestino, invertidoDestino) + " " + monedaDestinoNombre);
            textoUsado.setText(String.format(precisionOrigen, invertido) + " " + monedaOrigenNombre);

        }

        textoPrecioMod.setText("");
        textoPrecioMod.clearFocus();
        textoPorcentaje.setText("+0.00%");
        textoLiquidez.setText("0.00 " + liquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }


    private void setBotonLarga() {
        botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
        botonLarga.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        modo = modoLarga;
        adapterRecyclerPorcentajes.cambioModo(modo);
        encabezado.setText("larga " + monedaDestinoNombre + " con " + monedaOrigenNombre);
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

        textoPrecioMod.setText("");
        textoPrecioMod.clearFocus();
        textoPorcentaje.setText("+0.00%");
        textoLiquidez.setText("0.00 " + liquidezNombre);
        ajustadorPosicion(ajustadorPorcentajes);
        vibrator.vibrate(50);
    }

    private void setBotonPorcentajesAplanado() {
        if (botonPorcentajesAplanado) {
           // TransitionManager.beginDelayedTransition(calculador);
            calculador.setVisibility(View.GONE);
            recyclerPorcentajes.setVisibility(View.VISIBLE);
            recyclerBotonesPorcentajes.setVisibility(View.VISIBLE);
            botonPorcentajesAplanado = false;
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
            botonGuardar.setVisibility(View.GONE);

        } else {
          //  TransitionManager.beginDelayedTransition(calculador);
            calculador.setVisibility(View.VISIBLE);
            recyclerPorcentajes.setVisibility(View.GONE);
            recyclerBotonesPorcentajes.setVisibility(View.GONE);
            botonPorcentajesAplanado = true;
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior);
            botonGuardar.setVisibility(View.VISIBLE);
        }
        vibrator.vibrate(50);
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
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        super.onBackPressed();
    }
}
