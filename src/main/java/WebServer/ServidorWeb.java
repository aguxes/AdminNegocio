package WebServer;

import static spark.Spark.*;

import com.google.gson.Gson;
import DataBase.ClienteDAO;
import Clases.Principales.Cliente;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServidorWeb {
    public static void iniciar() {
        port(4567);  // Puerto del servidor

        // 👉 Servir index.html
        get("/", (req, res) -> {
            res.type("text/html");
            return new String(Files.readAllBytes(Paths.get("web/index.html")), StandardCharsets.UTF_8);
        });

        // 👉 Servir estilos CSS
        get("/estilos/estilos.css", (req, res) -> {
            res.type("text/css");
            return new String(Files.readAllBytes(Paths.get("web/estilos/estilos.css")), StandardCharsets.UTF_8);
        });

        // 👉 Servir JS
        get("/js/Main.js", (req, res) -> {
            res.type("application/javascript");
            return new String(Files.readAllBytes(Paths.get("web/js/Main.js")), StandardCharsets.UTF_8);
        });

        // 👉 Endpoint de clientes
        get("/clientes", (req, res) -> {
            res.type("application/json");
            ArrayList<Cliente> lista = new ArrayList<>();
            ClienteDAO.cargarClientesEnLista(lista);
            return new Gson().toJson(lista);
        });
    }

    public static void main(String[] args) {
        iniciar();
    }
}
