package Clases.Principales;

import java.time.LocalDate;

public class Producto implements Imprimible {
    private int productoID;
    private String nombreProducto;
    private double precioUnitario;
    private double costo;
    private int stock;

    private int idUnidadMedida;
    private int idCategoria;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

    // Opcionales si hacés JOINs para mostrar
    private String nombreUnidadMedida; // opcional
    private String nombreCategoria;    // opcional

    // Constructor principal
    public Producto(int productoID, String nombreProducto, double precioUnitario, double costo, int stock,
                    int idUnidadMedida, int idCategoria, LocalDate fechaAlta, LocalDate fechaBaja) {
        this.productoID = productoID;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.costo = costo;
        this.stock = stock;
        this.idUnidadMedida = idUnidadMedida;
        this.idCategoria = idCategoria;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    // Getters y setters
    public int getProductoID() { return productoID; }
    public String getNombreProducto() { return nombreProducto; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getCosto() { return costo; }
    public int getStock() { return stock; }
    public int getIdUnidadMedida() { return idUnidadMedida; }
    public int getIdCategoria() { return idCategoria; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public LocalDate getFechaBaja() { return fechaBaja; }



    // Métodos para impresión
    public void imprimirEncabezado() {
        System.out.printf("%-5s %-20s %-10s %-10s %-10s\n",
                "ID", "Nombre", "Precio", "Costo", "Stock");
        System.out.println("---------------------------------------------------");
    }
    public void imprimir() {
        System.out.printf("%-5d %-20s %-10.2f %-10.2f %-10d\n",
                productoID, nombreProducto, precioUnitario, costo, stock);
    }
}









    /*
        int Producto::getID() { return productoID; }
    const char* Producto::getNombre() { return nombreProducto; }
    const char* Producto::getCategoriaProducto() { return categoriaProducto; }
    float Producto::getPrecioUnitario() { return precioUnitario; }
    int Producto::getStock() { return stock; }

    void Producto::setProductoID(int _productoID) { productoID = _productoID; }


    void Producto::setNombre(const char* _nombreProducto) {
        strncpy(nombreProducto, _nombreProducto, sizeof(nombreProducto) - 1);
        nombreProducto[sizeof(nombreProducto) - 1] = '\0';
    }

    void Producto::setCategoriaProducto(const char* _categoriaProducto) {
        strncpy(categoriaProducto, _categoriaProducto, sizeof(categoriaProducto) - 1);
        categoriaProducto[sizeof(categoriaProducto) - 1] = '\0';
    }

    void Producto::setPrecioUnitario(float _PrecioUnitario) { precioUnitario = _PrecioUnitario; }

    void Producto::setStock(int _stock) { stock = _stock; }


    void Producto::cargarProducto()
    {
        ArchivoProductos Productos("ArchivoProductos.dat");

        int inputProductoID;
        char inputNombreProducto[50];
        char inputCategoriaProducto[50];
        float inputPrecioUnitario;
        int inputStock;

        if(Productos.CantidadRegistros() == 0){
            productoID = 1;
        }else{
            productoID = Productos.Leer((Productos.CantidadRegistros()-1)).getID() + 1;
        }

        cin.ignore();
        Menu::setColor(7);
        cout << "Ingrese el nombre del Producto: ";
        Menu::setColor(0);
        cin.getline(inputNombreProducto, 50);
        setNombre(inputNombreProducto);
        system("pause");
        system("cls");

        Menu::setColor(7);
        cout << "Ingrese la categoria del Producto: ";
        Menu::setColor(0);
        cin.getline(inputCategoriaProducto, 50);
        setCategoriaProducto(inputCategoriaProducto);
        system("pause");
        system("cls");

        Menu::setColor(7);
        cout << "Ingrese el precio del Producto: ";
        Menu::setColor(0);
        cin >> inputPrecioUnitario;
        setPrecioUnitario(inputPrecioUnitario);
        system("pause");
        system("cls");

        Menu::setColor(7);
        cout << "Ingrese el stock del Producto: ";
        Menu::setColor(0);
        cin >> inputStock;
        setStock(inputStock);
        system("pause");
        system("cls");

    }
    void Producto::mostrarProducto()
    {
        Menu menu;
        Menu::setColor(1);
        cout << "////////////////// ID PRODUCTO: " << getID() << " //////////////////" << endl;
        Menu::setColor(7);
        cout << "Nombre del Producto: ";
        menu.setColor(0);
        cout<< getNombre() << endl;
        menu.setColor(7);
        cout << "Categoria del Producto: ";
        menu.setColor(0);
        cout << getCategoriaProducto() << endl;
        menu.setColor(7);
        cout << "Precio Unitario: ";
        menu.setColor(0);
        cout << getPrecioUnitario() << endl;
        menu.setColor(7);
        cout << "Stock: ";
        menu.setColor(0);
        cout << getStock() << endl;
        menu.setColor(1);
        cout << "////////////////////////////////////////////////////" << endl << endl;
        Menu::setColor(7);
    }
    */

