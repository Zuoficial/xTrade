package com.smoowy.xTrade;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button botonCazar, botonCorta, botonLarga, botonEmpezar,
            botonPorcentajes, botonAgregarInversion, botonCambioInversion, botonForex, botonComisiones;
    EditText invertido, precio, comisionEntrada, comisionSalida, monedaOrigen, monedaDestino,
            precisionOrigen, precisionDestino, liquidezOrigen, liquidezDestino,
            liquidezNombre, precisionLiquidez, precisionPrecio;
    TextView encabezadoInversion, comisionEntradaLetra, comisionSalidaLetra, encabezadoLiquidez;
    int selectorCambioInversion = 0, idOperacion;
    final int cambioInversionOrigen = 0, cambioInversionDestino = 1,
            cambioInversionOrigenLiquidez = 2, cambioInversionDestinoLiquidez = 3;
    boolean botonPorcentajesAplanado, botonAgregarInversionAplanado, enForex, botonComisionAplanado;
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
        botonCambioInversion = findViewById(R.id.botonCambioInversion);
        botonForex = findViewById(R.id.botonForex);
        encabezadoInversion = findViewById(R.id.encabezadoInversion);
        encabezadoLiquidez = findViewById(R.id.encabezadoLiquidez);
        invertido = findViewById(R.id.inversion);
        precio = findViewById(R.id.precio);
        comisionEntrada = findViewById(R.id.comisionEntrada);
        comisionEntradaLetra = findViewById(R.id.comisionEntradaLetra);
        comisionSalida = findViewById(R.id.comisionSalida);
        comisionSalidaLetra = findViewById(R.id.comisionSalidaLetra);
        liquidezDestino = findViewById(R.id.liquidezDestino);
        liquidezOrigen = findViewById(R.id.liquidezOrigen);
        liquidezNombre = findViewById(R.id.liquidezNombre);
        monedaOrigen = findViewById(R.id.origen);
        monedaDestino = findViewById(R.id.destino);
        precisionOrigen = findViewById(R.id.precisionOrigen);
        precisionDestino = findViewById(R.id.precisionDestino);
        precisionLiquidez = findViewById(R.id.precisionLiquidez);
        precisionPrecio = findViewById(R.id.precisionPrecio);
        botonComisiones = findViewById(R.id.botonComisiones);
        botonComisiones.setOnTouchListener(onTouchListener);
        botonCazar.setOnTouchListener(onTouchListener);
        botonCorta.setOnTouchListener(onTouchListener);
        botonLarga.setOnTouchListener(onTouchListener);
        botonEmpezar.setOnTouchListener(onTouchListener);
        botonPorcentajes.setOnTouchListener(onTouchListener);
        botonAgregarInversion.setOnTouchListener(onTouchListener);
        botonCambioInversion.setOnTouchListener(onTouchListener);
        botonForex.setOnTouchListener(onTouchListener);
        encabezadoLiquidez.setOnTouchListener(onTouchListener);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        precisionOrigen.addTextChangedListener(textWatcher);
        precisionDestino.addTextChangedListener(textWatcher);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        botonPorcentajesAplanado = false;
        setRecyclerViewInversiones();
        acccederDB();


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


            adapterRecyclerInversiones.precisionOrigenFormato = precisionOrigenFormato;
            adapterRecyclerInversiones.precisionDestinoFormato = precisionDestinoFormato;
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
        db = realm.where(DB.class).equalTo("id", idOperacion).findFirst();
        monedaOrigen.setText(db.getMonedaOrigen());
        monedaDestino.setText(db.getMonedaDestino());
        invertido.setText(db.getInvertido());
        precio.setText(db.getPrecio());

        if (db.getEnForex() != null) {


            if (db.getEnForex()) {
                setBotonForex();
                if (db.getComisionEntrada() == null || !db.getComisionEntrada().equals("0"))
                    comisionEntrada.setText(db.getComisionEntrada());


                if (db.getComisionSalida() == null || !db.getComisionSalida().equals("0"))
                    comisionSalida.setText(db.getComisionSalida());


            } else {

                enForex = db.getEnForex();
                if (db.getComisionEntrada() == null || !db.getComisionEntrada().equals("0"))
                    comisionEntrada.setText(db.getComisionEntrada());
                if (db.getComisionSalida() == null || !db.getComisionSalida().equals("0"))
                    comisionSalida.setText(db.getComisionSalida());
            }
        }


        if (db.getLiquidezOrigen() == null || !db.getLiquidezOrigen().equals("0"))
            liquidezOrigen.setText(db.getLiquidezOrigen());
        if (db.getLiquidezDestino() == null || !db.getLiquidezDestino().equals("0"))
            liquidezDestino.setText(db.getLiquidezDestino());
        precisionOrigen.setText(db.getPrecisionOrigen());
        precisionDestino.setText(db.getPrecisionDestino());
        liquidezNombre.setText(db.getLiquidezNombre());
        precisionLiquidez.setText(db.getPrecisionLiquidez());
        precisionPrecio.setText(db.getPrecisionPrecio());
        adapterRecyclerInversiones.monedaOrigen = db.getMonedaOrigen();
        adapterRecyclerInversiones.monedaDestino = db.getMonedaDestino();
        adapterRecyclerInversiones.lista.addAll(db.operaciones);
        adapterRecyclerInversiones.datos = adapterRecyclerInversiones.lista.size();
        adapterRecyclerInversiones.notifyDataSetChanged();
        modo = db.getModo();
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


        realm.close();
    }


    public void guardarDB() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (!inversionInicio.equals(db.getInversionInicio()) || !precioIn.equals(db.getPrecioIn())) {

                    db.deleteFromRealm();
                    db = realm.createObject(DB.class, idOperacion);
                }


                db.setMonedaOrigen(monedaOrigen.getText().toString().toUpperCase());
                db.setMonedaDestino(monedaDestino.getText().toString().toUpperCase());
                db.setInvertido(invertido.getText().toString());
                db.setPrecio(precio.getText().toString());
                if (enForex) {

                    db.setComisionEntrada(comisionEntrada.getText().toString().isEmpty() ? "0" :
                            comisionEntrada.getText().toString());
                    db.setComisionSalida(comisionSalida.getText().toString().isEmpty() ? "0" :
                            comisionSalida.getText().toString());


                } else {

                    db.setComisionEntrada(comisionEntrada.getText().toString().isEmpty() ? "0" : comisionEntrada.getText().toString());
                    db.setComisionSalida(comisionSalida.getText().toString().isEmpty() ? "0" : comisionSalida.getText().toString());

                }
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
                snackbar.setAction("Regresar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapterRecyclerInversiones.recuperarDatos();
                    }
                }).show();


            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });


        itemTouchHelper.attachToRecyclerView(recyclerViewInversiones);

    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                vibrator.vibrate(50);
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

                    case R.id.botonCambioInversion: {

                        //TODO FALTA AJUSTAR ESTO

                        selectorCambioInversion += 1;

                        if (selectorCambioInversion > 3)
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
                            case cambioInversionOrigenLiquidez: {
                                encabezadoInversion.setText("Inversion origen con liquidez");
                                break;

                            }

                            case cambioInversionDestinoLiquidez: {
                                encabezadoInversion.setText("Inversion destino con liquidez");
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


                }


            }


            return true;
        }
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
            comisionEntradaLetra.setText("Comision entrada %");
            comisionSalidaLetra.setText("Comision salida %");
            enForex = false;

        } else {
            botonForex.setBackgroundResource(R.drawable.fondo_boton_forex);
            comisionEntradaLetra.setText("Comision entrada pips");
            comisionSalidaLetra.setText("Comision salida pips");
            enForex = true;
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

    private void agregarInversion() {

        double precioImportar = 0, invertidoImportar = 0, cantidadImportar = 0;

        precioImportar = Double.parseDouble(precio.getText().toString());


        switch (selectorCambioInversion) {


            case cambioInversionOrigen: {
                invertidoImportar = Double.parseDouble(invertido.getText().toString());
                cantidadImportar = invertidoImportar / precioImportar;
                break;

            }
            case cambioInversionDestino: {
                cantidadImportar = Double.parseDouble(invertido.getText().toString());
                invertidoImportar = cantidadImportar * precioImportar;
                break;

            }
            case cambioInversionOrigenLiquidez: {
                invertidoImportar = Double.parseDouble(invertido.getText().toString());
                invertidoImportar /= Double.parseDouble(liquidezOrigen.getText().toString());
                cantidadImportar = invertidoImportar / precioImportar;
                break;
            }

            case cambioInversionDestinoLiquidez: {
                cantidadImportar = Double.parseDouble(invertido.getText().toString());
                cantidadImportar /= Double.parseDouble(liquidezDestino.getText().toString());
                invertidoImportar = cantidadImportar * precioImportar;
                break;

            }
        }


        precio.setText("");
        invertido.setText("");

        adapterRecyclerInversiones.agregarDatos(precioImportar,
                invertidoImportar, cantidadImportar);
        adapterRecyclerInversiones.monedaOrigen = monedaOrigen.getText().toString();
        adapterRecyclerInversiones.monedaDestino = monedaDestino.getText().toString();
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

        if (!invertido.getText().toString().isEmpty())
            agregarInversion();


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
        precioIn = String.format(precisionOrigenFormato, precioVariosImportar);
        inversionInicio = String.format(precisionOrigenFormato, invertidoVariosImportar);
        inversionDestinoInicio = String.format(precisionDestinoFormato,
                (invertidoVariosImportar / precioVariosImportar));
        guardarDB();

        mIntent.putExtra("idOperacion", idOperacion);
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


        ArrayList<EditText> listaDeRevisionArray;

        if (adapterRecyclerInversiones.lista.size() > 0) {
            EditText[] listaDeRevision = {monedaOrigen, monedaDestino};
            listaDeRevisionArray = new ArrayList<>(Arrays.asList(listaDeRevision));
        } else {
            EditText[] listaDeRevision = {monedaOrigen, monedaDestino,
                    precio, invertido};
            listaDeRevisionArray = new ArrayList<>(Arrays.asList(listaDeRevision));
        }


        if (botonAgregarInversionAplanado) {
            EditText[] listaDeRevision = {monedaOrigen, monedaDestino,
                    precio, invertido};
            listaDeRevisionArray = new ArrayList<>(Arrays.asList(listaDeRevision));
            botonAgregarInversionAplanado = false;
        }


        for (EditText texto : listaDeRevisionArray) {

            if (texto.getText().toString().isEmpty()) {

                crearSnackBar("Faltan datos por llenar!");
                return true;
            }
        }


        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
          /*  Intent i = getBaseContext().getPackageManager().
                    getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);*/
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

        if (checarSiFaltanDatos()) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    db.deleteFromRealm();
                }
            });


        }

        super.onDestroy();
    }
}


/*

InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
 */