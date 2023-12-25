package es.unex.cum.si.practica.model.genotype;

public class Room {
    private final int id;
    private final String denomination;

    public Room(int id, String denomination) {
        this.id = id;
        this.denomination = denomination;
    }

    public int getId() {
        return id;
    }

    public String getDenomination() {
        return denomination;
    }
}
