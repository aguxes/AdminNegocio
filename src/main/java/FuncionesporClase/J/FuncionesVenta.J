#pragma once
#include <string>
#include <cstring>
#include <iostream>
#include <fstream>
#include <conio.h>

#include "ArchivoVentas.h"
#include "ArchivoEmpleados.h"
#include "ArchivoClientes.h"
#include "Listados.h"
#include "Venta.h"
#include "rlutil.h"
#include "Menu.h"
using namespace std;


ArchivoVentas::ArchivoVentas(string nombreArchivo){
    _nombreArchivo = nombreArchivo;
}

bool ArchivoVentas::Guardar(Venta venta){
    FILE *registro = fopen(_nombreArchivo.c_str(), "ab");
    if(registro == NULL){
        return false;
    }
    bool ok = fwrite(&venta, sizeof(Venta), 1, registro);
    fclose(registro);
    return ok;
}

bool ArchivoVentas::Guardar(Venta venta, int posicion){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb+");
    if(registro == NULL){
        return false;
    }
    fseek(registro, sizeof(Venta) * posicion, SEEK_SET);
    bool ok = fwrite(&venta, sizeof(Venta), 1, registro);
    fclose(registro);
    return ok;
}

Venta ArchivoVentas::Buscar(int idVenta){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Venta venta,fallo;
    fallo.setidVenta(-1);
    if(registro == NULL){
        return fallo;
    }
    while(fread(&venta, sizeof(venta), 1, registro)){
        if(venta.getIdVenta() == idVenta){
            fclose(registro);
            return venta;
        }
    }
    fclose(registro);
    return fallo;
}
Venta ArchivoVentas::Leer(int posicion){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if(registro == NULL){
        return Venta();
    }
    Venta venta;
    fseek(registro, sizeof(Venta) * posicion, SEEK_SET);
    fread(&venta, sizeof(Venta), 1, registro);
    fclose(registro);
    return venta;
}

int ArchivoVentas::CantidadRegistros(){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if(registro == NULL){
        return 0;
    }
    fseek(registro, 0, SEEK_END);
    int cantidadRegistros = ftell(registro) / sizeof(Venta);
    fclose(registro);
    return cantidadRegistros;
}


void ArchivoVentas::Leer(int cantidadRegistros, Venta *vector){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if(registro == NULL){
        return;
    }
    for(int i = 0; i < cantidadRegistros; i++){
        fread(&vector[i], sizeof(Venta), 1, registro);
    }
    fclose(registro);
}

int ArchivoVentas::BuscarPosRegistro(int idVenta){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Venta venta;
    if(registro == NULL){
        return -1;
    }
    int i = 0;
    while(fread(&venta, sizeof(venta), 1, registro) == 1){
        if(venta.getIdVenta() == idVenta){
            fclose(registro);
            return i;
        }
        i++;
    }
    fclose(registro);
    return -1;
}

void ArchivoVentas::ModificarVenta(int idVenta, int op) {
    Menu menu;
    ArchivoEmpleados Empleados("ArchivoEmpleados.dat");
    ArchivoClientes Clientes("ArchivoClientes.dat");
    int pos = BuscarPosRegistro(idVenta);
    if (pos == -1) {

         menu.mensajeDeError("Venta no encontrada.");
         cout  << endl;
        return;
        }
    Venta venta = Leer(pos);
    int idOriginal = venta.getIdVenta();

    switch (op)
    {
    case 1:
        {

        Fecha fecha;
        fecha.cargarFecha();
        venta.setFecha(fecha);
        }
        break;
    case 2:
        {

        int input;
        Listados listados;
        listados.listarEmpleadosAll();
        cout<< "Ingrese el Id Empleado: ";
        cin >> input;
        if(Empleados.Buscar(input).getID() != -1){
            venta.setidEmpleado(input);
        }
        }
        break;
    case 3:
        {
        Listados listados;
        listados.listarClientesAll();
        int input;
        cout<< "Ingrese el ID del Cliente: ";
        cin >> input;
        if(Clientes.Buscar(input).getID() != -1){
            venta.setidCliente(input);
        }

        }
        break;
    case 4:
        {
        int input = 0;
        Menu menu;
        while(!(input>0 && input<4)){
            cout<< "Ingrese el Metodo de pago:\n1.Efectivo\n2.Debito\n3.Credito\nOpcion: ";
            cin >> input;
            if(!(input>0 && input<4)){
            menu.mensajeDeError("Metodo de pago invalido");
            system("pause");
            }
        }
        venta.setFormaDePago(input);
        }
        break;
    default:
        break;
    }
    venta.setidVenta(idOriginal);
    if (Guardar(venta, pos)) {
            Menu::setColor(2);
        cout << "Datos de la venta actualizados." << endl;
            Menu::setColor(7);
    } else {

         menu.mensajeDeError("Error al actualizar los datos de la venta." );
         cout<< endl;
    }
}

