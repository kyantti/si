package es.unex.cum.si.practica;

import java.util.Random;

public class Test {
    static int[] chromosome = {8, 1, 20, 2, 29, 3, 15, 4, 19, 5};
    public void mutate2() {
        Random random = new Random();

        // Elegir un punto aleatorio impar para cortar el cromosoma
        int cutPoint = (random.nextInt(chromosome.length / 2) * 2) + 1;
        System.out.println("cutPoint: " + cutPoint);

        // Dar vuelta a la parte del cromosoma después del punto de corte
        for (int i = cutPoint, j = chromosome.length - 1; i < j; i++, j--) {
            int temp = chromosome[i];
            chromosome[i] = chromosome[j];
            chromosome[j] = temp;
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.mutate2();
        for (int i = 0; i < chromosome.length; i++) {
            System.out.print(chromosome[i] + ", ");
        }
    }

}
