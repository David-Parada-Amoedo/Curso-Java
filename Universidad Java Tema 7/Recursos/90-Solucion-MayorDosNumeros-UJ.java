import java.util.Scanner;

public class MayorDeDosNumeros {
    public static void main(String[] args) {
        System.out.println("*** El mayor de dos números ***");

        var consola = new Scanner(System.in);

        System.out.print("Proporciona el número 1: ");
        var numero1 = Integer.parseInt(consola.nextLine());

        System.out.print("Proporciona el número 2: ");
        var numero2 = Integer.parseInt(consola.nextLine());

        // El mayor de dos números
        if(numero1 > numero2)
            System.out.println("El número 1 es mayor: " + numero1);
        else
            System.out.println("El número 2 es mayor: " + numero2);
    }
}
