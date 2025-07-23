package util;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Method;

public class Tablas {
    public static <T> TableView<T> crearTabla(Class<T> tipoClase, String[][] columnas) {
        TableView<T> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay elementos cargados."));
        tabla.getStyleClass().add("tabla-generica");

        for (String[] col : columnas) {
            String nombreVisible = col[0];
            String propiedad = col[1];

            try {
                String getterName = "get" + propiedad.substring(0, 1).toUpperCase() + propiedad.substring(1);
                Method getter = tipoClase.getMethod(getterName);
                Class<?> returnType = getter.getReturnType();

                TableColumn<T, ?> columnaDetectada = switch (returnType.getSimpleName()) {
                    case "String" -> new TableColumn<T, String>(nombreVisible);
                    case "int", "Integer" -> new TableColumn<T, Integer>(nombreVisible);
                    case "float", "Float" -> new TableColumn<T, Float>(nombreVisible);
                    case "boolean", "Boolean" -> new TableColumn<T, Boolean>(nombreVisible);
                    default -> new TableColumn<T, Object>(nombreVisible);
                };

                columnaDetectada.setCellValueFactory(new PropertyValueFactory<>(propiedad));
                tabla.getColumns().add(columnaDetectada);

            } catch (NoSuchMethodException e) {
                System.err.println("❌ No se encontró getter para propiedad: " + propiedad);
            }
        }

        return tabla;
    }
}
