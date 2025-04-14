/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package exp1_s5_francisco_henriquez;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author usuario
 */

public class Exp1_s5_francisco_henriquez {
    
    // Declaracion de precios
    static final double precioVIP = 30000;
    static final double precioPlateaBaja = 15000;
    static final double precioPlateaAlta = 18000;
    static final double precioPalcos = 13000;
    static final double IVA = 0.19;

    // Declaracion variables globales
    static int      totalEntradasVendidas = 0;
    static double   ingresosTotales = 0;
    static int      cantidadEstudiantes = 0;

    // Declaracion de lista que almacena todas las entradas vendidas
    static ArrayList<Entrada> entradasVendidas = new ArrayList<>();
    static int contadorEntradas = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char[][] teatro = inicializarTeatro();
        boolean continuarSistema = true;

        while (continuarSistema) {
            int opcionMenu = mostrarMenu(sc);

            switch (opcionMenu) {
                case 1:
                    procesarVenta(teatro, sc);        
                    continuarSistema = deseaOtraOperacion(sc); 
                    if (!continuarSistema){
                        mostrarResumenFinal();
                    }                    
                    break;
                case 2:
                    mostrarPromociones();
                    continuarSistema = deseaOtraOperacion(sc);  
                    break;
                case 3:
                    buscarEntradas(sc);
                    continuarSistema = deseaOtraOperacion(sc);  
                    break;
                case 4:
                    eliminarEntrada(sc);
                    continuarSistema = deseaOtraOperacion(sc);  
                    break;
                case 5:
                    mostrarResumenFinal();
                    System.out.println("\nGracias por visitar el Teatro Moro. Hasta pronto!");
                    continuarSistema = false;
                    break;
            }
        }
        sc.close();
    }

    // Metodo auxiliar - muestra el menú principal con las opciones disponibles.
    static int mostrarMenu(Scanner sc) {
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("                  Bienvenido al Teatro Moro                  ");
        System.out.println("-------------------------------------------------------------");
        System.out.println("\n1. Comprar entrada");
        System.out.println("2. Ver promociones");
        System.out.println("3. Buscar entrada");
        System.out.println("4. Eliminar entrada");
        System.out.println("5. Salir");
        return solicitarEntero(sc, "Seleccione una opcion: ", 1, 5);
    }

    // Metodo auxiliar - procesa la venta de entradas.
    static void procesarVenta(char[][] teatro, Scanner sc) {
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("                  Sistema de Venta Entradas                  ");
        System.out.println("-------------------------------------------------------------");       
        
        mostrarPlanoTeatro(teatro);

        int[] seleccion = seleccionarAsiento(teatro, sc);
        int filaSeleccionada = seleccion[0];
        int asientoSeleccionado = seleccion[1];

        teatro[filaSeleccionada][asientoSeleccionado] = 'X';
        double precioBase = obtenerPrecioEntrada(filaSeleccionada);
        String tipoEntrada = obtenerTipoEntrada(filaSeleccionada);

        int edad = solicitarEntero(sc, "Ingrese su edad: ", 1, 120);
        boolean esEstudiante = false;
        int opcionEstudiante = solicitarEntero(sc, "¿Es estudiante? (1.Si / 2.No): ", 1, 2);
        if (opcionEstudiante == 1) {
            esEstudiante = true;
        } else {
            esEstudiante = false;
        }

        String tarifa = determinarTipoTarifa(esEstudiante);
        boolean esAdultoMayor = edad >= 60;

        double descuento = calcularDescuento(precioBase, esEstudiante, esAdultoMayor);
        double totalPagar = precioBase - descuento;
        double valorNeto = totalPagar / (1 + IVA);
        double valorIVA = valorNeto * IVA;
        
        imprimirResumenCompra(tipoEntrada, filaSeleccionada, asientoSeleccionado, precioBase, descuento, 
                                        totalPagar, valorNeto, valorIVA);

        if (solicitarEntero(sc, "¿Desea confirmar la compra? (1.Si / 2.No): ", 1, 2) == 1) {
            Entrada entrada = new Entrada(contadorEntradas++, tipoEntrada, filaSeleccionada, asientoSeleccionado, totalPagar, esEstudiante, esAdultoMayor);
            entradasVendidas.add(entrada);
            totalEntradasVendidas++;
            ingresosTotales += totalPagar;
            if (esEstudiante) {
                cantidadEstudiantes++;
            }
            imprimirBoleta(tipoEntrada, tarifa, entrada.numeroEntrada, filaSeleccionada, asientoSeleccionado,precioBase, descuento, totalPagar, valorNeto, valorIVA);
        } else {
            System.out.println("\nCompra cancelada.");
            teatro[filaSeleccionada][asientoSeleccionado] = 'O';
        }      
    }

    // Metodo auxiliar - muestra informacion de las promociones vigentes.
    static void mostrarPromociones() {
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("            Sistema de Informacion de Promociones              ");
        System.out.println("-------------------------------------------------------------");  
        System.out.println("\n- 10% de descuento para estudiantes.");
        System.out.println("- 15% de descuento para adultos mayores (60+ años).");
        System.out.println("- 25% de descuento combinando ambas condiciones.");
        System.out.println("- Comprando 5 entradas o mas, 5% adicional en cada una (en desarrollo, no disponible, por favor solicite directamente en caja).");
    }

    // Metodo auxiliar - busca informacion de las entradas vendidas registradas por el sistema.
    static void buscarEntradas(Scanner sc) {
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("                 Sistema de Busqueda Entradas                  ");
        System.out.println("-------------------------------------------------------------");   
        System.out.println("\nBuscar entradas por:");
        System.out.println("1. Numero de entrada");
        System.out.println("2. Tipo de entrada (en desarrollo, no disponible)");
        int opcion = solicitarEntero(sc, "Seleccione opcion: ", 1, 1);

        if (opcion == 1) {
            int numero = solicitarEntero(sc, "Ingrese el numero de entrada a buscar informacion: ", 1, contadorEntradas);
            for (int i = 0; i < entradasVendidas.size(); i++) {
                Entrada entrada = entradasVendidas.get(i);
                if (entrada.numeroEntrada == numero) {
                System.out.println("\n-------------------------------------------------------------");
                System.out.println("                 Informacion de la Entrada                  ");
                System.out.println("-------------------------------------------------------------");  
                System.out.println("\nEntrada #" + entrada.numeroEntrada);
                System.out.println("Tipo: " + entrada.tipoEntrada);
                System.out.println("Fila: " + entrada.filaSeleccionada);
                System.out.println("Asiento: " + entrada.asientoSeleccionado);
                System.out.println("Total: $" + entrada.totalPagar);
                return;
                }
            }
            System.out.println("\nEntrada no encontrada.");
        } 
    }

    // Metodo auxiliar - elimina entradas vendidas registradas por el sistema.
    static void eliminarEntrada(Scanner sc) {
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("             Sistema de Eliminacion de Entradas                  ");
        System.out.println("-------------------------------------------------------------");  
        int numero = solicitarEntero(sc, "Ingrese numero de entrada a eliminar: ", 1, contadorEntradas);
        Entrada encontrada = null;
        for (int i = 0; i < entradasVendidas.size(); i++) {
            Entrada entrada = entradasVendidas.get(i);
            if (entrada.numeroEntrada == numero) {
                encontrada = entrada;
                break;
            }
        }
        if (encontrada != null) {
            entradasVendidas.remove(encontrada);
            totalEntradasVendidas--;
            ingresosTotales -= encontrada.totalPagar;
            if (encontrada.estudiante){
            cantidadEstudiantes --;
            }
            System.out.println("\n-------------------------------------------------------------");
            System.out.println("                 Entrada Eliminada Exitosamente             ");
            System.out.println("-------------------------------------------------------------");  
            System.out.println("Entrada #" + encontrada.numeroEntrada);
            System.out.println("Tipo: " + encontrada.tipoEntrada);
            System.out.println("Fila: " + encontrada.filaSeleccionada);
            System.out.println("Asiento: " + encontrada.asientoSeleccionado);
            System.out.printf("Total: $%.0f\n", encontrada.totalPagar);
            System.out.println("-------------------------------------------------------------");
        } else {
            System.out.println("Entrada no encontrada.");
        }
    }

    // Metodo auxiliar - inicializa el arreglo para el plano: 7 filas, 5 asientos, libres.
    static char[][] inicializarTeatro() {
        char[][] teatro = new char[7][5];
        for (int i = 0; i < teatro.length; i++) {
            for (int c = 0; c < teatro[i].length; c++) {
                // Asignar O para asiento libre
                teatro[i][c] = 'O';
            }
        }
        return teatro;
    }

    // Metodo auxiliar - seleccion de asiento, realizando validacion de ocupado.
    static int[] seleccionarAsiento(char[][] teatro, Scanner sc) {
        int fila = -1, asiento =  -1;
        boolean asientoLibre = false;
        
        while (!asientoLibre) {
            fila = solicitarEntero(sc, "Ingrese fila (0-6): ", 0, 6);
            asiento = solicitarEntero(sc, "Ingrese asiento (0-4): ", 0, 4);
            
            if (teatro[fila][asiento] == 'O') {
                asientoLibre = true;
            } else {
            System.out.println("Asiento ocupado. Intente con otro.");
            }
        }
        return new int[] {fila, asiento};
    }

    // Metodo auxiliar - asignacion del precio de la entrada segun fila seleccionada.
    static double obtenerPrecioEntrada(int filaSeleccionada) {
        switch (filaSeleccionada){
            case 0:
                return precioVIP;
            case 1:
            case 2:
            case 3:
                return precioPalcos;
            case 4:
            case 5: 
                return precioPlateaBaja;
            case 6:
                return precioPlateaAlta;  
            default:
                return 0;
        }
    }
    
    // Metodo auxiliar - asignacion del tipo de entrada segun fila seleccionada.
    static String obtenerTipoEntrada(int filaSeleccionada) {
        switch (filaSeleccionada){
            case 0: 
                return "VIP";
            case 1:
            case 2:
            case 3:
                return "Palco";
            case 4:
            case 5: 
                return "Platea Baja";
            case 6: 
                return "Platea Alta";
            default:
                return "";
        }
    }

    // Metodo auxiliar - asignacion del tipo de tarifa si es estudiante o publico general.
    static String determinarTipoTarifa (boolean esEstudiante){
        if (esEstudiante){
        return "Estudiante";
        } else{
        return "Publico General"; 
        }     
    }
    
    // Metodo auxiliar - calcular descuento a aplicar.
    static double calcularDescuento(double precioBase, boolean estudiante, boolean adultoMayor) {
        if (estudiante && adultoMayor) {
            System.out.println("\nSe aplicara un 25% de descuento por ser adulto mayor y estudiante. ¡Felicitaciones! ¡Tu puedes alcanzar tu meta!");
            return precioBase * 0.25;
        }
        else if (adultoMayor) {
        System.out.println("\nSe aplicara un 15% de descuento por tercera edad.");           
            return precioBase * 0.15;
        }
        else if (estudiante) {
            System.out.println("\nSe aplicara un 10% de descuento por ser estudiante.");           
            return precioBase * 0.10;
        }
        else return 0;
    }

    // Metodo auxiliar - imprimir resumen de compra.    
    static void imprimirResumenCompra(String tipoEntrada, int filaSeleccionada, int asientoSeleccionado, double precioBase, double descuento, 
                                        double totalPagar, double valorNeto, double valorIVA){
        System.out.println("\n        RESUMEN DE COMPRA");
        System.out.println("-----------------------------------");
        System.out.println("Tipo de entrada: " + tipoEntrada);
        System.out.printf("Ubicacion: Fila %d, Asiento %d\n", filaSeleccionada, asientoSeleccionado);
        System.out.printf("Precio base: $%.0f\n", precioBase);
        System.out.printf("Descuento aplicado: $%.0f\n", descuento);
        System.out.printf("Total a pagar: $%.0f\n", totalPagar);
        System.out.printf("Valor neto (sin IVA): $%.0f\n", valorNeto);
        System.out.printf("IVA (19%%): $%.0f\n", valorIVA);
        System.out.println("-----------------------------------");        
    }
    
     // Metodo auxiliar - confirmacion de realizar otra operacion.    
    static boolean deseaOtraOperacion(Scanner sc){
        int opcion = solicitarEntero(sc, "¿Desea realizar otra operacion? (1. Si / 2. No): ", 1, 2);
        if (opcion == 1){
            return true;
        }else{
            return false;
        }
    }

    // Metodo auxiliar - validacion de datos y limites ingresados por el cliente.    
    static int solicitarEntero(Scanner sc, String mensaje, int min, int max) {
        int valor = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print("\n"+mensaje);
            String entrada = sc.nextLine().trim();
            try {
                valor = Integer.parseInt(entrada);
                if (valor >= min && valor <= max){ 
                    valido = true;
                } else {
                    System.err.println("Entrada invalida. Intente nuevamente.");
                }
            } catch (NumberFormatException  e) {
                System.err.println("Entrada invalida. Intente nuevamente.");
            }
        }
        return valor;
    }

    // Metodo auxiliar - imprimir plano del teatro.    
    static void mostrarPlanoTeatro(char[][] teatro) {
        System.out.println("\nPLANO DEL TEATRO (O = libre, X = ocupado):\n");
        System.out.println("                                Asiento");
        System.out.println("                               0 1 2 3 4");
        for (int fila = 0; fila < teatro.length; fila++) {
            String zonaNombre = "";
            if (fila <= 0) zonaNombre = "Fila " + fila + ": Zona A - VIP        ";       // VIP 
            else if (fila <= 3) zonaNombre = "Fila " + fila + ": Zona B - Palco      ";  // Palco 
            else if (fila <= 5) zonaNombre = "Fila " + fila + ": Zona C - Platea Baja";  // Platea Baja 
            else zonaNombre = "Fila " + fila  + ": Zona D - Platea Alta"; // Plantea Alta 

            System.out.print(zonaNombre + " | ");
            for (int asiento = 0; asiento < teatro[fila].length; asiento++) {
                System.out.print(teatro[fila][asiento] + " ");
            }
            System.out.println();
        }
    }
    
    // Metodo auxiliar - imprimir boleta de compra.    
    static void imprimirBoleta(String tipoEntrada, String tarifa, int numeroEntrada, int filaSeleccionada, int asientoSeleccionado,
                                double precioBase, double descuento, double totalPagar,
                                double valorNeto, double valorIVA) {
    System.out.println("\nPago realizado con exito. Gracias por su compra\n");
    System.out.println("-------------------------------------------------------------");
    System.out.println("                        Teatro Moro                            ");
    System.out.println("-------------------------------------------------------------");
    System.out.println("                      Boleta de Venta                          ");
    System.out.println("-------------------------------------------------------------");
    System.out.println("");
    System.out.println("Tipo de entrada: " + tipoEntrada);
    System.out.println("Tarifa: " + tarifa);
    System.out.println("Numero de entrada: " + numeroEntrada);
    System.out.printf("Ubicacion: Fila %d, Asiento %d\n", filaSeleccionada, asientoSeleccionado);
    System.out.printf("Precio base tarifa: $%.0f\n", precioBase);
    System.out.printf("Descuento aplicado: $%.0f\n", descuento);
    System.out.printf("Total a pagar: $%.0f\n", totalPagar);
    System.out.printf("Valor neto (sin IVA): $%.0f\n", valorNeto);
    System.out.printf("IVA incluido (19%%): $%.0f\n", valorIVA);
    System.out.println("");
    System.out.println("-------------------------------------------------------------");
    System.out.println("         Gracias por su compra, disfrute la funcion          ");
    System.out.println("-------------------------------------------------------------");
    }

    // Metodo auxiliar - imprimir resumen final - termino de ejecucion del programa.    
    static void mostrarResumenFinal() {
    System.out.println("\n==================== RESUMEN FINAL ====================");
    System.out.println("Entradas vendidas: " + totalEntradasVendidas);
    System.out.printf("Total recaudado: $%.0f\n", ingresosTotales);
    System.out.println("Cantidad de estudiantes: " + cantidadEstudiantes);
    System.out.println("======================================================");
    }
}

// Clase Entrada representa cada entrada vendida
class Entrada {
    int numeroEntrada;
    String tipoEntrada;
    int filaSeleccionada;
    int asientoSeleccionado;
    double totalPagar;
    boolean estudiante;
    boolean adultoMayor;

    Entrada(int numeroEntrada, String tipoEntrada, int filaSeleccionada, int asientoSeleccionado, double totalPagar, boolean estudiante, boolean adultoMayor) {
        this.numeroEntrada = numeroEntrada;
        this.tipoEntrada = tipoEntrada;
        this.filaSeleccionada = filaSeleccionada;
        this.asientoSeleccionado = asientoSeleccionado;
        this.totalPagar = totalPagar;
        this.estudiante = estudiante;
        this.adultoMayor = adultoMayor;
    }
}
