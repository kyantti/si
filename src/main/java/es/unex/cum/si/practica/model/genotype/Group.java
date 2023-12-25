package es.unex.cum.si.practica.model.genotype;

import java.util.Arrays;

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
