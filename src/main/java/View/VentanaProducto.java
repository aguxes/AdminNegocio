package View;
//Manejo del diseño
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections; // Importo cosas para el diseño y lenguaje de Java
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
//Utilidades de Java
import java.math.BigDecimal;
import java.sql.Connection; // conecxion sql
import java.time.LocalDate; //Para la fecha
import java.util.ArrayList;
import javafx.fxml.FXML;

import javafx.scene.control.cell.PropertyValueFactory; // setear los col
import Clases.Principales.*;
import DataBase.*;
import javafx.stage.Modality; // que es esto??
import javafx.stage.Stage; // que es esto??
import util.Tablas;

import static View.AppController.mostrarAlerta; // importo funcion de otro archivo para ser utilizada mas de una vez

public class VentanaProducto {
    @FXML private TextArea outputArea; // x2
    @FXML private VBox contenedor; //x3

    public VentanaProducto(VBox contenedor, TextArea outputArea) {
        this.contenedor = contenedor;
        this.outputArea = outputArea;
    }

    public void mostrarVentanaSeleccionProducto(TextField idProductoField) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Producto");
        ventana.initModality(Modality.APPLICATION_MODAL);

        String[][] columnas = {
                {"ID", "productoID"},
                {"Nombre", "nombreProducto"},
                {"Precio", "precioUnitario"},
                {"Costo", "costo"},
                {"Stock", "stock"}
        };
        TableView<Producto> tabla = Tablas.crearTabla(Producto.class, columnas);

        // Obtener y cargar productos
        ArrayList <Producto> productos = ProductoDAO.cargarProductosEnLista();
        tabla.setItems(FXCollections.observableArrayList(productos));

        // Campo de búsqueda por ID
        TextField idInput = new TextField();
        idInput.setPromptText("Ingrese ID del producto");

