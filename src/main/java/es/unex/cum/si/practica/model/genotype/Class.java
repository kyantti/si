package es.unex.cum.si.practica.model.genotype;

public class Class {
    private final int classId;
    private final int groupId;
    private final int subjectId;
    private int timeId;
    private int roomId;

    public Class(int classId, int groupId, int timeId, int roomId, int subjectId) {
        this.classId = classId;
        this.groupId = groupId;
        this.timeId = timeId;
        this.roomId = roomId;
        this.subjectId = subjectId;
    }

    public int getClassId() {
        return classId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getTimeId() {
        return timeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getGroupId() {
        return groupId;
    }
}
