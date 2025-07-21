package View;
//Manejo del diseño
import javafx.collections.FXCollections; // Importo cosas para el diseño y lenguaje de Java
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
//Utilidades de Java
import java.sql.Connection; // conecxion sql
import java.time.LocalDate; //Para la fecha
import java.util.ArrayList;
import javafx.fxml.FXML;

import javafx.scene.control.cell.PropertyValueFactory; // setear los col

import View.*;
import Clases.Extras.*;
import Clases.Principales.*;
import DataBase.*;
import util.Mapper;

import javafx.stage.Modality; // que es esto??
import javafx.stage.Stage; // que es esto??

import static View.AppController.mostrarAlerta; // importo funcion de otro archivo para ser utilizada mas de una vez

public class VentanaProducto {
    @FXML private VBox menuLateral; // creo los cosos para reutilizarse varias veces
    @FXML private TextArea outputArea; // x2
    @FXML private VBox contenedor; //x3

    private static final Connection conn = DataBaseConnection.getConnection();

    public VentanaProducto(VBox contenedor, TextArea outputArea) {
        this.contenedor = contenedor;
        this.outputArea = outputArea;
    }

    public void mostrarVentanaSeleccionProducto(TextField idProductoField) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Producto");
        ventana.initModality(Modality.APPLICATION_MODAL);

        TableView<Producto> tabla = new TableView<>();

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

        tabla.getColumns().addAll(colId, colNombre, colPrecio, colCosto, colStock);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        TableView<Producto> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay productos cargados."));

        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("productoID")); // Se le asigna a la celda el valor provado de la clase Producto

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));

        TableColumn<Producto, Double> colCosto = new TableColumn<>("Costo");
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Producto, String> colMedida = new TableColumn<>("Medida");
        colMedida.setCellValueFactory(new PropertyValueFactory<>("idMedida"));

        TableColumn<Producto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));

        TableColumn<Producto, LocalDate> colAlta = new TableColumn<>("Fecha Alta");
        colAlta.setCellValueFactory(new PropertyValueFactory<>("fechaAlta"));

        TableColumn<Producto, LocalDate> colBaja = new TableColumn<>("Fecha Baja");
        colBaja.setCellValueFactory(new PropertyValueFactory<>("fechaBaja"));

        tabla.getColumns().addAll(colId, colNombre, colPrecio, colCosto, colStock, colMedida, colCategoria, colAlta, colBaja);

        ArrayList<Producto> lista = ProductoDAO.cargarProductosEnLista();
        tabla.setItems(FXCollections.observableArrayList(lista));

        VBox layout = new VBox(10, new Label("📦 Lista de productos"), tabla);
        layout.setPadding(new Insets(20));

        contenedor.getChildren().add(layout);
    }



    public void agregarProducto() {
        outputArea.setText("➕ Formulario para agregar producto.");
    }

    public void modificarProducto() {
        outputArea.setText("✏️ Editar un producto existente.");
    }

    public void eliminarProducto() {
        outputArea.setText("🗑️ Eliminar producto por ID.");
    }

    public void buscarProducto() {
        outputArea.setText("🔍 Buscar producto por nombre o ID.");
    }
}
