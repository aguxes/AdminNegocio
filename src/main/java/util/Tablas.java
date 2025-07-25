package util;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.HashMap;
import javafx.geometry.Pos;


import java.lang.reflect.Method;
import java.util.Date;


public class Tablas {

    public static <T> TableView<T> crearTabla(Class<T> tipoClase, String[][] columnas)
    {
        /*  <T> Permite el llamado a cualquier clase(cliente, producto, etc..)
            La funcion crea una tabla de estilo TableView<T> es decir una tabla
            que se basa en los datos que obtiene de la clase que llamemos.  */
        TableView<T> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Adaptable al tipo de pantalla
        tabla.setPlaceholder(new Label("No hay elementos cargados."));
        tabla.getStyleClass().add("tabla-generica");

        for (String[] col : columnas) {
            String nombreDato = col[0];
            String valor = col[1];

            try {
                String getterName = "get" + valor.substring(0, 1).toUpperCase() + valor.substring(1);

                // Esta cosa de arriba es una genialidad, segun el nombre de dato y valor que recibe en el for por col[0] y col[1]
                //llama al getter de esa propiedad. ¿Como? =
                // "get" + :Asi emíeza este String
                // valor.substring(0, 1).toUpperCase() + :Esto agarra la primer letra que obtiene del segundo array de la matriz y la pasa a mayuscula
                // valor.substring(1); agarra todo el valor del segundo array de la matriz sin la pirmer letra.
                //Entonces nos queda: String getterName = "get + N + ombre" = getNombre (en el caso de "nombre").

                Method getter = tipoClase.getMethod(getterName); // esto simplemente para saber que tipo de dato utilizamos
                Class<?> returnType = getter.getReturnType(); // retornamos el tipo de dato

                TableColumn<T, ?> columnaDetectada = switch (returnType.getSimpleName()) {
                    // Aca dentro tenemos que poner todas las opciones de tipos de datos que puede retornar lo que nostros vamos a llamar
                    case "String" -> new TableColumn<T, String>(nombreDato);
                    case "int", "Integer" -> new TableColumn<T, Integer>(nombreDato);
                    case "float", "Float" -> new TableColumn<T, Float>(nombreDato);
                    case "boolean", "Boolean" -> new TableColumn<T, Boolean>(nombreDato);
                    case "Date" -> new TableColumn<T, Date>(nombreDato);
                    default -> new TableColumn<T, Object>(nombreDato);
                };

                columnaDetectada.setCellValueFactory(new PropertyValueFactory<>(valor)); // que hacen los <>?
                //<Cliente, String>("nombre") Son una forma abreviada de decir, mostra de la clase cliente
                //el string getter que lleva nombre
                tabla.getColumns().add(columnaDetectada);

            } catch (NoSuchMethodException e) {
                System.err.println("❌ No se encontró el getter adecuado para la columna: " + valor);
            }
        }

        return tabla;
    }
    public static VBox crearform( String [][] lineas, Map<String, TextField> entradas) {

        VBox formulario = new VBox(10);
        formulario.setPadding(new Insets(20));
        formulario.getStyleClass().add("form-box");

        for (String[] linea : lineas) {
            String label = linea[0];
            String valor = linea[1];

            TextField txt = new TextField();

            txt.setPromptText(label);
            txt.setMaxWidth(350);
            txt.getStyleClass().add("text-field");

            entradas.put(valor, txt);

            formulario.getChildren().addAll(txt);
        }
        return formulario;
    }
}
