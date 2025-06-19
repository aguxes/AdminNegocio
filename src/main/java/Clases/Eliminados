#include <iostream>
#include <vector>
#include <cstdio>
#include <cstring>

#include "ArchivoClientes.h"
#include "ArchivoEmpleados.h"
#include "ArchivoVentas.h"
#include "ArchivoProductos.h"
#include "ArchivoDetalleVentas.h"

#include "Menu.h"
#include "Listados.h"
#include "Eliminados.h"

#include "Cliente.h"
#include "Empleado.h"
#include "Venta.h"
#include "Producto.h"
#include "rlutil.h"

using namespace std;

void Eliminados::menuEliminarEmpleados()
{
    Menu menu;
    Eliminados eliminados;
    int op=0;
    bool opcionValida=false;

    do
    {
        menu.setColor(7);
        cout<<"----------------------------------"<<endl;
        cout<<"Elija la opcion que desee realizar"<<endl;
        cout<<"1. Eliminar un registro"<<endl;
        cout<<"2. Eliminar archivo"<<endl;
        cout<<"0. Salir"<<endl;
        cout<<"==================================="<<endl;
        menu.setColor(0);
        cin>>op;
        system("cls");
        if(op> (0) && op<4)
        {
            opcionValida=true;
        }

        switch (op)
        {
        case 1:
            eliminados.eliminarRegistroEmpleado();
            break;
        case 2:
            eliminados.eliminarArchivoEmpleados();
            break;
        case 0:
            menu.getMainMenu();
            break;
        default:

            menu.mensajeDeError("Opcion invalida!, vuelva a intentarlo" );

        }
        system("pause");
        system("cls");

        opcionValida = false;
    }
    while(!opcionValida);
}

void Eliminados::eliminarArchivoEmpleados()
{
    ArchivoEmpleados Empleados("ArchivoEmpleados.dat");
    Empleado empleado;

    Empleados.eliminarArchivoEmpleados();
    system("pause");
    system("cls");
    menuEliminarEmpleados();
}
void Eliminados::eliminarRegistroEmpleado()
{
    Menu menu;
    ArchivoEmpleados Empleados("ArchivoEmpleados.dat");
    Empleado empleado;

    int empleadoID;
    menu.setColor(7);
    cout << "Ingrese el ID del empleado que desea eliminar: ";
    menu.setColor(0);
    cin>>empleadoID;

    Empleados.eliminarRegistroEmpleado(empleadoID);
    system("pause");
    system("cls");
    menuEliminarEmpleados();
}

void Eliminados::menuEliminarClientes()
{
    Menu menu;
    int op=0;
    bool opcionValida=false;

    do
    {
        menu.setColor(7);
        cout<<"----------------------------------"<<endl;
        cout<<"Elija la opcion que desee realizar"<<endl;
        cout<<"1. Eliminar un registro"<<endl;
        cout<<"2. Eliminar archivo"<<endl;
        cout<<"0. Salir"<<endl;
        cout<<"==================================="<<endl;
        menu.setColor(0);
        cin>>op;
        system("cls");
        if(op> (0) && op<4)
        {
            opcionValida=true;
        }

        switch (op)
        {
        case 1:
            eliminarRegistroCliente();
            break;
        case 2:
            eliminarArchivoClientes();
            break;
        case 0:
            menu.getMainMenu();
            break;
        default:

            menu.mensajeDeError("Opcion invalida!, vuelva a intentarlo" );

        }
        system("pause");
        system("cls");

        opcionValida = false;
    }
    while(!opcionValida);

}
void Eliminados::eliminarArchivoClientes()
{
    ArchivoClientes Clientes("ArchivoClientes.dat");
    Cliente cliente;

    Clientes.eliminarArchivoClientes();
    system("pause");
    system("cls");
    menuEliminarClientes();
}
void Eliminados::eliminarRegistroCliente()
{
    ArchivoClientes Clientes("ArchivoClientes.dat");
    Cliente cliente;
    Menu menu;

    int clienteID;
    menu.setColor(7);
    cout << "Ingrese el ID del cliente que desea eliminar: ";
    menu.setColor(0);
    cin>>clienteID;

    Clientes.eliminarRegistroCliente(clienteID);
    system("pause");
    system("cls");
    menuEliminarClientes();
}
void Eliminados::eliminarArchivoVentas()
{
    ArchivoVentas Ventas("ArchivoVentas.dat");

    Ventas.eliminarArchivoVentas();
    system("pause");
    system("cls");
    menuEliminarVentas();
}
void Eliminados::eliminarRegistroVenta()
{
    ArchivoVentas Ventas("ArchivoVentas.dat");
    Menu menu;

    int ventaID;
    menu.setColor(7);
    cout << "Ingrese el ID de la venta que desea eliminar: ";
    menu.setColor(0);
    cin>>ventaID;

    Ventas.eliminarRegistroVenta(ventaID);
    system("pause");
    system("cls");
    menuEliminarVentas();
}

void Eliminados::menuEliminarVentas()
{
    Menu menu;
    int op=0;
    bool opcionValida=false;

    do
    {
        menu.setColor(7);
        cout<<"----------------------------------"<<endl;
        cout<<"Elija la opcion que desee realizar"<<endl;
        cout<<"1. Eliminar un registro"<<endl;
        cout<<"2. Eliminar archivo"<<endl;
        cout<<"0. Salir"<<endl;
        cout<<"==================================="<<endl;
        menu.setColor(0);
        cin>>op;
        system("cls");
        if(op> (0) && op<4)
        {
            opcionValida=true;
        }

        switch (op)
        {
        case 1:
            eliminarRegistroVenta();
            break;
        case 2:
            eliminarArchivoVentas();
            break;
        case 0:
            menu.getMainMenu();
            break;
        default:

            menu.mensajeDeError("Opcion invalida!, vuelva a intentarlo" );

        }
        system("pause");
        system("cls");

        opcionValida = false;
    }
    while(!opcionValida);
}
void Eliminados::menuEliminarProductos()
{
    Menu menu;
    int op=0;
    bool opcionValida=false;

    do
    {
        menu.setColor(7);
        cout<<"----------------------------------"<<endl;
        cout<<"Elija la opcion que desee realizar"<<endl;
        cout<<"1. Eliminar un registro"<<endl;
        cout<<"2. Eliminar archivo"<<endl;
        cout<<"0. Salir"<<endl;
        cout<<"==================================="<<endl;
        menu.setColor(0);
        cin>>op;
        system("cls");
        if(op> (0) && op<4)
        {
            opcionValida=true;
        }

        switch (op)
        {
        case 1:
            eliminarRegistroProducto();
            break;
        case 2:
            eliminarArchivoProductos();
            break;
        case 0:
            menu.getMainMenu();
            break;
        default:

            menu.mensajeDeError("Opcion invalida!, vuelva a intentarlo" );

        }
        system("pause");
        system("cls");

        opcionValida = false;
    }
    while(!opcionValida);
}

void Eliminados::eliminarRegistroProducto()
{
    ArchivoProductos Productos("ArchivoProductos.dat");
    Producto producto;
    Menu menu;

    int productoID;
    menu.setColor(7);
    cout << "Ingrese el ID del producto que desea eliminar: ";
    menu.setColor(0);
    cin>>productoID;

    Productos.eliminarRegistroProducto(productoID);
    system("pause");
    system("cls");

}

void Eliminados::eliminarArchivoProductos()
{
    ArchivoProductos Productos("ArchivoProductos.dat");
    Producto producto;

    Productos.eliminarArchivoProductos();
    system("pause");
    system("cls");
    menuEliminarProductos();
}
