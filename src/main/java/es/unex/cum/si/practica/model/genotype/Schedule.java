package es.unex.cum.si.practica.model.genotype;

import es.unex.cum.si.practica.model.fenotype.Individual;
import es.unex.cum.si.practica.model.util.Data;

import java.util.Arrays;
import java.util.Comparator;

public class Schedule {
    private Class[] classes;
    public Schedule(){
        // Have 2 classes per subject
        classes = new Class[Data.getInstance().getSubjects().size() * 2];
    }
    public Schedule(Schedule cloneable) {
        this.classes = cloneable.getClasses();
    }
    public void parseChromosome(Individual individual){
        // Each subject has 2 classes per week (4 hours per week)
        int[] chromosome = individual.getChromosome();
        int chromosomePos = 0;
        int classIndex = 0;

        int timeId = 0;
        int roomId = 0;

        for (Group group : Data.getInstance().getGroups().values()) {
            int[] subjectIds = group.getSubjectIds();
            for (int subjectId : subjectIds) {
                // First assignment
                timeId = chromosome[chromosomePos];
                chromosomePos++;
                roomId = chromosome[chromosomePos];
                chromosomePos++;

                classes[classIndex] = new Class(classIndex, group.getId(), timeId, roomId, subjectId);
                classIndex++;

                // Second assignment
                timeId = chromosome[chromosomePos];
                chromosomePos++;
                roomId = chromosome[chromosomePos];
                chromosomePos++;

                classes[classIndex] = new Class(classIndex, group.getId(), timeId, roomId, subjectId);
                classIndex++;
            }
        }
    }

    public Class[] getClasses(){
        return classes;
    }

    public int calcConflicts(){
        int conflicts = 0;
        for (Class classA : classes) {
            // Check if room is taken
            for (Class classB : classes) {
                if (classA.getRoomId() == classB.getRoomId() && classA.getTimeId() == classB.getTimeId() && classA.getClassId() != classB.getClassId()) {
                    conflicts++;
                    break;
                }
            }
            //Check if a group have more than one class at the same time
            for (Class classB : classes) {
                if (classA.getGroupId() == classB.getGroupId() && classA.getTimeId() == classB.getTimeId() && classA.getClassId() != classB.getClassId()) {
                    conflicts++;
                    break;
                }
            }

        }
        return conflicts;
    }

    public int calcTimeGaps(){
        int timeGaps = 0;
        Class[] aClasses = Arrays.copyOf(classes, classes.length);
        Class[] bClasses = Arrays.copyOf(classes, classes.length);
        //Sort classes by time slot id (lower to higher)
        Arrays.sort(aClasses, Comparator.comparingInt(Class::getTimeId));
        Arrays.sort(aClasses, Comparator.comparingInt(Class::getGroupId));

        //Sort classes by group id (lower to higher)
        Arrays.sort(bClasses, Comparator.comparingInt(Class::getGroupId));
        Arrays.sort(bClasses, Comparator.comparingInt(Class::getGroupId));
        //Calc time gaps between classes from the same group, if the gap between 2 classes is > 1
        for (Class classA: aClasses){
            for (Class classB :bClasses) {
                if (classA.getGroupId() == classB.getGroupId() && classA.getClassId() != classB.getClassId() && (Math.abs(classA.getTimeId() - classB.getTimeId()) > 1)) {
                        //Add the timegap between the classes to timeGaps
                        timeGaps += Math.abs(classA.getTimeId() - classB.getTimeId());

                }
            }
        }
        return timeGaps;
    }

    public int calcQuality(){
        int quality = 0;
        // If a group has more than 3 classes in a row, the quality is increased by 1
        //Sort classes by time slot id (lower to higher)
        Class[] aClasses = Arrays.copyOf(classes, classes.length);
        Class[] bClasses = Arrays.copyOf(classes, classes.length);
        Arrays.sort(aClasses, Comparator.comparingInt(Class::getTimeId));
        Arrays.sort(aClasses, Comparator.comparingInt(Class::getGroupId));

        //Sort classes by group id (lower to higher)
        Arrays.sort(bClasses, Comparator.comparingInt(Class::getGroupId));
        Arrays.sort(bClasses, Comparator.comparingInt(Class::getGroupId));
        for (Class classA : aClasses) {
            int count = 0;
            for (Class classB : bClasses) {
                if (classA.getGroupId() == classB.getGroupId() && classA.getClassId() != classB.getClassId() && (Math.abs(classA.getTimeId() - classB.getTimeId()) == 1)) {
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
