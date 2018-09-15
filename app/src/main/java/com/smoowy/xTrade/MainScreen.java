package com.smoowy.xTrade;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainScreen extends AppCompatActivity implements ComunicadorPosiciones{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setTheme(R.style.AppTheme_NoActionBar);
        if (getIntent().hasExtra("salida")) {
            finish();
        }

        ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(null, null, getColor(R.color.colorPrimary));
        setTaskDescription(taskDesc);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> crearOperacion());
        fab.setOnLongClickListener(view -> {
            vibrator.vibrate(500);
            adapterRecyclerPosiciones.borrarDatos();
            Snackbar snackbar =
                    Snackbar.make(findViewById(R.id.fondo), "Se ha borrado todo!", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
            snackbar.setAction("Regresar", viewX -> {
                vibrator.vibrate(500);
                adapterRecyclerPosiciones.recuperarDatos();
            }).show();
            return true;
        });
        accederDB();
        setRecyclerViewRecyclerBotonesPorcentajes();

    }

    DB db;
    int id;
    Vibrator vibrator;

    void crearOperacion() {


        vibrator.vibrate(500);

        if (resultadosRealm.size() == 0) {
            id = 0;

            realm.executeTransaction(realm -> db = realm.createObject(DB.class, id));

        } else {


            id = resultadosRealm.max("id").intValue();
            id += 1;

                realm.executeTransaction(realm -> db = realm.createObject(DB.class, id));


        }


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idOperacion", id);
        startActivity(intent);
        borrarPosicionesDB();
    }


    LinearLayoutManager layoutManagerPosiciones;
    RecyclerView recyclerPosiciones;
    AdapterRecyclerPosiciones adapterRecyclerPosiciones;
    TextView textoSinDatos;

    private void setRecyclerViewRecyclerBotonesPorcentajes() {
        textoSinDatos = findViewById(R.id.textoSinDatos);
        if (resultadosRealm.size() == 0)
            textoSinDatos.setVisibility(View.VISIBLE);
        recyclerPosiciones = findViewById(R.id.recyclerPosiciones);
        adapterRecyclerPosiciones = new AdapterRecyclerPosiciones(this, listaDatos);
        layoutManagerPosiciones = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerPosiciones.setAdapter(adapterRecyclerPosiciones);
        recyclerPosiciones.setLayoutManager(layoutManagerPosiciones);
        // layoutManagerPosiciones.scrollToPosition(2);

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


                adapterRecyclerPosiciones.quitarDatos(viewHolder.getLayoutPosition());


                Snackbar snackbar =
                        Snackbar.make(findViewById(R.id.fondo), "Inversion borrada!", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getColor(R.color.colorBotonForexClaro));
                snackbar.setAction("Regresar", view -> {
                    vibrator.vibrate(500);
                    adapterRecyclerPosiciones.recuperarDatos();

                }).show();
            }


            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerPosiciones);

        adapterRecyclerPosiciones.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (adapterRecyclerPosiciones.listaDatos.size() == 0) {
                    textoSinDatos.setVisibility(View.VISIBLE);
                    recyclerPosiciones.setVisibility(View.GONE);

                } else {
                    textoSinDatos.setVisibility(View.GONE);
                    recyclerPosiciones.setVisibility(View.VISIBLE);
                }
                super.onChanged();

            }


            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (adapterRecyclerPosiciones.listaDatos.size() == 0) {
                    textoSinDatos.setVisibility(View.VISIBLE);
                    recyclerPosiciones.setVisibility(View.GONE);

                } else {
                    textoSinDatos.setVisibility(View.GONE);
                    recyclerPosiciones.setVisibility(View.VISIBLE);
                }
                super.onItemRangeInserted(positionStart, itemCount);
            }

        });

    }


    Realm realm;
    RealmResults<DB> resultadosRealm;
    ArrayList<DB> listaDatos;

    public void accederDB() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        resultadosRealm = realm.where(DB.class).findAll();

        for (final DB db : resultadosRealm) {

            if (db.getPrecioIn() == null) {


                realm.executeTransaction(realm -> db.deleteFromRealm());
            }
        }
        resultadosRealm = realm.where(DB.class).findAll();
        listaDatos = new ArrayList<>(resultadosRealm);

    }

    void borrarPosicionesDB() {
        realm.executeTransaction(realm -> {

            for (DB db : adapterRecyclerPosiciones.listaParaBorrar) {
                db.deleteFromRealm();
            }
        });

    }

    @Override
    protected void onDestroy() {

        if (!realm.isClosed())
            realm.close();


        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        borrarPosicionesDB();
        super.onBackPressed();
    }

    @Override
    public void borrarDesdeRecycler() {
        borrarPosicionesDB();
    }
}
