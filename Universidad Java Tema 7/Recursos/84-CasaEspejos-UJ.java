import java.util.Scanner;

public class CasaDeLosEspejos {
    public static void main(String[] args) {
        System.out.println("*** Bienvenidos a la Casa de los Espejos ***");

        var consola = new Scanner(System.in);

        System.out.print("Cuál es tu edad? ");
        var edad = Integer.parseInt(consola.nextLine());

        System.out.print("Tienes miedo a la oscuridad (true/false)? ");
        var tienesMiedoOscuridad = Boolean.parseBoolean(consola.nextLine());

        // Verificacion
        if(!tienesMiedoOscuridad && edad >= 10){
            System.out.println("Puedes entrar a la Casa de los Espejos");
        }
        else{
            System.out.println("Lo siento, la Casa de los Espejos podría darte miedo");
        }
    }
}
