import java.util.Scanner;

public class EstacionAnio {
    public static void main(String[] args) {
        System.out.println("*** Estación del Año ***");

        var consola = new Scanner(System.in);

        System.out.print("Proporciona el valor del mes (1-12): ");
        var mes = Integer.parseInt(consola.nextLine());

        // Revision del mes usando la sintanxis switch mejorada
        var estacion = switch(mes){
            case 1, 2, 12 -> "Invierno";
            case 3, 4, 5 -> "Primavera";
            case 6, 7, 8 -> "Verano";
            case 9, 10, 11 -> "Otoño";
            default -> "Estación desconcida";
        };

        // Imprimimos el resultado
        System.out.printf("La estación para el mes %d es %s", mes, estacion);
    }
}
