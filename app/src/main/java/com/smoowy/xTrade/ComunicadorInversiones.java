package com.smoowy.xTrade;

public interface ComunicadorInversiones {

    void recuperarDatosRecycler(String inversion,String precio);
    void borrarInversionRecycler(Integer posicion);
    void recuperarInformacionContrato(String precioContrato, String cantidadContrato);
}
