package es.unex.cum.si.practica.model.phenotype;

import java.util.Arrays;

/**
 * The record group represents a group of students that share the same subjects.
 * The group attends to a class.
 * @param id
 * @param subjectIds
 */
public record Group(int id, int[] subjectIds) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (id != group.id) return false;
        return Arrays.equals(subjectIds, group.subjectIds);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Arrays.hashCode(subjectIds);
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", subjectIds=" + Arrays.toString(subjectIds) +
                '}';
    }
}
