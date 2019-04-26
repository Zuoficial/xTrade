package com.smoowy.xTrade;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DB extends RealmObject {


    @PrimaryKey
    int id;
    String monedaOrigen;
    String monedaDestino;
    String invertido;
    String precio;
    String comisionEntrada;
    String comisionSalida;
    String precisionOrigen;
    String precisionDestino;
    String precisionLiquidez;
    String precisionPrecio;
    String liquidezOrigen;
    String liquidezDestino;
    String liquidezNombre;
    String precioIn;
    String precioOut;
    String inversionInicio;
    String inversionInicioFinal;
    String ganadoInicio;
    String actualInicio;
    String inversionLiqInicio;
    String ganadoLiqInicio;
    String actualLiqInicio;
    String precisionOrigenFormato;
    String precisionDestinoFormato;
    String precisionLiquidezFormato;
    String precisionPrecioFormato;
    String usandoInicio;
    String usando;
    String referencia;
    String precioContrato;
    String cantidadContrato;
    Boolean botonPorcentajesAplanado;
    Boolean enForex;
    Boolean botonComisionAplanado;
    Boolean existeReduccion;
    Boolean comisionEntradaNegativa;
    Boolean comisionSalidaNegativa;
    Boolean botonLotesAplanado;
    Boolean hayContrato;
    RealmList<DBOpInversiones> operaciones;
    RealmList<DBReductor> reducciones;
    int modo;
    int modoLiquidez;
    double ganadoFinal;
    double invertidoRedFinal;
    double gananciaRedFinal;


    public Boolean getExisteReduccion() {
        return existeReduccion;
    }

    public void setExisteReduccion(Boolean existeReduccion) {
        this.existeReduccion = existeReduccion;
    }

    public double getInvertidoRedFinal() {
        return invertidoRedFinal;
    }

    public void setInvertidoRedFinal(double invertidoRedFinal) {
        this.invertidoRedFinal = invertidoRedFinal;
    }


    public double getGananciaRedFinal() {
        return gananciaRedFinal;
    }

    public void setGananciaRedFinal(double gananciaRedFinal) {
        this.gananciaRedFinal = gananciaRedFinal;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public String getInvertido() {
        return invertido;
    }

    public void setInvertido(String invertido) {
        this.invertido = invertido;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getComisionEntrada() {
        return comisionEntrada;
    }

    public void setComisionEntrada(String comisionEntrada) {
        this.comisionEntrada = comisionEntrada;
    }

    public String getComisionSalida() {
        return comisionSalida;
    }

    public void setComisionSalida(String comisionSalida) {
        this.comisionSalida = comisionSalida;
    }

    public String getPrecisionOrigen() {
        return precisionOrigen;
    }

    public void setPrecisionOrigen(String precisionOrigen) {
        this.precisionOrigen = precisionOrigen;
    }

    public String getPrecisionDestino() {
        return precisionDestino;
    }

    public void setPrecisionDestino(String precisionDestino) {
        this.precisionDestino = precisionDestino;
    }

    public String getPrecisionLiquidez() {
        return precisionLiquidez;
    }

    public void setPrecisionLiquidez(String precisionLiquidez) {
        this.precisionLiquidez = precisionLiquidez;
    }

    public String getPrecisionPrecio() {
        return precisionPrecio;
    }

    public void setPrecisionPrecio(String precisionPrecio) {
        this.precisionPrecio = precisionPrecio;
    }

    public String getLiquidezOrigen() {
        return liquidezOrigen;
    }

    public void setLiquidezOrigen(String liquidezOrigen) {
        this.liquidezOrigen = liquidezOrigen;
    }

    public String getLiquidezDestino() {
        return liquidezDestino;
    }

    public void setLiquidezDestino(String liquidezDestino) {
        this.liquidezDestino = liquidezDestino;
    }

    public String getLiquidezNombre() {
        return liquidezNombre;
    }

    public void setLiquidezNombre(String liquidezNombre) {
        this.liquidezNombre = liquidezNombre;
    }

    public String getPrecioIn() {
        return precioIn;
    }

    public void setPrecioIn(String precioIn) {
        this.precioIn = precioIn;
    }

    public String getPrecioOut() {
        return precioOut;
    }

    public void setPrecioOut(String precioOut) {
        this.precioOut = precioOut;
    }

    public String getInversionInicio() {
        return inversionInicio;
    }

    public void setInversionInicio(String inversionInicio) {
        this.inversionInicio = inversionInicio;
    }

    public String getInversionInicioFinal() {
        return inversionInicioFinal;
    }

    public void setInversionInicioFinal(String inversionInicioFinal) {
        this.inversionInicioFinal = inversionInicioFinal;
    }

    public String getGanadoInicio() {
        return ganadoInicio;
    }

    public void setGanadoInicio(String ganadoInicio) {
        this.ganadoInicio = ganadoInicio;
    }

    public String getActualInicio() {
        return actualInicio;
    }

    public void setActualInicio(String actualInicio) {
        this.actualInicio = actualInicio;
    }

    public String getInversionLiqInicio() {
        return inversionLiqInicio;
    }

    public void setInversionLiqInicio(String inversionLiqInicio) {
        this.inversionLiqInicio = inversionLiqInicio;
    }

    public String getGanadoLiqInicio() {
        return ganadoLiqInicio;
    }

    public void setGanadoLiqInicio(String ganadoLiqInicio) {
        this.ganadoLiqInicio = ganadoLiqInicio;
    }

    public String getActualLiqInicio() {
        return actualLiqInicio;
    }

    public void setActualLiqInicio(String actualLiqInicio) {
        this.actualLiqInicio = actualLiqInicio;
    }

    public String getPrecisionOrigenFormato() {
        return precisionOrigenFormato;
    }

    public void setPrecisionOrigenFormato(String precisionOrigenFormato) {
        this.precisionOrigenFormato = precisionOrigenFormato;
    }

    public String getPrecisionDestinoFormato() {
        return precisionDestinoFormato;
    }

    public void setPrecisionDestinoFormato(String precisionDestinoFormato) {
        this.precisionDestinoFormato = precisionDestinoFormato;
    }

    public String getPrecisionLiquidezFormato() {
        return precisionLiquidezFormato;
    }

    public void setPrecisionLiquidezFormato(String precisionLiquidezFormato) {
        this.precisionLiquidezFormato = precisionLiquidezFormato;
    }

    public String getPrecisionPrecioFormato() {
        return precisionPrecioFormato;
    }

    public void setPrecisionPrecioFormato(String precisionPrecioFormato) {
        this.precisionPrecioFormato = precisionPrecioFormato;
    }

    public String getUsando() {
        return usando;
    }

    public void setUsando(String usando) {
        this.usando = usando;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Boolean getBotonPorcentajesAplanado() {
        return botonPorcentajesAplanado;
    }

    public void setBotonPorcentajesAplanado(Boolean botonPorcentajesAplanado) {
        this.botonPorcentajesAplanado = botonPorcentajesAplanado;
    }

    public Boolean getEnForex() {
        return enForex;
    }

    public void setEnForex(Boolean enForex) {
        this.enForex = enForex;
    }

    public Boolean getBotonComisionAplanado() {
        return botonComisionAplanado;
    }

    public void setBotonComisionAplanado(Boolean botonComisionAplanado) {
        this.botonComisionAplanado = botonComisionAplanado;
    }

    public RealmList<DBOpInversiones> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(RealmList<DBOpInversiones> operaciones) {
        this.operaciones = operaciones;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public int getModoLiquidez() {
        return modoLiquidez;
    }

    public void setModoLiquidez(int modoLiquidez) {
        this.modoLiquidez = modoLiquidez;
    }

    public double getGanadoFinal() {
        return ganadoFinal;
    }

    public void setGanadoFinal(double ganadoFinal) {
        this.ganadoFinal = ganadoFinal;
    }

    public String getUsandoInicio() {
        return usandoInicio;
    }

    public void setUsandoInicio(String usandoInicio) {
        this.usandoInicio = usandoInicio;
    }

    public Boolean getComisionEntradaNegativa() {
        return comisionEntradaNegativa;
    }

    public void setComisionEntradaNegativa(Boolean comisionEntradaNegativa) {
        this.comisionEntradaNegativa = comisionEntradaNegativa;
    }

    public Boolean getComisionSalidaNegativa() {
        return comisionSalidaNegativa;
    }

    public void setComisionSalidaNegativa(Boolean comisionSalidaNegativa) {
        this.comisionSalidaNegativa = comisionSalidaNegativa;
    }

    public Boolean getBotonLotesAplanado() {
        return botonLotesAplanado;
    }

    public void setBotonLotesAplanado(Boolean botonLotesAplanado) {
        this.botonLotesAplanado = botonLotesAplanado;
    }

    public Boolean getHayContrato() {
        return hayContrato;
    }

    public void setHayContrato(Boolean hayContrato) {
        this.hayContrato = hayContrato;
    }

    public String getPrecioContrato() {
        return precioContrato;
    }

    public void setPrecioContrato(String precioContrato) {
        this.precioContrato = precioContrato;
    }

    public String getCantidadContrato() {
        return cantidadContrato;
    }

    public void setCantidadContrato(String cantidadContrato) {
        this.cantidadContrato = cantidadContrato;
    }

}