package es.unex.cum.si.practica.model.phenotype;

/**
 * The record class represents a class in a schedule.
 * A class is a combination of a group, a subject, a room, and a timeslot.
 * @param classId
 * @param groupId
 * @param timeId
 * @param roomId
 * @param subjectId
 */
public record Class(int classId, int groupId, int timeId, int roomId, int subjectId) {
}
