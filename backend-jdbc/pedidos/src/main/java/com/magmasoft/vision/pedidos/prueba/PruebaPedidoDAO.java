package com.magmasoft.vision.pedidos.prueba;

import com.magmasoft.vision.pedidos.dao.PedidoDAO;
import com.magmasoft.vision.pedidos.modelo.Pedido;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Clase de prueba manual del PedidoDAO. Ejecuta, en orden, las cuatro
 * operaciones CRUD contra la base de datos y muestra el resultado de
 * cada una por consola.
 *
 * Evidencia: GA7-220501096-AA2-EV01
 */
public class PruebaPedidoDAO {

    public static void main(String[] args) {
        PedidoDAO pedidoDAO = new PedidoDAO();

        try {
            System.out.println("== 1. CONSULTAR TODOS (estado inicial) ==");
            List<Pedido> pedidosIniciales = pedidoDAO.consultarTodos();
            pedidosIniciales.forEach(System.out::println);

            System.out.println("\n== 2. INSERTAR nuevo pedido ==");
            Pedido nuevoPedido = new Pedido(
                    "Jorge Ramirez",
                    "318-555-0606",
                    "jorge.ramirez@correo.com",
                    "PROGRESIVO",
                    "Dra. Martinez",
                    LocalDate.of(2026, 9, 25),
                    "EN_PROCESO"
            );
            int idGenerado = pedidoDAO.insertar(nuevoPedido);
            System.out.println("Pedido insertado con id = " + idGenerado);

            System.out.println("\n== 3. CONSULTAR POR ID ==");
            Optional<Pedido> pedidoConsultado = pedidoDAO.consultarPorId(idGenerado);
            pedidoConsultado.ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("No se encontro el pedido.")
            );

            System.out.println("\n== 4. ACTUALIZAR pedido ==");
            Pedido pedidoAActualizar = pedidoConsultado.orElseThrow();
            pedidoAActualizar.setEstado("LISTO");
            pedidoAActualizar.setMedicoTratante("Dra. Martinez (control)");
            boolean actualizado = pedidoDAO.actualizar(pedidoAActualizar);
            System.out.println("Actualizacion exitosa: " + actualizado);
            System.out.println("Pedido actualizado: " + pedidoDAO.consultarPorId(idGenerado).orElseThrow());

            System.out.println("\n== 5. CONSULTAR POR ESTADO (LISTO) ==");
            List<Pedido> pedidosListos = pedidoDAO.consultarPorEstado("LISTO");
            pedidosListos.forEach(System.out::println);

            System.out.println("\n== 6. ELIMINAR pedido de prueba ==");
            boolean eliminado = pedidoDAO.eliminar(idGenerado);
            System.out.println("Eliminacion exitosa: " + eliminado);

            System.out.println("\n== 7. CONSULTAR TODOS (estado final) ==");
            List<Pedido> pedidosFinales = pedidoDAO.consultarTodos();
            pedidosFinales.forEach(System.out::println);
            System.out.println("\nTotal de pedidos al finalizar: " + pedidosFinales.size()
                    + " (igual al inicial, ya que el pedido de prueba fue eliminado)");

        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }
}
