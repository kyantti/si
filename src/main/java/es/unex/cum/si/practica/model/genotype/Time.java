package es.unex.cum.si.practica.model.genotype;

public class Time {
    private final int id;
    private final String denomination;

    public Time(int id, String denomination) {
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
