/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Alex971
 */
public class VistaStatsViviendas extends VistaVivienda {

    private String runCliente;
    private String runVendedor;
    private String fecha;

    public VistaStatsViviendas() {
    }

    public VistaStatsViviendas(String runCliente, String runVendedor, String fecha) {
        this.runCliente = runCliente;
        this.runVendedor = runVendedor;
        this.fecha = fecha;
    }

    public VistaStatsViviendas(String runCliente, String runVendedor, String fecha, int nDeRol, String tipo, String disponibilidad, int precioDeArriendo, int precioDeVenta, int cantBanios, int cantPiezas, String direccion, String condicion) {
        super(nDeRol, tipo, disponibilidad, precioDeArriendo, precioDeVenta, cantBanios, cantPiezas, direccion, condicion);
        this.runCliente = runCliente;
        this.runVendedor = runVendedor;
        this.fecha = fecha;
    }

    public String getRunCliente() {
        return runCliente;
    }

    public void setRunCliente(String runCliente) {
        this.runCliente = runCliente;
    }

    public String getRunVendedor() {
        return runVendedor;
    }

    public void setRunVendedor(String runVendedor) {
        this.runVendedor = runVendedor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "VistaStatsViviendas{" + "runCliente=" + runCliente + ", runVendedor=" + runVendedor + ", fecha=" + fecha + '}';
    }
}