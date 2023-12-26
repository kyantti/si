package es.unex.cum.si.practica.model.genotype;

import es.unex.cum.si.practica.model.fenotype.Individual;
import es.unex.cum.si.practica.model.util.Data;
import java.util.Arrays;
import java.util.Comparator;

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

    public int calcTimeGaps() {
        int timeGaps = 0;
        Class[] sortedClasses = Arrays.copyOf(classes, classes.length);
        Arrays.sort(sortedClasses, Comparator.comparingInt(Class::groupId).thenComparingInt(Class::timeId));

        for (int i = 0; i < sortedClasses.length; i++) {
            for (int j = i + 1; j < sortedClasses.length; j++) {
                if (sortedClasses[i].groupId() == sortedClasses[j].groupId() && (Math.abs(sortedClasses[i].timeId() - sortedClasses[j].timeId()) > 1)) {
                    timeGaps += Math.abs(sortedClasses[i].timeId() - sortedClasses[j].timeId());
                }
            }
        }
        return timeGaps;
    }

    public int calcQuality() {
        int quality = 0;
        // If a group has more than 3 classes in a row, the quality is increased by 1
        //Sort classes by time slot id (lower to higher)
        Class[] aClasses = Arrays.copyOf(classes, classes.length);
        Class[] bClasses = Arrays.copyOf(classes, classes.length);
        Arrays.sort(aClasses, Comparator.comparingInt(Class::timeId));
        Arrays.sort(aClasses, Comparator.comparingInt(Class::groupId));

        //Sort classes by group id (lower to higher)
        Arrays.sort(bClasses, Comparator.comparingInt(Class::groupId));
        Arrays.sort(bClasses, Comparator.comparingInt(Class::groupId));
        for (Class classA : aClasses) {
            int count = 0;
            for (Class classB : bClasses) {
                if (classA.groupId() == classB.groupId() && (Math.abs(classA.timeId() - classB.timeId()) == 1)) {
                    count++;
                    if (count > 3) {
                        quality++;
                    }
                }
            }
        }
        return quality;
    }


}
