package Clases;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Venta implements Imprimible{
    private int idVenta;
    private int idEmpleado;
    private int idCliente;
    private LocalDate fecha;
    private int formaDePago;
    private BigDecimal importeTotal; //es mejor BigDecimal para finanzas
    private boolean estado; //ver para que sirve


    public Venta(int idVenta, int idEmpleado, int idCliente, LocalDate fecha, int formaDePago, BigDecimal importeTotal, boolean estado) {
        this.idVenta = idVenta;
        this.idEmpleado = idEmpleado;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.formaDePago = formaDePago;
        this.importeTotal = importeTotal;
        this.estado = estado;
    }

    public int getIdVenta() {return idVenta;}

    public int getIdEmpleado() {return idEmpleado;}

    public int getIdCliente() {return idCliente;}

    public LocalDate getFecha() {return fecha;}

    public void setFecha(LocalDate fecha) {this.fecha = fecha;}

    public int getFormaDePago() {return formaDePago;}

    public void setFormaDePago(int formaDePago) {this.formaDePago = formaDePago;}

    public BigDecimal getImporteTotal() {return importeTotal;}

    public void setImporteTotal(BigDecimal importeTotal) {this.importeTotal = importeTotal;}

    public boolean isEstado() {return estado;}

    public void setEstado(boolean estado) {this.estado = estado;}

    @Override
    public void imprimir() {
        System.out.printf("%-8d %-10d %-10d %-12s %-12d %-12.2f %-10b\n",
                idVenta, idEmpleado, idCliente, fecha.toString(), formaDePago,
                importeTotal, estado
        );
    }


    /*
    void Venta::cargarVenta() {
    Menu menu;
    ArchivoEmpleados Empleados("ArchivoEmpleados.dat");
    ArchivoClientes Clientes("ArchivoClientes.dat");
    ArchivoVentas Ventas("ArchivoVentas.dat");
    ArchivoDetalleVentas DetalleVentas("ArchivoDetalleVentas.dat");
    Empleado registroEmpleado;
    Cliente registroCliente;
    DetalleVenta registroDetalle;
    float montoTotalVenta;

    if(Ventas.CantidadRegistros() == 0){
        idVenta = 1;
    }else{
        idVenta = Ventas.Leer((Ventas.CantidadRegistros()-1)).getIdVenta() + 1;
    }

    idVenta = Ventas.CantidadRegistros() + 1;
    int input = 0;
    char inputChar = 'n';
    bool vendedorValido = false, clienteValido = false, formaDePagoValida = false;

    rlutil::cls();
    Menu::setColor(7);
    cout << "Ingrese la fecha de la venta:" << endl;
    Menu::setColor(0);
    fecha.cargarFecha();
    system("pause");
    system("cls");

    while (!vendedorValido) {
        Menu::setColor(7);
        cout << "Seleccione el Empleado (ID - Nombre):" << endl;
        Menu::setColor(7);

        for (int i = 0; i < Empleados.CantidadRegistros(); i++) {
            registroEmpleado = Empleados.Leer(i);
            cout << "ID: " << registroEmpleado.getID() << " - Nombre: " << registroEmpleado.getNombre() <<" "<< registroEmpleado.getApellido()<< endl;
        }
        Menu::setColor(7);
        cout << "Ingrese el ID del Empleado: ";
        Menu::setColor(0);
        cin >> input;

        if (Empleados.Buscar(input).getID() != -1) {
            idEmpleado = input;
            vendedorValido = true;
        } else {

            menu.mensajeDeError("ID invalido. Intente nuevamente." );
            Menu::setColor(7);
            system("pause");
            system("cls");

        }
    }
    menu.setColor(7);
    cout<<"EMPLEADO ELEGIDO: "<<endl;
    Empleados.Buscar(idEmpleado).mostrarEmpleado();
    cout<<endl;

    system("pause");
    system("cls");

    while (!clienteValido) {
        Menu::setColor(7);
        cout << "Seleccione el cliente (ID - Nombre):" << endl;
        Menu::setColor(7);

        for (int i = 0; i < Clientes.CantidadRegistros(); i++) {
            registroCliente = Clientes.Leer(i);
            cout << "ID: " << registroCliente.getID() << " - Nombre: " << registroCliente.getNombre() << registroCliente.getApellido() << endl;
        }
        Menu::setColor(7);
        cout << "Ingrese el ID del cliente: ";
        Menu::setColor(0);
        cin >> input;

        if (Clientes.Buscar(input).getID() != -1) {
            idCliente = input;
            clienteValido = true;
        } else {

            menu.mensajeDeError("ID invalido. Intente nuevamente.");
            Menu::setColor(7);
            system("pause");
            system("cls");
        }
    }
    Menu::setColor(7);
    system("pause");
    system("cls");

    while (!formaDePagoValida) {
        Menu::setColor(7);
        cout << "Seleccione forma de pago:" << endl;
        cout << "1. Efectivo\n2. Debito\n3. Credito" << endl << "Opcion: ";
        Menu::setColor(0);
        cin >> input;
        formaDePago = input;

        if (formaDePago > 0 && formaDePago < 4) {
            formaDePagoValida = true;
            Menu::setColor(2);
            switch (formaDePago) {
                case 1: cout << "Metodo de pago seleccionado: Efectivo" << endl; break;
                case 2: cout << "Metodo de pago seleccionado: Debito" << endl; break;
                case 3: cout << "Metodo de pago seleccionado: Credito" << endl; break;
            }
            Menu::setColor(7);
        } else {

            menu.mensajeDeError("Metodo de pago no valido.");

        }
    }
    system("pause");
    system("cls");

    bool validacionDetalles = false;
    while (!validacionDetalles) {
        Menu::setColor(7);
        cout << "--------------- DETALLE DE VENTA --------------------" << endl;
        Menu::setColor(0);
        registroDetalle.cargarDetalleDeVenta(getIdVenta());
        if (DetalleVentas.Guardar(registroDetalle)) {
            Menu::setColor(2);
            cout << "El detalle de venta se ha registrado correctamente" << endl;
            Menu::setColor(7);
        }

        cout << "-----------------------------------------------------" << endl;

        while (inputChar != 'S' && inputChar != 'N') {
            Menu::setColor(7);
            cout << "Desea registrar otro producto para esta venta? S/N: ";
            Menu::setColor(0);
            cin >> inputChar;
            if (inputChar != 'S' && inputChar != 'N') {

                menu.mensajeDeError("Opcion invalida! Vuelva a intentarlo" );

            }
        }

        Menu::setColor(0);
        if (inputChar == 'N') {
            validacionDetalles = true;
        }else{
            inputChar = 'n';
            Menu::setColor(7);
            cout<< "Siguiente detalle!"<<endl;
        }
        system("pause");
        system("cls");
    }
    for (int i = 1; i< DetalleVentas.ContLineas(idVenta) ;i++ ){
        montoTotalVenta += DetalleVentas.BuscarPorLinea(idVenta,i).getImporte();
    }
    importeTotal = montoTotalVenta;
    }



    void Venta::mostrarVenta() {
    ArchivoEmpleados Empleados("ArchivoEmpleados.dat");
    ArchivoClientes Clientes("ArchivoClientes.dat");
    ArchivoDetalleVentas DetallesVentas("ArchivoDetalleVentas.dat");
    Menu menu;
    Venta saldo;
    menu.setColor(1);
    cout << endl<<endl;
    cout << "======================================================================" << endl;
    cout << "=========================== ID DE VENTA: " << getIdVenta() << " ===========================" << endl;
    cout << "======================================================================" << endl;
    cout << endl<<endl<<endl;
    menu.setColor(7);
    cout << "FECHA: ";
    getFecha().mostrarFecha();
    cout << "VENDEDOR: " << endl;

    Empleados.Buscar(getIdEmpleado()).mostrarEmpleado();
    cout << endl;
    cout << "COMPRADOR: " << endl;
    Clientes.Buscar(getIdCliente()).mostrarCliente();
    cout << endl;
    cout << "FORMA DE PAGO: ";
    menu.setColor(0);
    switch (getFormaDePago()) {
        case 1: cout << "Efectivo" << endl; break;
        case 2: cout << "Debito" << endl; break;
        case 3: cout << "Credito" << endl; break;
        menu.setColor(7);
    }
    cout << endl;
    menu.setColor(5);
    cout << endl<<endl<<endl;
    cout << "======================================================================" << endl;
    cout << "========================== DETALLE DE VENTA ==========================" << endl;
    cout << "======================================================================" << endl;
    cout << endl<<endl<<endl;
    menu.setColor(7);
    cout<< "ARTICULOS COMPRADOS: ";
    menu.setColor(0);
    cout << DetallesVentas.ContLineas(getIdVenta())<<endl<<endl;
    menu.setColor(7);
    for(int i = 0 ; i <= DetallesVentas.ContLineas(idVenta) ; i++ )
    {
        saldo.importeTotal += DetallesVentas.BuscarPorLinea(idVenta,i).getImporte();
    }
    DetallesVentas.Buscar(getIdVenta()).mostrarDetalleDeVenta();
    cout<<"Importe TOTAL de la Venta: ";
    menu.setColor(0);
    cout <<"$";
    cout<< saldo.getImporteTotal();
    menu.setColor(7);
    cout << endl;
    */
}

