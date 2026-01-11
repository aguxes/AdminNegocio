package WebServer;

import static spark.Spark.*;

import com.google.gson.Gson;
import DataBase.ClienteDAO;
import DataBase.DataBaseConnection; // Importar conexión
import Clases.Principales.Cliente;

public class ServidorWeb {
    public static void iniciar() {
        // Inicializar conexión DB al arranque
        DataBaseConnection.getConnection();
        
        port(4567);  // Puerto del servidor

        // Configuración de archivos estáticos (HTML, CSS, JS)
        staticFiles.externalLocation("web");

        Gson gson = new Gson();

        // ➤ API REST Clientes
        path("/api/clientes", () -> {
            
            // Listar todos
            get("", (req, res) -> {
                res.type("application/json");
                try {
                    return gson.toJson(ClienteDAO.obtenerTodos());
                } catch (Exception e) {
                    e.printStackTrace(); // Ver error en consola
                    res.status(500);
                    return gson.toJson("Error interno: " + e.getMessage());
                }
            });

            // Obtener uno por ID
            get("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                Cliente c = ClienteDAO.obtenerClientePorId(id);
                if (c != null) {
                    return gson.toJson(c);
                } else {
                    res.status(404);
                    return gson.toJson("Cliente no encontrado");
                }
            });

            // Crear Cliente
            post("", (req, res) -> {
                res.type("application/json");
                try {
                    Cliente nuevoCliente = gson.fromJson(req.body(), Cliente.class);
                    // Validaciones básicas podrían ir aquí
                    if (ClienteDAO.insertarCliente(nuevoCliente)) {
                        res.status(201);
                        return gson.toJson("Cliente creado exitosamente");
                    } else {
                        res.status(500);
                        return gson.toJson("Error al crear el cliente");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    res.status(400);
                    return gson.toJson("Error en los datos enviados: " + e.getMessage());
                }
            });

            // Actualizar Cliente
            put("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id")); // El ID viene en la URL, pero el objeto en el body
                try {
                    Cliente clienteAEditar = gson.fromJson(req.body(), Cliente.class);
                    // Asegurar que el ID del objeto coincida o usarlo para buscar primero (depende de tu lógica)
                    // En tu DAO, actualizar usa el DNI para buscar Persona y Cliente. 
                    // OJO: Si editas el DNI, esto es más complejo. Asumimos que el DNI es la clave para updates por ahora.
                    
                    if (ClienteDAO.actualizarCliente(clienteAEditar)) {
                        return gson.toJson("Cliente actualizado exitosamente");
                    } else {
                        res.status(500);
                        return gson.toJson("Error al actualizar cliente");
                    }
                } catch (Exception e) {
                    res.status(400);
                    return gson.toJson("Error en los datos enviados");
                }
            });

            // Eliminar Cliente
            delete("/:id", (req, res) -> {
                res.type("application/json");
                int id = Integer.parseInt(req.params(":id"));
                String resultado = ClienteDAO.eliminar(id);
                return gson.toJson(resultado);
            });
        });
        
        // Manejo global de excepciones para ver errores 500 en consola
        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace(); // ¡IMPORTANTE! Imprimir stack trace en consola
        });
    }

    public static void main(String[] args) {
        iniciar();
    }
}