package com.smoowy.xTrade;

import io.realm.RealmObject;

public class DBOpInversiones extends RealmObject {

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getInversion() {
        return inversion;
    }

    public void setInversion(Double inversion) {
        this.inversion = inversion;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    Double inversion;
    Double cantidad;
    Double precio;

}
