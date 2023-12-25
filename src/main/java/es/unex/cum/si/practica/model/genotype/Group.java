package es.unex.cum.si.practica.model.genotype;

public class Group {
    private final int id;
    private final int[] subjectIds;

    public Group(int id, int[] subjectIds) {
        this.id = id;
        this.subjectIds = subjectIds;
    }

    public int getId() {
        return id;
    }

    public int[] getSubjectIds() {
        return subjectIds;
    }

}
