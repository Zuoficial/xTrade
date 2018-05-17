package com.smoowy.xTrade;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setTheme(R.style.AppTheme_NoActionBar);
        ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(null, null, getColor(R.color.colorPrimary));
        setTaskDescription(taskDesc);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearOperacion();
            }
        });
        accederDB();
        setRecyclerViewRecyclerBotonesPorcentajes();

    }

    DB db;
    int id;

    void crearOperacion() {

        if (resultadosRealm.size() == 0) {
            id = 0;

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    db = realm.createObject(DB.class, id);
                }
            });

        } else {

            id = resultadosRealm.get(resultadosRealm.size() - 1).getId();
            id += 1;

            DB check = realm.where(DB.class).equalTo("id", id).findFirst();

            if (check == null) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        db = realm.createObject(DB.class, id);
                    }
                });
            } else {
                id += 10;

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        db = realm.createObject(DB.class, id);
                    }
                });
            }


        }


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idOperacion", id);
        startActivity(intent);

    }


    LinearLayoutManager layoutManagerPosiciones;
    RecyclerView recyclerPosiciones;
    AdapterRecyclerPosiciones adapterRecyclerPosiciones;

    private void setRecyclerViewRecyclerBotonesPorcentajes() {
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


                Snackbar.make(findViewById(R.id.fondo), "Inversion borrada!", Snackbar.LENGTH_LONG)
                        .setAction("Regresar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapterRecyclerPosiciones.recuperarDatos();
                            }
                        }).show();


            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerPosiciones);


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
        listaDatos = new ArrayList<>(resultadosRealm);

    }

    @Override
    protected void onDestroy() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (DB db : adapterRecyclerPosiciones.listaParaBorrar) {

                    db.deleteFromRealm();

                }


            }
        });
        realm.close();

        super.onDestroy();
    }
}
