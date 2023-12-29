package es.unex.cum.si.practica.model.util;

import es.unex.cum.si.practica.model.genotype.Group;
import es.unex.cum.si.practica.model.genotype.Room;
import es.unex.cum.si.practica.model.genotype.Subject;
import es.unex.cum.si.practica.model.genotype.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Data {
    private static final HashMap<Integer, Time> timeSlots = new HashMap<>();
    private static final HashMap<Integer, Room> roomSlots = new HashMap<>();
    private static final HashMap<Integer, Subject> subjects = new HashMap<>();
    private static final HashMap<Integer, Group> groups = new HashMap<>();
    private static final int SUBJECT_HOURS_PER_WEEK = 2;
    private final Random random = new Random();
    private static Data instance;

    /*private Data() {
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

        roomSlots.put(0, new Room(0, "Aula 1"));
        roomSlots.put(1, new Room(1, "Aula 2"));
        roomSlots.put(2, new Room(2, "Aula 3"));
        roomSlots.put(3, new Room(3, "Aula 4"));
        roomSlots.put(4, new Room(4, "Aula 5"));
        roomSlots.put(5, new Room(5, "Aula 6"));

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

        groups.put(1 , new Group(1, new int[] { 0, 1, 2, 3, 4, 5 }));
        groups.put(2 , new Group(2, new int[] { 6, 7, 8, 9, 10, 11 }));
        groups.put(3 , new Group(3, new int[] { 12, 13, 14, 15, 16, 17 }));
        groups.put(4 , new Group(4, new int[] { 18, 19, 20, 21, 22, 23 }));
    }*/

    private Data(){
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/es/unex/cum/si/practica/config/config.properties"))) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numDays = Integer.parseInt(properties.getProperty("DAYS"));
        int numPeriods = Integer.parseInt(properties.getProperty("PERIODS"));
        int numRooms = Integer.parseInt(properties.getProperty("ROOMS"));
        int numSubjects = Integer.parseInt(properties.getProperty("SUBJECTS"));
        int numGroups = Integer.parseInt(properties.getProperty("GROUPS"));

        int i;

        for (i = 0; i < numDays * numPeriods ; i++) {
            timeSlots.put(i, new Time(i, "Time " + i));
        }

        for (i = 0; i < numRooms; i++) {
            roomSlots.put(i, new Room(i, "Room " + (i+1)));
        }

        for (i = 0; i < numSubjects * numGroups; i++) {
            subjects.put(i, new Subject(i, "Subject " + i));
        }

        // asignar a cada grupo un conjunto de asignaturas, al primer grupo las primeras 6, al segundo las siguientes 6, etc.
        for (i = 0; i < numGroups; i++) {
            int[] groupSubjects = new int[numSubjects];
            for (int j = 0; j < groupSubjects.length; j++) {
                groupSubjects[j] = i * groupSubjects.length + j;
            }
            groups.put(i + 1, new Group(i + 1, groupSubjects));
        }
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public Map<Integer, Group> getGroups() {
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

    public Room getRoomSlot(int id) {
        return roomSlots.get(id);
    }

    public Subject getSubject(int id) {
        return subjects.get(id);
    }

    public int getNumOfClasses() {
        return subjects.size() * SUBJECT_HOURS_PER_WEEK;
    }

    public static void main(String[] args) {
        Data data = Data.getInstance();
        // show all data
        System.out.println("Time slots:");
        for (Time time : data.timeSlots.values()) {
            System.out.println(time);
        }
        System.out.println("Room slots:");
        for (Room room : data.roomSlots.values()) {
            System.out.println(room);
        }
        System.out.println("Subjects:");
        for (Subject subject : data.subjects.values()) {
            System.out.println(subject);
        }
        System.out.println("Groups:");
        for (Group group : data.groups.values()) {
            System.out.println(group);
        }

    }

}