void ArchivoVentas::eliminarArchivoVentas()
{
    Menu menu;
    menu.setColor(7);
    int id, aux;
    ifstream salida;
    salida.open("ArchivoVentas.dat", ios::in);
    ofstream entrada;
    entrada.open("temp.dat", ios::out);


    if (salida.fail())
    {
        menu.mensajeDeError("Hubo un error al abrir el archivo ArchivoVentas.dat");
       cout << endl;
        cin.get();
        exit(0);
    }
    else
    {

        salida.close();
        entrada.close();

        remove("ArchivoVentas.dat");  // Eliminar el archivo original

        rename("temp.dat", "ArchivoVentas.dat");  // Renombrar el archivo temporal a "ArchivoVentas.dat"
        Menu::setColor(2);
        cout<<"Registros del archivo Ventas eliminados"<<endl;
        Menu::setColor(7);
    }

    salida.open("ArchivoDetalleVentas.dat", ios::in);
    entrada.open("temp1.dat", ios::out);

        if (salida.fail())
    {
        menu.mensajeDeError("Hubo un error al abrir el archivo ArchivoVentas.dat");
       cout << endl;
        cin.get();
        exit(0);
    }
    else
    {

        salida.close();
        entrada.close();

        remove("ArchivoDetalleVentas.dat");

        rename("temp1.dat", "ArchivoDetalleVentas.dat");
        Menu::setColor(2);
        cout<<"Registros del archivo Detalle de Ventas eliminados"<<endl;
        Menu::setColor(7);
    }
}
void ArchivoVentas::eliminarRegistroVenta(int ventaID)
{
    Menu menu;
    int idVenta;
    FILE* archivoOriginal = fopen(_nombreArchivo.c_str(), "rb");
    if (archivoOriginal == nullptr)
    {
         menu.mensajeDeError("Error al abrir el archivo para lectura." );
        cout<< endl;
        return;
    }

    FILE* archivoTemporal = fopen("ventas_temp.dat", "wb");
    if (archivoTemporal == nullptr)
    {
         menu.mensajeDeError("Error al crear archivo temporal.");
         cout << endl;
        fclose(archivoOriginal);
        return;
    }

    Venta venta;
    bool encontrado = false;


    while (fread(&venta, sizeof(Venta), 1, archivoOriginal))
    {
        if (venta.getIdVenta() != ventaID)
        {

            fwrite(&venta, sizeof(Venta), 1, archivoTemporal);
        }
        else
        {
            idVenta = venta.getIdVenta();
            encontrado = true;
        }
    }

    fclose(archivoOriginal);
    fclose(archivoTemporal);


    if (encontrado)
    {
        remove(_nombreArchivo.c_str());
        rename("ventas_temp.dat", _nombreArchivo.c_str());
         Menu::setColor(7);
        cout << "Venta con ID " ;
         Menu::setColor(0);
        cout<< idVenta ;
         Menu::setColor(7);
        cout<<" eliminado correctamente." << endl;
    }
    else
    {

        remove("ventas_temp.dat");
         Menu::setColor(7);
        cout << "Venta con ID ";
         Menu::setColor(0);
        cout << idVenta;
         Menu::setColor(7);
        cout<< " no encontrado." << endl;
    }
}


void ArchivoVentas::exportarVentasACSV(string nombreArchivoCSV) {
        Menu menu;
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if (registro == nullptr)
    {
        menu.mensajeDeError("Error al abrir el archivo para lectura.");
        return;
    }

    ofstream archivoCSV(nombreArchivoCSV);
    if (!archivoCSV.is_open())
    {
        menu.mensajeDeError("Error al crear el archivo CSV.");
        fclose(registro);
        return;
    }

    int cont = 0;
    Venta venta;
    ArchivoEmpleados Empleados("ArchivoEmpleados.dat");
    ArchivoClientes Clientes("ArchivoClientes.dat");

        archivoCSV << "     -------------Datos de Ventas----------------"<<endl;
    while (fread(&venta, sizeof(Venta), 1, registro))
    {
        Menu::setColor(7);
        archivoCSV << "FECHA: " << venta.getFecha().getDia()<<"/"<<venta.getFecha().getMes()<<"/"<<venta.getFecha().getAnio()<<endl;
        archivoCSV << "ID VENTA: " << venta.getIdVenta() <<endl;
        archivoCSV << "EMPLEADO: " << Empleados.Buscar(venta.getIdVenta()).getNombre() <<endl;
        archivoCSV << "EMPLEADO ID: " << Empleados.Buscar(venta.getIdVenta()).getID() <<endl;
        archivoCSV << "CLIENTE: " << Clientes.Buscar(venta.getIdVenta()).getNombre() <<endl;
        archivoCSV << "CLIENTE ID: " << Clientes.Buscar(venta.getIdVenta()).getID() <<endl;
        archivoCSV << "FORMA DE PAGO: " << venta.getFormaDePago() << endl;
        archivoCSV << "IMPORTE TOTAL: " << venta.getImporteTotal() << endl;
        archivoCSV << "ESTADO: " << venta.getEstado() << endl;
        archivoCSV << endl;
        archivoCSV << "-----------------------------------------------------" << endl;
        archivoCSV << endl;



        cout << "FECHA: " << venta.getFecha().getDia()<<"/"<<venta.getFecha().getMes()<<"/"<<venta.getFecha().getAnio()<<endl;
        cout << "ID:"<< venta.getIdVenta()<< endl;
        cout << "EMPLEADO: " << Empleados.Buscar(venta.getIdVenta()).getNombre() <<endl;
        cout << "EMPLEADO ID: " << Empleados.Buscar(venta.getIdVenta()).getID() <<endl;
        cout << "CLIENTE: " << Clientes.Buscar(venta.getIdVenta()).getNombre() <<endl;
        cout << "CLIENTE ID: " << Clientes.Buscar(venta.getIdVenta()).getID() <<endl;
        cout << "FECHA DE INGRESO: " << venta.getFecha().getDia()<<"/"<<venta.getFecha().getMes()<<"/"<<venta.getFecha().getAnio() <<endl;
        cout <<"FORMA DE PAGO:" << venta.getFormaDePago() << endl;
        cout <<"IMPORTE TOTAL:" << venta.getImporteTotal() << endl;
        cout <<"ESTADO:" << venta.getEstado() << endl;
        cout << endl;
        cout << "-----------------------------------------------------" << endl;
        cout << endl;
        cont++;
    }
    fclose(registro);
    archivoCSV.close();

    cout << "Se exportaron " << cont << " Ventas al archivo CSV." << endl;
}
