package es.unex.cum.si.practica.model.phenotype;

import es.unex.cum.si.practica.model.genotype.Individual;
import es.unex.cum.si.practica.model.util.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The class Schedule stores the classes of a schedule. This is a translation of the chromosome to a genotype, so it can be easily evaluated.
 */

public class Schedule {
    private final Class[] classes;

    /**
     * Constructs a schedule with a specified number of classes.
     *
     * @param numOfClasses The number of classes in the schedule.
     */
    public Schedule(int numOfClasses) {
        classes = new Class[numOfClasses];
    }

    /**
     * Constructs a schedule by cloning another schedule.
     *
     * @param cloneable The schedule to clone.
     */
    public Schedule(Schedule cloneable) {
        this.classes = cloneable.getClasses();
    }

    /**
     * Translates the chromosome of an individual to a schedule.
     *
     * @param individual The individual with a chromosome to parse.
     */
    public void parseChromosome(Individual individual) {
        // Each subject has 2 classes per week (4 hours per week)
        int[] chromosome = individual.getChromosome();
        int i = 0;
        int j = 0;
        int timeId;
        int roomId;

        for (Group group : Data.getInstance().getGroups().values()) {
            int[] subjectIds = group.subjectIds();
            for (int subjectId : subjectIds) {
                // +2h
                timeId = chromosome[i++];
                roomId = chromosome[i++];
                classes[j++] = new Class(j, group.id(), timeId, roomId, subjectId);
                // +2h
                timeId = chromosome[i++];
                roomId = chromosome[i++];
                classes[j++] = new Class(j, group.id(), timeId, roomId, subjectId);
            }
        }
    }

    /**
     * Gets the array of classes representing the schedule.
     *
     * @return The array of classes.
     */
    public Class[] getClasses() {
        return classes;
    }

    /**
     * Calculates the number of conflicts in the schedule.
     *
     * @return The number of conflicts.
     */
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

    /**
     * Calculates the quality of the schedule based on:
     * - The number of days with more than 3 classes.
     * - The number of classes that are not consecutive.
     * - The number of classes that are not in the first period of the morning or the first period of the afternoon.
     *
     * @return The quality score of the schedule.
     */
    public int calcPenalties() {
        int groupId;
        int timeId;
        int day;
        int totalPeriods = Data.getInstance().getNumPeriods();
        int totalDays = Data.getInstance().getNumDays();
        int totalGroups = Data.getInstance().getGroups().size();
        List<Integer>[][] timeIdsPerGroupPerDay = new ArrayList[totalGroups][totalDays];
        int i;
        int j;
        int penalty = 0;
        int currentClassId;
        int nextClassId;
        int timeGap;

        for (Class scheduleClass : classes) {
            groupId = scheduleClass.groupId();
            timeId = scheduleClass.timeId();

            day = (timeId / totalPeriods) % totalDays;

            if (timeIdsPerGroupPerDay[groupId][day] == null) {
                timeIdsPerGroupPerDay[groupId][day] = new ArrayList<>();
            }

            timeIdsPerGroupPerDay[groupId][day].add(timeId);
        }

        for (i = 0; i < totalGroups; i++) {
            for (j = 0; j < totalDays; j++) {
                List<Integer> timeIds = timeIdsPerGroupPerDay[i][j];
                if (timeIds != null) {
                    // Penalize if there are more than 3 classes in a day
                    if (timeIds.size() > 3){
                        penalty += 2;
                    }
                    timeIds.sort(Comparator.naturalOrder());
                    // Penalize if the first class is not in the first period of the morning or the first period of the afternoon
                    if (timeIds.get(0) % 3 != 0){
                        penalty += 1;
                    }
                    for (int k = 0; k < timeIds.size() - 1; k++) {
                        currentClassId = timeIds.get(k);
                        nextClassId = timeIds.get(k + 1);
                        timeGap = nextClassId - currentClassId;
                        // Penalize if the classes are not consecutive
                        if (timeGap > 1) {
                            penalty += timeGap;
                        }
                    }
                }
            }
        }

        return penalty;
    }

}
