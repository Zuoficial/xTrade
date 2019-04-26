package com.smoowy.xTrade;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ComunicadorInversiones {

    Button botonCazar, botonCorta, botonLarga, botonEmpezar,
            botonPorcentajes, botonAgregarInversion, botonMenu, botonForex, botonComisiones, botonLotes;
    EditText invertido, precio, comisionEntrada, comisionSalida, monedaOrigen, monedaDestino,
            precisionOrigen, precisionDestino, liquidezOrigen, liquidezDestino,
            liquidezNombre, precisionLiquidez, precisionPrecio;
    TextView encabezadoInversion, comisionEntradaLetra, comisionSalidaLetra, encabezadoLiquidez, textoPrecio;
    int selectorCambioInversion = 0, idOperacion;
    final int cambioInversionOrigen = 0, cambioInversionDestino = 1,
            cambioInversionALiquidez = 2;
    boolean botonPorcentajesAplanado, botonAgregarInversionAplanado,
            enForex, botonComisionAplanado, botonLotesAplanado, hayContrato;
    String precioIn, inversionInicio, inversionDestinoInicio;
    String precisionOrigenFormato, precisionDestinoFormato, precisionLiquidezFormato,
            precisionPrecioFormato;
    Vibrator vibrator;
    DrawerLayout drawer;
    RecyclerView recyclerViewInversiones;
    int modo = 0, modoLiquidez = 0;
    final int modoCazar = 0, modoCorta = 1, modoLarga = 2;
    Realm realm;
    DB db;
    boolean esDuplicado, comisionEntradaNegativa, comisionSalidaNegativa, vibracionActivada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botonCazar = findViewById(R.id.botonCazar);
        botonCorta = findViewById(R.id.botonCorta);
        botonLarga = findViewById(R.id.botonLarga);
        botonEmpezar = findViewById(R.id.botonEmpezar);
        botonComisiones = findViewById(R.id.botonComisiones);
        botonPorcentajes = findViewById(R.id.botonPorcentajes);
        botonAgregarInversion = findViewById(R.id.botonAgregarInversion);
        botonMenu = findViewById(R.id.botonMenu);
        botonForex = findViewById(R.id.botonForex);
        botonLotes = findViewById(R.id.botonLotes);
        encabezadoInversion = findViewById(R.id.encabezadoInversion);
        encabezadoInversion.setOnLongClickListener(onLongClickListener);
        encabezadoLiquidez = findViewById(R.id.encabezadoLiquidez);
        encabezadoLiquidez.setOnLongClickListener(onLongClickListener);
        invertido = findViewById(R.id.inversion);
        ponerKeyListener(invertido);
        precio = findViewById(R.id.precio);
        ponerKeyListener(precio);
        comisionEntrada = findViewById(R.id.comisionEntrada);
        comisionEntradaLetra = findViewById(R.id.comisionEntradaLetra);
        comisionEntradaLetra.setOnClickListener(view -> {
            vibrar(200);
            if (!comisionEntradaNegativa) {
                comisionEntradaNegativa = true;

                comisionEntradaLetra.setText("Comision entrada +%");

            } else {

                comisionEntradaNegativa = false;

                comisionEntradaLetra.setText("Comision entrada %");


            }


        });
        comisionSalida = findViewById(R.id.comisionSalida);
        comisionSalidaLetra = findViewById(R.id.comisionSalidaLetra);
        comisionSalidaLetra.setOnClickListener(view -> {
            vibrar(200);
            if (!comisionSalidaNegativa) {
                comisionSalidaNegativa = true;

                comisionSalidaLetra.setText("Comision salida +%");

            } else {

                comisionSalidaNegativa = false;

                comisionSalidaLetra.setText("Comision salida %");


            }


        });
        liquidezDestino = findViewById(R.id.liquidezDestino);
        ponerKeyListener(liquidezDestino);
        liquidezOrigen = findViewById(R.id.liquidezOrigen);
        ponerKeyListener(liquidezOrigen);
        liquidezNombre = findViewById(R.id.liquidezNombre);
        monedaOrigen = findViewById(R.id.origen);
        monedaDestino = findViewById(R.id.destino);
        precisionOrigen = findViewById(R.id.precisionOrigen);
        precisionDestino = findViewById(R.id.precisionDestino);
        precisionLiquidez = findViewById(R.id.precisionLiquidez);
        precisionPrecio = findViewById(R.id.precisionPrecio);
        botonComisiones = findViewById(R.id.botonComisiones);
        botonComisiones.setOnClickListener(onClickListener);
        textoPrecio = findViewById(R.id.texto_precio);
        textoPrecio.setOnClickListener(onClickListener);
        botonCazar.setOnClickListener(onClickListener);
        botonCorta.setOnClickListener(onClickListener);
        botonLarga.setOnClickListener(onClickListener);
        botonEmpezar.setOnClickListener(onClickListener);
        botonPorcentajes.setOnClickListener(onClickListener);
        botonAgregarInversion.setOnClickListener(onClickListener);
        botonMenu.setOnClickListener(onClickListener);
        botonForex.setOnClickListener(onClickListener);
        botonLotes.setOnClickListener(onClickListener);
        encabezadoLiquidez.setOnClickListener(onClickListener);
        encabezadoInversion.setOnClickListener(onClickListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        precisionOrigen.addTextChangedListener(textWatcher);
        precisionDestino.addTextChangedListener(textWatcher);
        precisionPrecio.addTextChangedListener(textWatcher);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        botonPorcentajesAplanado = false;
        setRecyclerViewInversiones();
        acccederDB();

    }


    Dialog dialog;
    EditText etPrecioDialogCon, etCantidadDialogCon;
    TextView tValorDialogCon;
    Button bSalirDialogCon;
    String precioContrato = "", cantidadContrato = "", valorContrato = "";

    void crearDialogContrato() {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_contrato);
        dialog.show();
        etPrecioDialogCon = dialog.findViewById(R.id.dialog_contrato_et_precio);
        etCantidadDialogCon = dialog.findViewById(R.id.dialog_contrato_et_cantidad);
        tValorDialogCon = dialog.findViewById(R.id.dialog_contrato_t_valor);
        bSalirDialogCon = dialog.findViewById(R.id.dialog_contrato_b_salir);
        bSalirDialogCon.setOnClickListener(onClickListenerDialogContrato);
        etPrecioDialogCon.addTextChangedListener(textWatcherDialog);
        etCantidadDialogCon.addTextChangedListener(textWatcherDialog);

        if (hayContrato) {
            etPrecioDialogCon.setText(String.valueOf(precioContrato));
            etCantidadDialogCon.setText(String.valueOf(cantidadContrato));
        }

    }

    TextWatcher textWatcherDialog = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (!etPrecioDialogCon.getText().toString().equals("") &&
                    !etPrecioDialogCon.getText().toString().equals(".") &&
                    !etCantidadDialogCon.getText().toString().equals("") &&
                    !etCantidadDialogCon.getText().toString().equals(".")) {

                double precio = Double.parseDouble(etPrecioDialogCon.getText().toString());
                double cantidad = Double.parseDouble(etCantidadDialogCon.getText().toString());
                double resultado = precio * cantidad;

                tValorDialogCon.setText(String.format(precisionPrecioFormato, resultado));
                hayContrato = true;
                precioContrato = String.valueOf(precio);
                cantidadContrato = String.valueOf(cantidad);
                valorContrato = String.format(precisionPrecioFormato, resultado);

            } else
                tValorDialogCon.setText("0.00");
            hayContrato = false;
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    View.OnClickListener onClickListenerDialogContrato = view -> {
        if (!tValorDialogCon.getText().toString().equals("0.00")) {
            precio.setText(etPrecioDialogCon.getText().toString());
            textoPrecio.setText("Precio destino ^");
            hayContrato = true;
        } else
            textoPrecio.setText("Precio destino");
        dialog.dismiss();
    };

    public void recuperarInformacionContrato(String precioContrato, String cantidadContrato) {

        this.precioContrato = precioContrato;
        this.cantidadContrato = cantidadContrato;
        hayContrato = true;
        textoPrecio.setText("Precio destino ^");
    }


    void ponerKeyListener(EditText editText) {
        editText.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_NUMPAD_ADD) {
                editText.setText(editText.getText() + "000");
                editText.setSelection(editText.getText().length());
                return true;
            } else
                return false;
        });
    }


    void vibrar(Integer valor) {

        if (vibracionActivada)
            vibrator.vibrate(valor);
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (precisionOrigen.getText().toString().isEmpty()) {

                precisionOrigenFormato = "%,.2f";

            } else
                precisionOrigenFormato = "%,." + precisionOrigen.getText().toString() + "f";


            if (precisionDestino.getText().toString().isEmpty()) {

                precisionDestinoFormato = "%,.2f";

            } else
                precisionDestinoFormato = "%,." + precisionDestino.getText().toString() + "f";


            if (precisionPrecio.getText().toString().isEmpty()) {

                precisionPrecioFormato = "%,.2f";

            } else
                precisionPrecioFormato = "%,." + precisionPrecio.getText().toString() + "f";


            adapterRecyclerInversiones.precisionOrigenFormato = precisionOrigenFormato;
            adapterRecyclerInversiones.precisionDestinoFormato = precisionDestinoFormato;
            adapterRecyclerInversiones.precisionPrecioFormato = precisionPrecioFormato;
            adapterRecyclerInversiones.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private void acccederDB() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        idOperacion = getIntent().getExtras().getInt("idOperacion");
        esDuplicado = getIntent().getExtras().getBoolean("esDuplicado");
        db = realm.where(DB.class).equalTo("id", idOperacion).findFirst();

        if (db == null)
            realm.executeTransaction(realm -> db = realm.createObject(DB.class, idOperacion));

        monedaOrigen.setText(db.getMonedaOrigen());
        monedaDestino.setText(db.getMonedaDestino());
        invertido.setText(db.getInvertido());
        precio.setText(db.getPrecio());

        if (db.getComisionEntradaNegativa() != null) {
            comisionEntradaNegativa = db.getComisionEntradaNegativa();
            comisionSalidaNegativa = db.getComisionSalidaNegativa();
        }

        if (db.getEnForex() != null) {
            if (db.getEnForex()) {
                setBotonForex();
            }
            if (!comisionEntradaNegativa)
                comisionEntradaLetra.setText("Comision entrada %");
            else
                comisionEntradaLetra.setText("Comision entrada +%");

            if (!comisionSalidaNegativa)
                comisionSalidaLetra.setText("Comision salida %");
            else
                comisionSalidaLetra.setText("Comision salida +%");

        }

        if (!comisionEntradaNegativa) {
            if (db.getComisionEntrada() == null || !db.getComisionEntrada().equals("0"))
                comisionEntrada.setText(db.getComisionEntrada());
        } else {

            if (db.getComisionEntrada() == null || !db.getComisionEntrada().equals("0"))
                comisionEntrada.setText(db.getComisionEntrada().replace("-", ""));
        }

        if (!comisionSalidaNegativa) {
            if (db.getComisionSalida() == null || !db.getComisionSalida().equals("0"))
                comisionSalida.setText(db.getComisionSalida());
        } else {
            if (db.getComisionSalida() == null || !db.getComisionSalida().equals("0"))
                comisionSalida.setText(db.getComisionSalida().replace("-", ""));
        }

        if (db.getLiquidezOrigen() == null || !db.getLiquidezOrigen().equals("0"))
            liquidezOrigen.setText(db.getLiquidezOrigen());
        if (db.getLiquidezDestino() == null || !db.getLiquidezDestino().equals("0"))
            liquidezDestino.setText(db.getLiquidezDestino());
        precisionOrigen.setText(db.getPrecisionOrigen());
        precisionDestino.setText(db.getPrecisionDestino());
        liquidezNombre.setText(db.getLiquidezNombre());
        precisionLiquidez.setText(db.getPrecisionLiquidez());
        if (precisionPrecio != null)
            precisionPrecio.setText(db.getPrecisionPrecio());
        else
            precisionPrecioFormato = "%,.2f";

        if (db.getHayContrato() != null) {

            if (hayContrato = db.getHayContrato()) {
                textoPrecio.setText("Precio destino ^");
                precioContrato = db.getPrecioContrato();
                cantidadContrato = db.getCantidadContrato();
            }

        }
        adapterRecyclerInversiones.monedaOrigen = db.getMonedaOrigen();
        adapterRecyclerInversiones.monedaDestino = db.getMonedaDestino();
        adapterRecyclerInversiones.lista.addAll(db.operaciones);
        adapterRecyclerInversiones.datos = adapterRecyclerInversiones.lista.size();
        adapterRecyclerInversiones.hayContrato =hayContrato;
        adapterRecyclerInversiones.notifyDataSetChanged();
        modo = db.getModo();
        if (modo == 0)
            modo = 2;
        modoLiquidez = db.getModoLiquidez();
        cambiarModo();
        cambiarModoLiquidez(true);

        if (db.getBotonPorcentajesAplanado() != null) {
            botonPorcentajesAplanado = db.getBotonPorcentajesAplanado();

            if (botonPorcentajesAplanado) {
                botonPorcentajesAplanado = false;
                setBotonPorcentajes();
            }
        } else
            setBotonPorcentajes();


        if (db.getBotonComisionAplanado() != null) {
            botonComisionAplanado = db.getBotonComisionAplanado();

            if (botonComisionAplanado) {
                botonComisionAplanado = false;
                setBotonComisiones();
            }
        } else
            setBotonComisiones();


        if (esDuplicado) {

            realm.executeTransaction(realm -> {
                int id = realm.where(DB.class).findAll().max("id").intValue();
                id += 1;
                db = realm.createObject(DB.class, id);
                idOperacion = id;

            });
        }

        if (db.getBotonLotesAplanado() != null)
            botonLotesAplanado = db.getBotonLotesAplanado();

        if (botonLotesAplanado)
            botonLotes.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
        else
            botonLotes.setBackgroundResource(R.drawable.fondo_botones_superior);



        realm.close();
    }


    public void guardarDB() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> {


            double inversionOrig = 0, inversionNuev, precioOrig = 0, precioNuev;

            if (db.getInversionLiqInicio() != null)
                inversionOrig = Double.parseDouble(db.getInversionInicio());
            inversionNuev = Double.parseDouble(inversionInicio);
            if (db.getPrecioIn() != null)
                precioOrig = Double.parseDouble(db.getPrecioIn());
            precioNuev = Double.parseDouble(precioIn);

            if (inversionOrig != inversionNuev || precioOrig != precioNuev) {

                db.deleteFromRealm();
                db = realm.createObject(DB.class, idOperacion);
            }


            db.setMonedaOrigen(monedaOrigen.getText().toString().toUpperCase());
            db.setMonedaDestino(monedaDestino.getText().toString().toUpperCase());
            db.setInvertido(invertido.getText().toString());
            db.setPrecio(precio.getText().toString());
            db.setEnForex(enForex);
            db.setLiquidezOrigen(liquidezOrigen.getText().toString().isEmpty() ? "0" : liquidezOrigen.getText().toString());
            db.setLiquidezDestino(liquidezDestino.getText().toString().isEmpty() ? "0" : liquidezDestino.getText().toString());
            db.setLiquidezNombre(liquidezNombre.getText().toString().toUpperCase());
            db.operaciones.clear();
            db.operaciones.addAll(adapterRecyclerInversiones.lista);
            db.setModo(modo);
            db.setModoLiquidez(modoLiquidez);
            db.setInversionInicio(inversionInicio);
            db.setActualInicio(inversionInicio);
            db.setUsando(inversionDestinoInicio);
            db.setPrecioIn(precioIn);
            db.setPrecisionOrigen(precisionOrigen.getText().toString());
            db.setPrecisionDestino(precisionDestino.getText().toString());
            db.setPrecisionOrigenFormato(precisionOrigenFormato);
            db.setPrecisionDestinoFormato(precisionDestinoFormato);
            db.setPrecisionLiquidez(precisionLiquidez.getText().toString());
            db.setPrecisionLiquidezFormato(precisionLiquidezFormato);
            db.setPrecisionPrecio(precisionPrecio.getText().toString());
            db.setPrecisionPrecioFormato(precisionPrecioFormato);
            db.setBotonPorcentajesAplanado(botonPorcentajesAplanado);
            db.setBotonComisionAplanado(botonComisionAplanado);
            db.setBotonLotesAplanado(botonLotesAplanado);
            db.setComisionEntradaNegativa(comisionEntradaNegativa);
            db.setComisionSalidaNegativa(comisionSalidaNegativa);
            db.setHayContrato(hayContrato);
            db.setPrecioContrato(precioContrato);
            db.setCantidadContrato(cantidadContrato);

            if (!comisionEntradaNegativa) {
                db.setComisionEntrada(comisionEntrada.getText().toString().isEmpty() ? "0" :
                        comisionEntrada.getText().toString());
            } else {
                db.setComisionEntrada(comisionEntrada.getText().toString().isEmpty() ? "0" : "-" +
                        comisionEntrada.getText().toString());
            }

            if (!comisionSalidaNegativa) {
                db.setComisionSalida(comisionSalida.getText().toString().isEmpty() ? "0" :
                        comisionSalida.getText().toString());

            } else {
                db.setComisionSalida(comisionSalida.getText().toString().isEmpty() ? "0" : "-" +
                        comisionSalida.getText().toString());
            }

        });

        realm.close();


    }

    AdapterRecyclerInversiones adapterRecyclerInversiones;

    private void setRecyclerViewInversiones() {
        recyclerViewInversiones = findViewById(R.id.recyclerInversiones);
        adapterRecyclerInversiones = new AdapterRecyclerInversiones(this);
        recyclerViewInversiones.setAdapter(adapterRecyclerInversiones);
        recyclerViewInversiones.setLayoutManager(new LinearLayoutManager(this));
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


                adapterRecyclerInversiones.quitarDatos(viewHolder.getLayoutPosition());


                Snackbar snackbar =
                        Snackbar.make(findViewById(R.id.botonEmpezar), "Inversion borrada!", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
                snackbar.setAction("Regresar", view -> adapterRecyclerInversiones.recuperarDatos()).show();


            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });


        itemTouchHelper.attachToRecyclerView(recyclerViewInversiones);

    }


    View.OnClickListener onClickListener = view -> {

        vibrar(50);
        switch (view.getId()) {
            case R.id.botonCazar: {
                modo = modoCazar;
                cambiarModo();
                break;
            }
            case R.id.botonCorta: {

                modo = modoCorta;
                cambiarModo();
                break;
            }


            case R.id.botonLarga: {
                modo = modoLarga;
                cambiarModo();
                break;
            }

            case R.id.botonComisiones: {
                setBotonComisiones();
                break;
            }

            case R.id.botonPorcentajes: {
                setBotonPorcentajes();
                break;
            }

            case R.id.botonEmpezar: {
                empezarCalculos();
                break;
            }

            case R.id.botonAgregarInversion: {

                botonAgregarInversionAplanado = true;
                if (checarSiFaltanDatos())
                    break;

                agregarInversion();
                break;
            }

            case R.id.encabezadoInversion: {


                selectorCambioInversion += 1;

                if (selectorCambioInversion > 2)
                    selectorCambioInversion = 0;


                switch (selectorCambioInversion) {


                    case cambioInversionOrigen: {
                        encabezadoInversion.setText("Inversion origen");
                        break;

                    }
                    case cambioInversionDestino: {
                        encabezadoInversion.setText("Inversion destino");
                        break;

                    }
                    case cambioInversionALiquidez: {
                        encabezadoInversion.setText("Inversion a liquidez");
                        break;
                    }

                }
                break;
            }
            case R.id.botonForex: {

                setBotonForex();


                break;
            }

            case R.id.encabezadoLiquidez: {

                cambiarModoLiquidez(false);
                break;
            }

            case R.id.texto_precio:
                crearDialogContrato();
                break;

            case R.id.botonMenu: {

                drawer.openDrawer(Gravity.START);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
            }

            case R.id.botonLotes:
                botonLotesAplanado = !botonLotesAplanado;

                if (botonLotesAplanado)
                    botonLotes.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                else
                    botonLotes.setBackgroundResource(R.drawable.fondo_botones_superior);
        }

    };

    View.OnLongClickListener onLongClickListener = view -> {

        switch (view.getId()) {

            case R.id.encabezadoInversion:

                invertido.getText().clear();
                break;


            case R.id.encabezadoLiquidez: {

                liquidezNombre.getText().clear();
                liquidezOrigen.getText().clear();
                liquidezDestino.getText().clear();
                break;
            }

        }


        return false;
    };


    private void setBotonComisiones() {

        if (botonComisionAplanado) {
            botonComisiones.setBackgroundResource(R.drawable.fondo_botones_superior);
            botonComisionAplanado = false;
        } else {
            botonComisiones.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
            botonComisionAplanado = true;
        }

    }

    private void cambiarModoLiquidez(Boolean alInicio) {

        if (!alInicio) {

            modoLiquidez += 1;

            if (modoLiquidez > 3)
                modoLiquidez = 0;
        }

        switch (modoLiquidez) {


            case 0: {
                encabezadoLiquidez.setText("Origen a liquidez");
                break;
            }
            case 1: {
                encabezadoLiquidez.setText("Liquidez a origen");
                break;

            }
            case 2: {
                encabezadoLiquidez.setText("Destino a liquidez");
                break;

            }

            case 3: {
                encabezadoLiquidez.setText("Liquidez a destino");
                break;


            }


        }


    }

    private void setBotonForex() {
        if (enForex) {
            botonForex.setBackgroundResource(R.drawable.fondo_marcador_neutral);
            enForex = false;
            precisionPrecio.setText("");
            comisionEntrada.setText("");
        } else {
            botonForex.setBackgroundResource(R.drawable.fondo_boton_forex);
            enForex = true;
            precisionPrecio.setText("5");
            comisionEntrada.setText(".01");

        }
    }

    private void cambiarModo() {

        switch (modo) {

            case modoCazar: {
                botonCazar.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
                botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);
                break;
            }

            case modoCorta: {
                botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
                botonCorta.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                botonLarga.setBackgroundResource(R.drawable.fondo_botones_superior);
                break;

            }

            case modoLarga: {

                botonCazar.setBackgroundResource(R.drawable.fondo_botones_superior);
                botonCorta.setBackgroundResource(R.drawable.fondo_botones_superior);
                botonLarga.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
                break;

            }
        }
    }

    boolean noExistiaInversion;

    private void agregarInversion() {

        double precioImportar = 0, invertidoImportar = 0, cantidadImportar = 0;

        if (hayContrato)
            precioImportar = Double.parseDouble(valorContrato.replace(",", ""));
        else
            precioImportar = Double.parseDouble(precio.getText().toString());

        if (invertido.getText().toString().isEmpty()) {
            invertido.setText(String.valueOf(100));
            noExistiaInversion = true;
        }

        switch (selectorCambioInversion) {


            case cambioInversionOrigen: {
                invertidoImportar = Double.parseDouble(invertido.getText().toString());
                if (!noExistiaInversion) {
                    if (enForex)
                        invertidoImportar *= 100000;
                }
                cantidadImportar = invertidoImportar / precioImportar;
                break;

            }
            case cambioInversionDestino: {
                cantidadImportar = Double.parseDouble(invertido.getText().toString());
                if (!noExistiaInversion) {
                    if (enForex)
                        cantidadImportar *= 100000;
                }
                invertidoImportar = cantidadImportar * precioImportar;
                break;

            }
            case cambioInversionALiquidez: {

                double inversionX, liqOrigenX, liqDestinoX;


                switch (modoLiquidez) {

                    case 0:
                        if (invertido.getText().toString().isEmpty() ||
                                liquidezOrigen.toString().isEmpty()) {
                            crearSnackBar("Faltan datos de liquidez");
                            return;
                        }

                        inversionX = Double.parseDouble(invertido.getText().toString());
                        liqOrigenX = Double.parseDouble(liquidezOrigen.getText().toString());
                        invertidoImportar = inversionX / liqOrigenX;
                        break;
                    case 1:

                        if (invertido.getText().toString().isEmpty() ||
                                liquidezOrigen.toString().isEmpty()) {
                            crearSnackBar("Faltan datos de liquidez");
                            return;
                        }
                        inversionX = Double.parseDouble(invertido.getText().toString());
                        liqOrigenX = Double.parseDouble(liquidezOrigen.getText().toString());
                        invertidoImportar = inversionX * liqOrigenX;
                        break;
                    case 2:
                        if (invertido.getText().toString().isEmpty() ||
                                liquidezDestino.toString().isEmpty()) {
                            crearSnackBar("Faltan datos de liquidez");
                            return;
                        }
                        inversionX = Double.parseDouble(invertido.getText().toString());
                        liqDestinoX = Double.parseDouble(liquidezDestino.getText().toString());
                        invertidoImportar = inversionX / liqDestinoX;
                        break;
                    case 3:
                        if (invertido.getText().toString().isEmpty() ||
                                liquidezDestino.toString().isEmpty()) {
                            crearSnackBar("Faltan datos de liquidez");
                            return;
                        }
                        inversionX = Double.parseDouble(invertido.getText().toString());
                        liqDestinoX = Double.parseDouble(liquidezDestino.getText().toString());
                        invertidoImportar = inversionX * liqDestinoX;
                        break;
                }
                cantidadImportar = invertidoImportar / precioImportar;
                break;
            }
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.botonEmpezar),
                "Inversion agregada", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
        snackbar.show();
        precio.setText("");
        invertido.setText("");

        adapterRecyclerInversiones.hayContrato = hayContrato;
        adapterRecyclerInversiones.agregarDatos(precioImportar,
                invertidoImportar, cantidadImportar, precioContrato, cantidadContrato);
        adapterRecyclerInversiones.monedaOrigen = monedaOrigen.getText().toString().toUpperCase();
        adapterRecyclerInversiones.monedaDestino = monedaDestino.getText().toString().toUpperCase();

    }

    private void setBotonPorcentajes() {
        if (botonPorcentajesAplanado) {

            botonPorcentajes.setBackgroundResource(R.drawable.fondo_botones_superior);
            botonPorcentajesAplanado = false;
        } else {
            botonPorcentajes.setBackgroundResource(R.drawable.fondo_boton_forex_claro);
            botonPorcentajesAplanado = true;
        }
    }


    void empezarCalculos() {

        if (checarSiFaltanDatos()) {
            drawer.closeDrawer(Gravity.START);
            return;
        }


        if (!precio.getText().toString().isEmpty()) {

            if (invertido.getText().toString().isEmpty()) {
                invertido.setText(String.valueOf(100));
                noExistiaInversion = true;
            }
            agregarInversion();
        }


        Intent mIntent = new Intent(this, Calculos.class);


        if (precisionOrigen.getText().toString().isEmpty()) {

            precisionOrigenFormato = "%.2f";


        } else {

            precisionOrigenFormato = "%." + precisionOrigen.getText().toString() + "f";


        }


        if (precisionDestino.getText().toString().isEmpty()) {

            precisionDestinoFormato = "%.2f";


        } else {

            precisionDestinoFormato = "%." + precisionDestino.getText().toString() + "f";


        }

        if (precisionLiquidez.getText().toString().isEmpty()) {

            precisionLiquidezFormato = "%.2f";


        } else {

            precisionLiquidezFormato = "%." + precisionLiquidez.getText().toString() + "f";
        }

        if (precisionPrecio.getText().toString().isEmpty()) {

            precisionPrecioFormato = precisionOrigenFormato;


        } else {

            precisionPrecioFormato = "%." + precisionPrecio.getText().toString() + "f";
        }


        int numeroDeDatos = adapterRecyclerInversiones.lista.size();

        double invertidoFinalVarios = 0, invertidoVariosImportar = 0,
                inversion, precio, precioVariosImportar;


        for (int i = 0; i < numeroDeDatos; i++) {

            precio = adapterRecyclerInversiones.lista.get(i).getPrecio();
            inversion = adapterRecyclerInversiones.lista.get(i).getInversion();

            invertidoFinalVarios += (inversion / precio);
            invertidoVariosImportar += inversion;

        }

        precioVariosImportar = invertidoVariosImportar / invertidoFinalVarios;
        precioIn = String.format(precisionPrecioFormato, precioVariosImportar);
        inversionInicio = String.format(precisionOrigenFormato, invertidoVariosImportar);
        inversionDestinoInicio = String.format(precisionDestinoFormato,
                (invertidoVariosImportar / precioVariosImportar));
        guardarDB();

        mIntent.putExtra("idOperacion", idOperacion);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
    }


    void crearSnackBar(String texto) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.botonEmpezar),
                texto, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
        snackbar.show();
    }

    private boolean checarSiFaltanDatos() {
        if (liquidezNombre.getText().toString().isEmpty()) {

            if (!liquidezDestino.getText().toString().isEmpty() ||
                    !liquidezOrigen.getText().toString().isEmpty()) {

                crearSnackBar("Falta el nombre en liquidez!");
                return true;
            }
        }


        if (botonAgregarInversionAplanado) {
            botonAgregarInversionAplanado = false;
        }

        if (adapterRecyclerInversiones.lista.size() == 0 &&
                precio.getText().toString().isEmpty()) {

            crearSnackBar("Faltan datos por llenar!");
            return true;
        }


        if (monedaOrigen.getText().toString().isEmpty())
            monedaOrigen.setText("A");

        if (monedaDestino.getText().toString().isEmpty())
            monedaDestino.setText("B");


        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = getBaseContext().getPackageManager().
                    getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void recuperarDatosRecycler(String inversion, String precio) {
        vibrar(200);
        if (!enForex) {
            invertido.setText(inversion
                    .replace(",", "")
                    .replace(monedaOrigen.getText().toString().toUpperCase(), ""));
        } else {
            String np = inversion
                    .replace(",", "")
                    .replace(monedaOrigen.getText().toString().toUpperCase(), "");
            double n = Double.parseDouble(np);
            if (n >= 1000)
                n /= 100000;
            invertido.setText(String.valueOf(n));
        }
        this.precio.setText(precio
                .replace(",", "")
                .replace(monedaOrigen.getText().toString().toUpperCase(), ""));

        if (inversion.equals("0"))
            invertido.setText("");

    }

    @Override
    public void borrarInversionRecycler(Integer posicion) {

        adapterRecyclerInversiones.quitarDatos(posicion);


        Snackbar snackbar =
                Snackbar.make(findViewById(R.id.botonEmpezar), "Inversion borrada!", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
        snackbar.setAction("Regresar", view -> {
                    adapterRecyclerInversiones.recuperarDatos();
                    vibrar(500);
                }
        ).show();

        if (hayContrato && adapterRecyclerInversiones.datos == 0) {
            hayContrato = false;
            textoPrecio.setText("Precio destino");
        }


    }
}
/*

InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
 */