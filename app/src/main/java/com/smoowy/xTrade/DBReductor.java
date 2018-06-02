package com.smoowy.xTrade;

import io.realm.RealmObject;

public class DBReductor extends RealmObject {

    int id;
    String inversionRed;
    String precioRed;
    String precioBase;
    String textoGanadoRed;
    String textoGanandoLiqRed;
    String ganadoRed;
    String textoUsando;
    double inversionRedNumero;
    double ganadoRedNumero;
    double precioNumero;
    int tipo;
    double inversionR;
    double precioRedR;
    double precioBaseR;
    double ganadoRedR;
    double usandoR;
    double inversionDisponibleRedR;

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

    public double getInversionRedNumero() {
        return inversionRedNumero;
    }

    public void setInversionRedNumero(double inversionRedNumero) {
        this.inversionRedNumero = inversionRedNumero;
    }

    public double getGanadoRedNumero() {
        return ganadoRedNumero;
    }

    public void setGanadoRedNumero(double ganadoRedNumero) {
        this.ganadoRedNumero = ganadoRedNumero;
    }

    public double getPrecioNumero() {
        return precioNumero;
    }

    public void setPrecioNumero(double precioNumero) {
        this.precioNumero = precioNumero;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public double getInversionR() {
        return inversionR;
    }

    public void setInversionR(double inversionR) {
        this.inversionR = inversionR;
    }

    public double getPrecioRedR() {
        return precioRedR;
    }

    public void setPrecioRedR(double precioRedR) {
        this.precioRedR = precioRedR;
    }

    public double getPrecioBaseR() {
        return precioBaseR;
    }

    public void setPrecioBaseR(double precioBaseR) {
        this.precioBaseR = precioBaseR;
    }

    public double getGanadoRedR() {
        return ganadoRedR;
    }

    public void setGanadoRedR(double ganadoRedR) {
        this.ganadoRedR = ganadoRedR;
    }

    public double getUsandoR() {
        return usandoR;
    }

    public void setUsandoR(double usandoR) {
        this.usandoR = usandoR;
    }

    public double getInversionDisponibleRedR() {
        return inversionDisponibleRedR;
    }

    public void setInversionDisponibleRedR(double inversionDisponibleRedR) {
        this.inversionDisponibleRedR = inversionDisponibleRedR;
    }
}
