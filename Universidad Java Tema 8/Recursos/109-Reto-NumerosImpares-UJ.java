public class NumerosImpares {
    public static void main(String[] args) {
        System.out.println("*** Números Impares ***");
        var contador = 0;
        do{
            // Revisamos si es número impar
            if(contador % 2 != 0)
                System.out.print(contador + " ");
            contador++;
        } while(contador <= 20);
    }
}
