import java.util.Scanner;

public class CalculoAreaRectangulo {
    public static void main(String[] args) {
        System.out.println("*** Cálculo del Área de un Rectángulo ***");

        var consola = new Scanner(System.in);

        System.out.print("Proporciona la base: ");
        var base = Integer.parseInt(consola.nextLine());

        System.out.print("Proporciona la altura: ");
        var altura = Integer.parseInt(consola.nextLine());

        // Realizamos el calculo del area
        var areaRectangulo = base * altura;
        System.out.println("Área del rectángulo: " + areaRectangulo);

        // Realizamos el cálculo del perímetro
        var perimetroRectangulo = (base + altura) * 2;
        System.out.println("Perímetro del rectángulo: " + perimetroRectangulo);
    }
}
