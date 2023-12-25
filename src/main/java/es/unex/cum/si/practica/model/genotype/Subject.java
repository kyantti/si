package es.unex.cum.si.practica.model.genotype;

public class Subject {
    private final int id;
    private final String denomination;
    private final int year;
    public Subject(int id, String denomination, int year) {
        this.id = id;
        this.denomination = denomination;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getDenomination() {
        return denomination;
    }

    public int getYear() {
        return year;
    }
}
