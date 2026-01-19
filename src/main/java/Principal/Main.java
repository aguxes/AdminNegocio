package Principal;

import Clases.Principales.Imprimible;
import DataBase.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        /*
        ArrayList<Imprimible> listaClientes = new ArrayList<>();
        ArrayList<Imprimible> listaVentas = new ArrayList<>();
        ArrayList<Imprimible> listaProdutos = new ArrayList<>();
        ArrayList<Imprimible> listaReportes = new ArrayList<>();
        */
        ClienteDAO daoCliente = new ClienteDAO();
        VentaDAO daoVenta = new VentaDAO();
        ProductoDAO daoProducto = new ProductoDAO();
        ReportesDAO daoReportes = new ReportesDAO();

        Scanner scan = new Scanner(System.in);
        int opcion = 0;
        //ClienteDAO.cargarClientesEnLista(listaClientes); //con cada vuelta del bucle si borro un dato se actualiza
    }
}