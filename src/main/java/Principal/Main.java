package Principal;

import Clases.Cliente;
import Clases.*;
import DataBase.*;



import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Imprimible> lista = new ArrayList<>();
        ClienteDAO daoCliente = new ClienteDAO();
        VentaDAO daoVenta = new VentaDAO();
        ProductoDAO daoProducto = new ProductoDAO();

        Cliente cliente = new Cliente(null , 0, null, null, 0, null, 0);

        Scanner scan = new Scanner(System.in); //esto crea el input, lo llamas poniendo:
        int opcion = 0;

        do{
            lista.clear(); //Borro todo lo de la lista
            daoCliente.cargarClientesEnLista(lista); //con cada vuelta del bucle si borro un dato se actualiza

            try{ //basicamente es para que si poonen un string en vez de int no se rompa nada
                MetodosSwitch.mostrarMenu();
                opcion = Integer.parseInt(scan.nextLine());

                switch (opcion) {
                    case 1: // Ver clientes
                        daoCliente.imprimirClientes(lista);
                        break;

                    case 2: // Agregar nuevo cliente
                        Cliente nuevo = daoCliente.agregarClientePorConsola(scan);
                        ClienteDAO.insertar(nuevo);
                        break;

                    case 3: // Modificar datos de un cliente
                        daoCliente.modificarClientePorId(scan, lista);
                        break;

                    case 4: // Eliminar cliente por ID
                        daoCliente.eliminarPorId(scan);
                        break;

                    case 5: // Buscar cliente por nombre/email
                        daoCliente.buscarClientePorDato(scan);
                        break;

                    case 6: // Ver ventas
                        daoVenta.mostrarTodasLasVentas();
                        break;

                    case 7: // Registrar nueva venta
                        daoVenta.registrarVentaPorConsola(scan, lista);
                        break;

                    case 8: // Ver ventas por cliente
                        daoVenta.mostrarVentasPorCliente(scan);
                        break;

                    case 9: // Ver productos
                        daoProducto.mostrarProductos();
                        break;

                    case 10: // Agregar nuevo producto
                        Producto p = daoProducto.agregarProductoPorConsola(scan);
                        daoProducto.insertar(p);
                        break;

                    case 11: // Modificar producto
                        daoProducto.modificarProductoPorId(scan);
                        break;

                    case 12: // Eliminar producto
                        daoProducto.eliminarProductoPorId(scan);
                        break;

                    case 13: // Buscar producto por nombre o código
                        daoProducto.buscarProductoPorNombreOCodigo(scan);
                        break;

                    case 14: // Reportes (totales, por fecha, por cliente)
                        MetodosSwitch.mostrarReportes(scan);
                        break;

                    case 15: // Abrir interfaz gráfica
                        MetodosSwitch.abrirApp();
                        break;

                    case 16:
                        System.out.println("Saliendo del sistema...");
                        break;
                }
            }catch(Exception e){
                System.out.println("Opcion no permitida, solo numeros");
            }
        }while(opcion != 16);

    }
}