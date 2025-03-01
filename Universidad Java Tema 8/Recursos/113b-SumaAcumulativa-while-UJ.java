public class SumaAcumulativa {
    public static void main(String[] args) {
        // Suma Acumulativa (Iterativa)
        final var MAXIMO = 10;
        var acumuladorSuma = 0;
        // Iterar los valores
        var numero = 1;
        while(numero <= MAXIMO){
            // Imprimir lo que se va a sumar
            System.out.println("(acumuladorSuma + numero) -> " + acumuladorSuma + " + " + numero);
            acumuladorSuma += numero++; // acumularSuma = acumuladorSuma + numero
            System.out.println("Suma parcial acumulada: " + acumuladorSuma + "\n");
        }
        System.out.println("Suma de los primeros " + MAXIMO + " números: " + acumuladorSuma);
    }
}
