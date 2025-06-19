#pragma once
#include <string>
#include <cstring>
#include <iostream>
#include <fstream>
#include <conio.h>

#include "ArchivoProductos.h"
#include "Producto.h"
#include "rlutil.h"
#include "Menu.h"
using namespace std;


ArchivoProductos::ArchivoProductos(string nombreArchivo){
    _nombreArchivo = nombreArchivo;
}

bool ArchivoProductos::Guardar(Producto producto){
    FILE *registro = fopen(_nombreArchivo.c_str(), "ab");
    if(registro == NULL){
        return false;
    }
    bool ok = fwrite(&producto, sizeof(Producto), 1, registro);
    fclose(registro);
    return ok;
}

bool ArchivoProductos::Guardar(Producto producto, int posicion){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb+");
    if(registro == NULL){
        return false;
    }
    fseek(registro, sizeof(Producto) * posicion, SEEK_SET);
    bool ok = fwrite(&producto, sizeof(Producto), 1, registro);
    fclose(registro);
    return ok;
}

Producto ArchivoProductos::Buscar(int productoID){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Producto producto, fallo;
    fallo.setProductoID(-1);
    if(registro == NULL){
        return fallo;
    }
    while(fread(&producto, sizeof(producto), 1, registro)){
        if(producto.getID() == productoID){
            fclose(registro);
            return producto;
        }
    }
    fclose(registro);
    return fallo;
}

void ArchivoProductos::FiltrarProductos()
{
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Producto producto;
    Menu menu;
    int cont = 0;

    if (registro == nullptr)
    {
        menu.mensajeDeError("No se encontraron productos.");
        return;
    }
    while (fread(&producto, sizeof(producto), 1, registro))
    {
        cont++;
        producto.mostrarProducto();
    }

    if (cont == 0)
    {
        menu.mensajeDeError("No se encontraron productos en el archivo.");
    }

    fclose(registro);
}
void ArchivoProductos::FiltrarPorNombre(string _nombre){

FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Producto producto;
    Menu menu;
    int cont = 0;
    if (registro == nullptr) {
        menu.mensajeDeError("No se encontraron productos." );
        return;
    }
    while(fread(&producto, sizeof(producto), 1, registro))
    {
        if(producto.getNombre() == _nombre){
            fclose(registro);
            producto.mostrarProducto();
            cont++;
        }
    }
    if(cont == 0){
        menu.mensajeDeError("No se encontraron productos con el nombre: ");
        cout << _nombre<<endl;
    }
    fclose(registro);

}

void ArchivoProductos::FiltrarPorID(int _productoID)
{
FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Producto producto;
    Menu menu;
    int cont = 0;
    if (registro == nullptr) {
        menu.mensajeDeError("No se encontraron productos." );
        return;
    }
    while(fread(&producto, sizeof(producto), 1, registro)){
        if(producto.getID() == _productoID)
        {
            producto.mostrarProducto();
            cont++;
        }
    }
    if(cont == 0){
        menu.mensajeDeError("No se encontraron productos con el ID: ");
        cout<< _productoID<<endl;
    }
    fclose(registro);
}

void ArchivoProductos::FiltrarPorCategoria (string _categoria)
{
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Producto producto;
    Menu menu;
    int cont = 0;
    if (registro == nullptr) {
        menu.mensajeDeError("No se encontraron categorias de productos" );
        return;
    }
    while(fread(&producto, sizeof(producto), 1, registro)){
        if(producto.getCategoriaProducto() == _categoria)
        {
            producto.mostrarProducto();
            cont++;
        }
    }
    if(cont == 0){
        menu.mensajeDeError("No se encontro esa categoria. ");
    }
    fclose(registro);
}

int ArchivoProductos::BuscarPos(int productoID)
{
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    Producto producto;
    int posicion = 0;

    if (registro == NULL)
    {
        return -1;
    }

    while (fread(&producto, sizeof(producto), 1, registro))
    {
        if (producto.getID() == productoID)
        {
            fclose(registro);
            return posicion;
        }
        posicion++;
    }
    fclose(registro);
    return -1;
}


Producto ArchivoProductos::Leer(int posicion){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if(registro == NULL){
        return Producto();
    }
    Producto producto;
    fseek(registro, sizeof(Producto) * posicion, SEEK_SET);
    fread(&producto, sizeof(Producto), 1, registro);
    fclose(registro);
    return producto;
}

int ArchivoProductos::CantidadRegistros(){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if(registro == NULL){
        return 0;
    }
    fseek(registro, 0, SEEK_END);
    int cantidadRegistros = ftell(registro) / sizeof(Producto);
    fclose(registro);
    return cantidadRegistros;
}


void ArchivoProductos::Leer(int cantidadRegistros, Producto *vector){
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if(registro == NULL){
        return;
    }
    for(int i = 0; i < cantidadRegistros; i++){
        fread(&vector[i], sizeof(Producto), 1, registro);
    }
    fclose(registro);
}

