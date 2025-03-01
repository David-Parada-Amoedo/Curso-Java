public class Ciclos {
    public static void main(String[] args) {
        // Sumar los primeros 5 numeros
        final var MAXIMO = 5;
        var acumuladorSuma = 0;

        // Iterar los valores
        for(var numero = 1; numero <= MAXIMO; numero++){
            // Imprimir lo que se va a sumar
            System.out.println("(acumuladorSuma + numero) ->" + acumuladorSuma + " + " + numero);
            acumuladorSuma += numero;
            // Imprimir el resultado parcial
            System.out.println("Suma parcial acumulada: " + acumuladorSuma);
        }
        System.out.println("Suma de los primeros 5 numeros: " + acumuladorSuma);
    }
}
