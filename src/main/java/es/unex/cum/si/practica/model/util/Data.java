package es.unex.cum.si.practica.model.util;

import es.unex.cum.si.practica.model.genotype.Group;
import es.unex.cum.si.practica.model.genotype.Room;
import es.unex.cum.si.practica.model.genotype.Subject;
import es.unex.cum.si.practica.model.genotype.Time;

import java.util.HashMap;
import java.util.Random;

public class Data {
    private final HashMap<Integer, Time> timeSlots;
    private final HashMap<Integer, Room> roomSlots;
    private final HashMap<Integer, Subject> subjects;
    private final HashMap<Integer, Group> groups;
    private final Random random = new Random();
    private static Data instance;

    private Data() {
        timeSlots = new HashMap<>();
        timeSlots.put(0, new Time(0, "Lunes 08:00 - 10:00"));
        timeSlots.put(1, new Time(1, "Lunes 10:00 - 12:00"));
        timeSlots.put(2, new Time(2, "Lunes 12:00 - 14:00"));
        timeSlots.put(3, new Time(3, "Lunes 15:00 - 17:00"));
        timeSlots.put(4, new Time(4, "Lunes 17:00 - 19:00"));
        timeSlots.put(5, new Time(5, "Lunes 19:00 - 21:00"));
        timeSlots.put(6, new Time(6, "Martes 08:00 - 10:00"));
        timeSlots.put(7, new Time(7, "Martes 10:00 - 12:00"));
        timeSlots.put(8, new Time(8, "Martes 12:00 - 14:00"));
        timeSlots.put(9, new Time(9, "Martes 15:00 - 17:00"));
        timeSlots.put(10, new Time(10, "Martes 17:00 - 19:00"));
        timeSlots.put(11, new Time(11, "Martes 19:00 - 21:00"));
        timeSlots.put(12, new Time(12, "Miércoles 08:00 - 10:00"));
        timeSlots.put(13, new Time(13, "Miércoles 10:00 - 12:00"));
        timeSlots.put(14, new Time(14, "Miércoles 12:00 - 14:00"));
        timeSlots.put(15, new Time(15, "Miércoles 15:00 - 17:00"));
        timeSlots.put(16, new Time(16, "Miércoles 17:00 - 19:00"));
        timeSlots.put(17, new Time(17, "Miércoles 19:00 - 21:00"));
        timeSlots.put(18, new Time(18, "Jueves 08:00 - 10:00"));
        timeSlots.put(19, new Time(19, "Jueves 10:00 - 12:00"));
        timeSlots.put(20, new Time(20, "Jueves 12:00 - 14:00"));
        timeSlots.put(21, new Time(21, "Jueves 15:00 - 17:00"));
        timeSlots.put(22, new Time(22, "Jueves 17:00 - 19:00"));
        timeSlots.put(23, new Time(23, "Jueves 19:00 - 21:00"));
        timeSlots.put(24, new Time(24, "Viernes 08:00 - 10:00"));
        timeSlots.put(25, new Time(25, "Viernes 10:00 - 12:00"));
        timeSlots.put(26, new Time(26, "Viernes 12:00 - 14:00"));
        timeSlots.put(27, new Time(27, "Viernes 15:00 - 17:00"));
        timeSlots.put(28, new Time(28, "Viernes 17:00 - 19:00"));
        timeSlots.put(29, new Time(29, "Viernes 19:00 - 21:00"));

        roomSlots = new HashMap<>();
        roomSlots.put(0, new Room(0, "Aula 1"));
        roomSlots.put(1, new Room(1, "Aula 2"));
        roomSlots.put(2, new Room(2, "Aula 3"));
        roomSlots.put(3, new Room(3, "Aula 4"));
        roomSlots.put(4, new Room(4, "Aula 5"));
        roomSlots.put(5, new Room(5, "Aula 6"));

        subjects = new HashMap<>();
        subjects.put(0, new Subject(0, "ALG", 1));
        subjects.put(1, new Subject(1, "CAL", 1));
        subjects.put(2, new Subject(2, "FP", 1));
        subjects.put(3, new Subject(3, "TC", 1));
        subjects.put(4, new Subject(4, "FI", 1));
        subjects.put(5, new Subject(5, "EDI", 1));
        subjects.put(6, new Subject(6, "FR", 2));
        subjects.put(7, new Subject(7, "AMA", 2));
        subjects.put(8, new Subject(8, "IPO", 2));
        subjects.put(9, new Subject(9, "MDP", 2));
        subjects.put(10, new Subject(10, "EC", 2));
        subjects.put(11, new Subject(11, "ASLE", 2));
        subjects.put(12, new Subject(12, "SO", 3));
        subjects.put(13, new Subject(13, "PCD", 3));
        subjects.put(14, new Subject(14, "IS", 3));
        subjects.put(15, new Subject(15, "SEI", 3));
        subjects.put(16, new Subject(16, "SINF", 3));
        subjects.put(17, new Subject(17, "HC", 3));
        subjects.put(18, new Subject(18, "GPT", 4));
        subjects.put(19, new Subject(19, "MDAI", 4));
        subjects.put(20, new Subject(20, "SI", 4));
        subjects.put(21, new Subject(21, "DP", 4));
        subjects.put(22, new Subject(22, "SA", 4));
        subjects.put(23, new Subject(23, "PFTT", 4));

        groups = new HashMap<>();
        groups.put(1 , new Group(1, new int[] { 0, 1, 2, 3, 4, 5 }));
        groups.put(2 , new Group(2, new int[] { 6, 7, 8, 9, 10, 11 }));
        groups.put(3 , new Group(3, new int[] { 12, 13, 14, 15, 16, 17 }));
        groups.put(4 , new Group(4, new int[] { 18, 19, 20, 21, 22, 23 }));
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public HashMap<Integer, Time> getTimeSlots() {
        return timeSlots;
    }

    public HashMap<Integer, Room> getRoomSlots() {
        return roomSlots;
    }

    public HashMap<Integer, Subject> getSubjects() {
        return subjects;
    }

    public HashMap<Integer, Group> getGroups() {
        return groups;
    }

    public Time getRandomTimeslot() {
        int timeslotId = random.nextInt(timeSlots.size());
        return timeSlots.get(timeslotId);
    }

    public Room getRandomRoom() {
        int roomId = random.nextInt(roomSlots.size());
        return roomSlots.get(roomId);
    }

    public Subject getRandomSubject() {
        int subjectId = random.nextInt(subjects.size());
        return subjects.get(subjectId);
    }

    public Time getTimeSlot(int id) {
        return timeSlots.get(id);
    }

    public Room getRoomSlot(int id) {
        return roomSlots.get(id);
    }

    public Subject getSubject(int id) {
        return subjects.get(id);
    }

    public Group getGroup(int id) {
        return groups.get(id);
    }

}
