package es.unex.cum.si.restos;

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
        int totalDias = 5; // Número total de días en la semana
        int periodosPorDia = 6; // Número de periodos por día
        int totalPeriodos = totalDias * periodosPorDia; // Número total de periodos en la semana

        // Supongamos que tienes un time id específico
        int timeId = 12;

        // Calcular el día y el periodo
        int dia = (timeId / periodosPorDia) % totalDias;
        int periodo = timeId % periodosPorDia;

        // Imprimir resultados
        System.out.println("El time id " + timeId + " corresponde al día " + dia + " y al periodo " + periodo);
    }

}
