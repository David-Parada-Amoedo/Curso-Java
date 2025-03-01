import java.util.Scanner;

public class SistemaAutenticacion {
    public static void main(String[] args) {
        System.out.println("*** Sistema de Autenticación ***");

        final var USUARIO_VALIDO = "admin";
        final var PASSWORD_VALIDO = "123";

        var consola = new Scanner(System.in);

        System.out.print("Cuál es tu usuario? ");
        var usuarioIngresado = consola.nextLine();

        System.out.print("Cuál es tu password? ");
        var passwordIngresado = consola.nextLine();

        var sonDatosCorrectos = usuarioIngresado.equals(USUARIO_VALIDO)
                && passwordIngresado.equals(PASSWORD_VALIDO);

        System.out.println("Datos son correctos? " + sonDatosCorrectos);
    }
}
