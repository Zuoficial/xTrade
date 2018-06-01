package com.smoowy.xTrade;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DBReductor extends RealmObject {

    int id;
    String inversionRed;
    String precioRed;
    String precioBase;
    String textoGanadoRed;
    String textoGanandoLiqRed;
    String ganadoRed;
    String textoUsando;
    Double inversionRedNumero;
    Double ganadoRedNumero;
    Double precioNumero;
    int tipo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInversionRed() {
        return inversionRed;
    }

    public void setInversionRed(String inversionRed) {
        this.inversionRed = inversionRed;
    }

    public String getPrecioRed() {
        return precioRed;
    }

    public void setPrecioRed(String precioRed) {
        this.precioRed = precioRed;
    }

    public String getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(String precioBase) {
        this.precioBase = precioBase;
    }

    public String getTextoGanadoRed() {
        return textoGanadoRed;
    }

    public void setTextoGanadoRed(String textoGanadoRed) {
        this.textoGanadoRed = textoGanadoRed;
    }

    public String getTextoGanandoLiqRed() {
        return textoGanandoLiqRed;
    }

    public void setTextoGanandoLiqRed(String textoGanandoLiqRed) {
        this.textoGanandoLiqRed = textoGanandoLiqRed;
    }

    public String getGanadoRed() {
        return ganadoRed;
    }

    public void setGanadoRed(String ganadoRed) {
        this.ganadoRed = ganadoRed;
    }

    public String getTextoUsando() {
        return textoUsando;
    }

    public void setTextoUsando(String textoUsando) {
        this.textoUsando = textoUsando;
    }

    public Double getInversionRedNumero() {
        return inversionRedNumero;
    }

    public void setInversionRedNumero(Double inversionRedNumero) {
        this.inversionRedNumero = inversionRedNumero;
    }

    public Double getGanadoRedNumero() {
        return ganadoRedNumero;
    }

    public void setGanadoRedNumero(Double ganadoRedNumero) {
        this.ganadoRedNumero = ganadoRedNumero;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Double getPrecioNumero() {
        return precioNumero;
    }

    public void setPrecioNumero(Double precioNumero) {
        this.precioNumero = precioNumero;
    }

}
