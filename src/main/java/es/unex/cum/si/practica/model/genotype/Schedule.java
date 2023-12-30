package es.unex.cum.si.practica.model.genotype;

import es.unex.cum.si.practica.model.fenotype.Individual;
import es.unex.cum.si.practica.model.util.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Schedule {
    private Class[] classes;

    public Schedule(int numOfClasses) {
        classes = new Class[numOfClasses];
    }

    public Schedule(Schedule cloneable) {
        this.classes = cloneable.getClasses();
    }

    public void parseChromosome(Individual individual) {
        // Each subject has 2 classes per week (4 hours per week)
        int[] chromosome = individual.getChromosome();
        int chromosomePos = 0;
        int classIndex = 0;
        int timeId;
        int roomId;

        for (Group group : Data.getInstance().getGroups().values()) {
            int[] subjectIds = group.subjectIds();
            for (int subjectId : subjectIds) {
                // First assignment
                timeId = chromosome[chromosomePos++];
                roomId = chromosome[chromosomePos++];
                classes[classIndex++] = new Class(classIndex, group.id(), timeId, roomId, subjectId);
                // Second assignment
                timeId = chromosome[chromosomePos++];
                roomId = chromosome[chromosomePos++];
                classes[classIndex++] = new Class(classIndex, group.id(), timeId, roomId, subjectId);
            }
        }
    }

    public Class[] getClasses() {
        return classes;
    }

    public int calcConflicts() {
        int conflicts = 0;
        for (int i = 0; i < classes.length; i++) {
            for (int j = i + 1; j < classes.length; j++) {
                if (classes[i].roomId() == classes[j].roomId() && classes[i].timeId() == classes[j].timeId()) {
                    conflicts++;
                    break;
                }
            }
            for (int j = i + 1; j < classes.length; j++) {
                if (classes[i].groupId() == classes[j].groupId() && classes[i].timeId() == classes[j].timeId()) {
                    conflicts++;
                    break;
                }
            }
        }
        return conflicts;
    }

    public int calcQuality() {
        int totalDays = Data.getInstance().getNumDays();
        int totalGroups = Data.getInstance().getGroups().size();
        int totalPeriods = Data.getInstance().getNumPeriods();
        List<Integer>[][] timeIdsPerGroupPerDay = new ArrayList[totalGroups][totalDays];
        int groupId;
        int timeId;
        int day;
        int penalty = 0;

        for (Class scheduleClass : classes) {
            groupId = scheduleClass.groupId();
            timeId = scheduleClass.timeId();

            // Obtener el día correspondiente al timeId
            day = (timeId / totalPeriods) % totalDays;

            // Inicializar la lista si es necesario
            if (timeIdsPerGroupPerDay[groupId][day] == null) {
                timeIdsPerGroupPerDay[groupId][day] = new ArrayList<>();
            }

            // Agregar el timeId a la lista
            timeIdsPerGroupPerDay[groupId][day].add(timeId);
        }

        // Penalizar más de 3 clases por día
        for (int i = 0; i < totalGroups; i++) {
            for (int j = 0; j < totalDays; j++) {
                if (timeIdsPerGroupPerDay[i][j] != null && timeIdsPerGroupPerDay[i][j].size() > 3) {
                    penalty = (penalty + 1) * 2;
                }
            }
        }

        // Penalizar no consecutividad de clases
        for (int i = 0; i < totalGroups; i++) {
            for (int j = 0; j < totalDays; j++) {
                List<Integer> timeIds = timeIdsPerGroupPerDay[i][j];
                if (timeIds != null) {
                    timeIds.sort(Comparator.naturalOrder());
                    if (timeIds.get(0) % 3 != 0){
                        penalty++;
                    }
                    for (int k = 0; k < timeIds.size() - 1; k++) {
                        int currentClassId = timeIds.get(k);
                        int nextClassId = timeIds.get(k + 1);
                        if (nextClassId - currentClassId > 1) {
                            penalty += nextClassId - currentClassId;
                        }
                    }
                }
            }
        }

        return penalty;
    }

}
