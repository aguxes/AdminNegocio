package Principal;

import Clases.Cliente;
import Clases.*;
import DataBase.*;



import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Imprimible> listaClientes = new ArrayList<>();
        ArrayList<Imprimible> listaVentas = new ArrayList<>();
        ArrayList<Imprimible> listaProdutos = new ArrayList<>();
        ArrayList<Imprimible> listaReportes = new ArrayList<>();

        ClienteDAO daoCliente = new ClienteDAO();
        VentaDAO daoVenta = new VentaDAO();
        ProductoDAO daoProducto = new ProductoDAO();
        ReportesDAO daoReportes = new ReportesDAO();

        Scanner scan = new Scanner(System.in); //esto crea el input, lo llamas poniendo:
        int opcion = 0;
        daoCliente.cargarClientesEnLista(listaClientes); //con cada vuelta del bucle si borro un dato se actualiza

        //AGORA SI MANITO, AGORA SI
        //Tenemos main
        do{
            try{
                opcion = MetodosSwitch.menuPrincipal(scan, listaClientes,listaVentas, listaProdutos, listaReportes, daoCliente, daoVenta, daoProducto, daoReportes);

            }catch(Exception e){
                System.out.println("Opcion no permitida, solo numeros");
            }
        }while(opcion != 5);

    }
}