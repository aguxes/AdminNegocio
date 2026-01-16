package View.prueba;
//Manejo del diseño

import Clases.Principales.Producto;
import DataBase.ProductoDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Tablas;
import util.prueba.Tablass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static View.AppController.mostrarAlerta;

public class VentanaProducto {
    @FXML private TextArea outputArea; // x2
    @FXML private VBox contenedor; //x3

    public VentanaProducto(VBox contenedor, TextArea outputArea) {
        this.contenedor = contenedor;
        this.outputArea = outputArea;
    }

    public static void mostrarVentanaSeleccionProducto(TextField idProductoField) {
        Stage ventana = new Stage();
        ventana.setTitle("Seleccionar Producto");
        ventana.initModality(Modality.APPLICATION_MODAL);


        var random = new HashMap<String, String>();
        random.put("ID", "productoID");
        random.put("Nombre", "nombreProducto");
        random.put("Precio", "precioUnitario");
        random.put("Costo", "costo");
        random.put("Stock", "stock");
        random.put("Telefono", "telefonoStr");

        String[][] columnas = {
                {"ID", "productoID"},
                {"Nombre", "nombreProducto"},
                {"Precio", "precioUnitario"},
                {"Costo", "costo"},
                {"Stock", "stock"}
        };
        TableView<Producto> tabla = Tablass.crearTabla(Producto.class, random);

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

        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.getStyleClass().add("card");
        VBox.setVgrow(tarjeta, Priority.ALWAYS);

        Label titulo = new Label("📦 Lista de Productos");
        titulo.getStyleClass().add("titulo-seccion");

/*        TextField campoBusqueda = new TextField();

        campoBusqueda.setPrefWidth(200);
*/
        Label lblCampo = new Label("Buscar por:");
        lblCampo.getStyleClass().add("label-form");

        ChoiceBox<String> cC = new ChoiceBox<>();
        cC.getItems().addAll("nombre", "ID", "Categoria", "Medida");
        cC.setValue("Categoria");
        cC.getStyleClass().add("input-form");

        TextField txtValor = new TextField();
        txtValor.setPromptText("Ej: Electronica");
        txtValor.getStyleClass().add("input-form");
        txtValor.setMaxWidth(220);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btn-verde");

        var random = new HashMap<String, String>();
        random.put("ID", "productoID");
        random.put("Nombre", "nombreProducto");
        random.put("Precio", "precioUnitario");
        random.put("Costo", "costo");
        random.put("Stock", "stock");
        random.put("Medida", "medidaNombre");
        random.put("Categoría", "categoriaNombre");
        random.put("Fecha Alta", "fechaAlta");
        random.put("Fecha Baja", "fechaBaja");

        String[][] columnas = {
                {"ID", "productoID"},
                {"Nombre", "nombreProducto"},
                {"Precio", "precioUnitario"},
                {"Costo", "costo"},
                {"Stock", "stock"},
                {"Medida", "medidaNombre"},
                {"Categoría", "categoriaNombre"},
                {"Fecha Alta", "fechaAlta"},
                {"Fecha Baja", "fechaBaja"}
        };

        TableView<Producto> tabla = Tablass.crearTabla(Producto.class, random);

        HBox barraBusqueda = new HBox(10); // espacio entre elementos STYLE
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.getChildren().addAll(lblCampo, cC, txtValor, btnBuscar);

        Label lblCantidad = new Label();
        lblCantidad.getStyleClass().add("label-cantidad");

        ArrayList<Producto> listaOriginal = ProductoDAO.cargarProductosEnLista();
        tabla.setItems(FXCollections.observableArrayList(listaOriginal));
        lblCantidad.setText("Total de productos: " + listaOriginal.size());

        btnBuscar.setOnAction(e -> {
            String campo = cC.getValue();
            String valor = txtValor.getText();
            ArrayList<Producto> resultado = ProductoDAO.buscarProductoPorDato(campo, valor);

            if (resultado.isEmpty()) {

                tabla.getItems().clear();
                mostrarAlerta("❌ No se encontró ningún producto con ese dato.");
            } else {
                tabla.getItems().setAll(resultado);
            }
        });
        txtValor.setOnAction(e -> btnBuscar.fire());
        //Armado final

        btnBuscar.setPrefWidth(240); // mismo ancho que txtValor
        btnBuscar.setAlignment(Pos.CENTER);

        tarjeta.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho disponible
        VBox.setVgrow(tarjeta, Priority.ALWAYS); // Opcional para que crezca si hay espacio


        tarjeta.getChildren().addAll(titulo, barraBusqueda, tabla, lblCantidad);
        contenedor.getChildren().setAll(tarjeta);
    }

    public void agregarProducto() {
        contenedor.getChildren().clear();

        Label titulo = new Label("➕ Agregar Producto");
        titulo.getStyleClass().add("titulo-principal");

        Map<String, Object> campos = new HashMap<>();
        Map<String, Runnable> acciones = new HashMap<>();

        // Acciones para botones de selección
        acciones.put("idMedida", () -> mostrarVentanaSeleccionMedida((TextField) campos.get("idMedida")));
        acciones.put("idCategoria", () -> mostrarVentanaSeleccionCategoria((TextField) campos.get("idCategoria")));

        // Definición de campos
        String[][] lineas = {
                {"Nombre del producto", "nombreProducto", "text"},
                {"Precio unitario", "precioUnitario", "text"},
                {"Costo", "costo", "text"},
                {"Stock", "stock", "text"},
                {"ID medida", "idMedida", "select"},
                {"ID categoría", "idCategoria", "select"},
                {"Fecha de alta", "fechaAlta", "date"},
                {"Fecha de baja (opcional)", "fechaBaja", "date"}
        };
        VBox formCampos = Tablas.formtoAddEntidad(lineas, campos, acciones);

        Button btnGuardar = new Button("💾 Guardar Producto");
        btnGuardar.getStyleClass().add("boton-accion");

        btnGuardar.setOnAction(e -> {
            try {
                Producto nuevo = new Producto(
                        0,
                        ((TextField) campos.get("nombreProducto")).getText().trim(),
                        new BigDecimal(((TextField) campos.get("precioUnitario")).getText().trim()),
                        Double.parseDouble(((TextField) campos.get("costo")).getText().trim()),
                        Integer.parseInt(((TextField) campos.get("stock")).getText().trim()),
                        Integer.parseInt(((TextField) campos.get("idMedida")).getText().trim()),
                        Integer.parseInt(((TextField) campos.get("idCategoria")).getText().trim()),
                        true,
                        ((DatePicker) campos.get("fechaAlta")).getValue().atStartOfDay()
                        // Fecha baja opcional
                        // ((DatePicker) campos.get("fechaBaja")).getValue() != null ?
                        // ((DatePicker) campos.get("fechaBaja")).getValue().atStartOfDay() : null
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

        VBox formFinal = new VBox(10, titulo, formCampos, btnGuardar);
        contenedor.getChildren().add(formFinal);
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