        Button btnSeleccionar = new Button("Seleccionar");
        btnSeleccionar.setOnAction(e -> {
            try {
                int idBuscado = Integer.parseInt(idInput.getText().trim());
                Producto prod = productos.stream()
                        .filter(p -> p.getProductoID() == idBuscado)
                        .findFirst().orElse(null);
                if (prod != null) {
                    idProductoField.setText(String.valueOf(prod.getProductoID()));
                    ventana.close();
                } else {
                    mostrarAlerta("❌ No se encontró ningún producto con ese ID.");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("ID inválido.");
            }
        });

        // Doble clic en fila para seleccionar
        tabla.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tabla.getSelectionModel().getSelectedItem() != null) {
                Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
                idProductoField.setText(String.valueOf(seleccionado.getProductoID()));
                ventana.close();
            }
        });

        VBox layout = new VBox(10, tabla, idInput, btnSeleccionar);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 650, 450);
        ventana.setScene(scene);
        ventana.showAndWait();
    }

    public void verProductos() {
        contenedor.getChildren().clear();

        VBox tarjeta = new VBox(15);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.getStyleClass().add("card");
        VBox.setVgrow(tarjeta, Priority.ALWAYS);

        Label titulo = new Label("📦 Lista de Productos");
        titulo.getStyleClass().add("titulo-seccion");

        // Barra de búsqueda
        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);

        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Buscar por ID o Nombre");
        campoBusqueda.setPrefWidth(200);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-verde");

        barraBusqueda.getChildren().addAll(new Label("🔍 Buscar:"), campoBusqueda, btnBuscar);

        //Tabla
        TableView<Producto> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay productos cargados."));
        tabla.getStyleClass().add("tabla-clientes");
        VBox.setVgrow(tabla, Priority.ALWAYS);

        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("productoID"));

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));

        TableColumn<Producto, Double> colCosto = new TableColumn<>("Costo");
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Producto, String> colMedida = new TableColumn<>("Medida");
        colMedida.setCellValueFactory(new PropertyValueFactory<>("medidaNombre"));

        TableColumn<Producto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoriaNombre"));

        TableColumn<Producto, LocalDate> colAlta = new TableColumn<>("Fecha Alta");
        colAlta.setCellValueFactory(new PropertyValueFactory<>("fechaAlta"));

        TableColumn<Producto, LocalDate> colBaja = new TableColumn<>("Fecha Baja");
        colBaja.setCellValueFactory(new PropertyValueFactory<>("fechaBaja"));

        tabla.getColumns().addAll(colId, colNombre, colPrecio, colCosto, colStock,
                colMedida, colCategoria, colAlta, colBaja);

        // Label Cantidad
        Label lblCantidad = new Label();
        lblCantidad.getStyleClass().add("label-cantidad");

        // Cargar datos
        ArrayList<Producto> listaOriginal = ProductoDAO.cargarProductosEnLista();
        tabla.setItems(FXCollections.observableArrayList(listaOriginal));
        lblCantidad.setText("Total de productos: " + listaOriginal.size());

        //  Acción del boton Buscar
        btnBuscar.setOnAction(e -> {
            String texto = campoBusqueda.getText().trim().toLowerCase();
            if (texto.isEmpty()) {
                tabla.setItems(FXCollections.observableArrayList(listaOriginal));
                lblCantidad.setText("Total de productos: " + listaOriginal.size());
            } else {
                ArrayList<Producto> filtrados = new ArrayList<>();
                for (Producto p : listaOriginal) {
                    if (String.valueOf(p.getProductoID()).equals(texto) ||
                            p.getNombreProducto().toLowerCase().contains(texto)) {
                        filtrados.add(p);
                    }
                }
                tabla.setItems(FXCollections.observableArrayList(filtrados));
                lblCantidad.setText("Coincidencias: " + filtrados.size());
            }
        });

        //Armado final
        tarjeta.getChildren().addAll(titulo, barraBusqueda, tabla, lblCantidad);
        contenedor.getChildren().setAll(tarjeta);
    }

    public void agregarProducto() {
        contenedor.getChildren().clear();

        VBox tarjeta = new VBox(12);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.getStyleClass().add("form-box");

        Label titulo = new Label("➕ Agregar Producto");
        titulo.getStyleClass().add("titulo-principal");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre del producto");
        nombre.getStyleClass().add("input-form");

        TextField precio = new TextField();
        precio.setPromptText("Precio unitario");
        precio.getStyleClass().add("input-form");

        TextField costo = new TextField();
        costo.setPromptText("Costo");
        costo.getStyleClass().add("input-form");

        TextField stock = new TextField();
        stock.setPromptText("Stock");
        stock.getStyleClass().add("input-form");

        TextField idMedida = new TextField();
        idMedida.setPromptText("ID medida");
        idMedida.setEditable(false);
        idMedida.getStyleClass().add("input-form");

        Button btnMedida = new Button("📏 Seleccionar medida");
        btnMedida.getStyleClass().add("boton-secundario");
        btnMedida.setOnAction(e -> mostrarVentanaSeleccionMedida(idMedida));

        TextField idCategoria = new TextField();
        idCategoria.setPromptText("ID categoría");
        idCategoria.setEditable(false);
        idCategoria.getStyleClass().add("input-form");

        Button btnCategoria = new Button("🏷️ Seleccionar categoría");
        btnCategoria.getStyleClass().add("boton-secundario");
        btnCategoria.setOnAction(e -> mostrarVentanaSeleccionCategoria(idCategoria));

        //boolean activo = true;

        DatePicker fechAlta = new DatePicker();
        fechAlta.setPromptText("Fecha de alta");

        DatePicker fechaBaja = new DatePicker();
        fechaBaja.setPromptText("Fecha de baja (opcional)");

        Button btnGuardar = new Button("💾 Guardar Producto");
        btnGuardar.getStyleClass().add("boton-accion");

        btnGuardar.setOnAction(e -> {
            try {
                Producto nuevo = new Producto(
                        0,
                        nombre.getText(),
                        new BigDecimal(precio.getText()),
                        Double.parseDouble(costo.getText()),
                        Integer.parseInt(stock.getText()),
                        Integer.parseInt(idMedida.getText()),
                        Integer.parseInt(idCategoria.getText()),
                        true,
                        fechAlta.getValue().atStartOfDay()
                        //(fechaBaja.getValue() == null) ? null : fechaBaja.getValue().atStartOfDay()

                );
                if (ProductoDAO.insertarProducto(nuevo)) {
                    mostrarAlerta("✅ Producto agregado con éxito.");
                    verProductos(); // refresca la lista
                } else {
                    mostrarAlerta("❌ Error al agregar el producto.");
                }
            } catch (Exception ex) {
                mostrarAlerta("❌ Verificá los campos. Error: " + ex.getMessage());
            }
        });

        // Agrupación visual
        tarjeta.getChildren().addAll(
                titulo,
                nombre, precio, costo, stock,
                new HBox(10, idMedida, btnMedida),
                new HBox(10, idCategoria, btnCategoria),
                fechAlta, fechaBaja,
                btnGuardar
        );
        contenedor.getChildren().setAll(tarjeta);
    }


    public void mostrarVentanaSeleccionCategoria(TextField campoDestino) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Categoría");
        ventana.initModality(Modality.APPLICATION_MODAL);

        TableView<String> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));
        tabla.getColumns().add(colDescripcion);

        tabla.getItems().addAll(ProductoDAO.obtenerCategorias());

        tabla.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String seleccion = tabla.getSelectionModel().getSelectedItem();
                if (seleccion != null) {
                    int id = ProductoDAO.obtenerIdCategoria(seleccion);
                    campoDestino.setText(String.valueOf(id));
                    ventana.close();
                }
            }
        });

        VBox layout = new VBox(10, tabla);
        layout.setPadding(new Insets(10));
        ventana.setScene(new Scene(layout, 400, 300));
        ventana.showAndWait();
    }

    public void mostrarVentanaSeleccionMedida(TextField campoDestino) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Unidad de Medida");
        ventana.initModality(Modality.APPLICATION_MODAL);

        TableView<String> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));
        tabla.getColumns().add(colDescripcion);

        tabla.getItems().addAll(ProductoDAO.obtenerMedidas());

        tabla.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String seleccion = tabla.getSelectionModel().getSelectedItem();
                if (seleccion != null) {
                    int id = ProductoDAO.obtenerIdMedida(seleccion);
                    campoDestino.setText(String.valueOf(id));
                    ventana.close();
                }
            }
        });

        VBox layout = new VBox(10, tabla);
        layout.setPadding(new Insets(10));
        ventana.setScene(new Scene(layout, 400, 300));
        ventana.showAndWait();
    }



    public void modificarProducto() {
        outputArea.setText("✏️ Editar un producto existente.");
    }

    public void eliminarProducto()
    {
        Stage ventana = new Stage();
        ventana.setTitle("Eliminar Producto");

        TextField txtId = new TextField();
        txtId.setPromptText("ID del Producto");

        Label lblConfirmacion = new Label();

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            int id = Integer.parseInt(txtId.getText());
            String nombre = ProductoDAO.buscarxNombre(id);
            if (nombre != null) {
                lblConfirmacion.setText("¿Eliminar " + nombre + "?");
            } else {
                lblConfirmacion.setText("Producto no encontrado.");
            }
        });

        Button btnEliminar = new Button("Sí, eliminar");
        btnEliminar.setOnAction(e -> {
            ProductoDAO.eliminar(Integer.parseInt(txtId.getText()));
            ventana.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> ventana.close());

        HBox botones = new HBox(10, btnEliminar, btnCancelar);
        botones.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, new Label("ID Producto:"), txtId, btnBuscar, lblConfirmacion, botones);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene escena = new Scene(layout, 300, 250);
        ventana.setScene(escena);
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.showAndWait();
    }
}