void ArchivoProductos::ModificarProducto(int productoID) {
    int pos = BuscarPos(productoID);
    if (pos == -1) {
            Menu::setColor(4);
        cout << "Producto no encontrado." << endl;
        return;
        }
    Producto producto = Leer(pos);
    int idOriginal = producto.getID();
    producto.cargarProducto();
    producto.setProductoID(idOriginal);
    if (Guardar(producto, pos)) {
            Menu::setColor(7);
        cout << "Datos del producto actualizados." << endl;
    } else {
        Menu::setColor(4);
        cout << "Error al actualizar los datos del producto." << endl;
    }
}

void ArchivoProductos::eliminarArchivoProductos()
{
    Menu menu;
    menu.setColor(7);
    string nombre, aux;
    ifstream salida;
    salida.open("ArchivoProductos.dat", ios::in);
    ofstream entrada;
    entrada.open("temp.dat", ios::out);

    if (salida.fail())
    {
        cout << "Hubo un error al abrir el archivo ArchivoProductos.dat" << endl;
        cin.get();
        exit(0);
    }
    else
    {
        cout << "Introduzca el nombre: ";
        cin >> aux;

        while (salida >> nombre)
        {
            if (aux == nombre)
            {
                cout << "El registro ha sido eliminado." << endl;
            }
            else
            {
                entrada << nombre << " " << endl;
            }
        }

        salida.close();
        entrada.close();

        remove("ArchivoProductos.dat");  // Eliminar el archivo original
        rename("temp.dat", "ArchivoProductos.dat");  // Renombrar el archivo temporal a "ArchivoEmpleados.dat"
    }
}
void ArchivoProductos::eliminarRegistroProducto(int productoID)
{
    string nombreProducto;
    FILE* archivoOriginal = fopen(_nombreArchivo.c_str(), "rb");
    if (archivoOriginal == nullptr)
    {
        cout << "Error al abrir el archivo para lectura." << endl;
        return;
    }

    FILE* archivoTemporal = fopen("productos_temp.dat", "wb");
    if (archivoTemporal == nullptr)
    {
        cout << "Error al crear archivo temporal." << endl;
        fclose(archivoOriginal);
        return;
    }

    Producto producto;
    bool encontrado = false;


    while (fread(&producto, sizeof(Producto), 1, archivoOriginal))
    {
        if (producto.getID() != productoID)
        {

            fwrite(&producto, sizeof(Producto), 1, archivoTemporal);
        }
        else
        {
            nombreProducto = producto.getNombre();
            encontrado = true;
        }
    }


    fclose(archivoOriginal);
    fclose(archivoTemporal);


    if (encontrado)
    {
        remove(_nombreArchivo.c_str());
        rename("productos_temp.dat", _nombreArchivo.c_str());
        cout << "Producto con ID " << productoID <<" y con nombre " << nombreProducto << " eliminado correctamente." << endl;
    }
    else
    {

        remove("productos_temp.dat");
        cout << "Producto con ID " << productoID << " no encontrado." << endl;
    }
}


void ArchivoProductos::exportarProductosACSV(string nombreArchivoCSV)
{
    FILE *registro = fopen(_nombreArchivo.c_str(), "rb");
    if (registro == nullptr)
    {
        Menu menu;
        menu.mensajeDeError("Error al abrir el archivo para lectura.");
        return;
    }

    ofstream archivoCSV(nombreArchivoCSV);
    if (!archivoCSV.is_open())
    {
        Menu menu;
        menu.mensajeDeError("Error al crear el archivo CSV.");
        fclose(registro);
        return;
    }

    int cont = 0;
    Producto producto;
//  Leer productos desde el archivo binario y escribirlos en el archivo CSV
    while (fread(&producto, sizeof(Producto), 1, registro))
    {
        Menu::setColor(7);
//      Escribir los datos de cada producto en el archivo CSV:
        archivoCSV << "ID:" << producto.getID() <<endl;
        archivoCSV << "NOMBRE: " <<producto.getNombre() <<endl;
        archivoCSV << "CATEGORIA: " <<producto.getCategoriaProducto() <<endl;
        archivoCSV << "PRECIO UNITARIO: " <<producto.getPrecioUnitario() <<endl;
        archivoCSV << "STOCK: " <<producto.getStock() << endl;
        archivoCSV << endl;
        archivoCSV << "-----------------------------------------------------" << endl;
        archivoCSV << endl;

        cout << "ID:"<< producto.getID()<< endl;
        cout <<"NOMBRE:"<< producto.getNombre()<< endl;
        cout <<"CATEGORIA:"<< producto.getCategoriaProducto()<< endl;
        cout <<"PRECIO UNITARIO:"<< producto.getPrecioUnitario()<< endl;
        cout <<"STOCK:" << producto.getStock() << endl;
        cout << endl;
        cout << "-----------------------------------------------------" << endl;
        cout << endl;
        cont++;
    }
    fclose(registro);
    archivoCSV.close();

    cout << "Se exportaron " << cont << " productos al archivo CSV." << endl;
}
