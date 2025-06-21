package Clases;
public class Producto implements Imprimible {
        private int productoID = 0;
        private String nombreProducto; //[0] = '\0';
        private String categoriaProducto; //[0] = '\0';
        private String descripcionProducto;
        private double precioUnitario;
        private int stock;
        private double costo;
        private String unidadMedida;

    public Producto(int productoID, String nombreProducto, String categoriaProducto, String descripcionProducto, double precioUnitario, int stock, double costo, String unidadMedida) {
        this.productoID = productoID;
        this.nombreProducto = nombreProducto;
        this.categoriaProducto = categoriaProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.costo = costo;
        this.unidadMedida = unidadMedida;
    }

    public Producto(int productoID, String nombreProducto, String descripcionProducto, double precioUnitario, double costo, int stock, String unidadMedida) {
        this.productoID = productoID;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitario = precioUnitario;
        this.costo = costo;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
    }

    public Producto( String nombreProducto, String descripcionProducto, double precioUnitario, double costo, int stock, String unidadMedida) {
        this.productoID = productoID;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitario = precioUnitario;
        this.costo = costo;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public int getProductoID() {return productoID;}

    public void setProductoID(int productoID) {this.productoID = productoID;}

    public String getNombreProducto() {return nombreProducto;}

    public void setNombreProducto(String nombreProducto) {this.nombreProducto = nombreProducto;}

    public String getCategoriaProducto() {return categoriaProducto;}

    public void setCategoriaProducto(String categoriaProducto) {this.categoriaProducto = categoriaProducto;}

    public double getPrecioUnitario() {return precioUnitario;}

    public void setPrecioUnitario(double precioUnitario) {this.precioUnitario = precioUnitario;}

    public int getStock() {return stock;}

    public void setStock(int stock) {this.stock = stock;}

    @Override
    public void imprimir() {
        System.out.printf("%-5d %-20s %-40s %-10.2f %-10.2f %-10d %-15s\n",
                productoID, nombreProducto, descripcionProducto, precioUnitario, costo, stock, unidadMedida);
    }

    @Override
    public void imprimirEncabezado() {
        System.out.printf("%-5s %-20s %-40s %-10s %-10s %-10s %-15s\n",
                "ID", "Nombre", "Descripción", "Precio", "Costo", "Stock", "Unidad");
        System.out.println("-------------------------------------------------------------------------------------------------");
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
}
