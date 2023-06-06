package model;

public class Seat {
    private Integer id;
    private Reservation reservation;
    private Integer number;

    public Seat(){};

    public Seat(Integer id, Reservation reservation, Integer number) {
        this.id = id;
        this.reservation = reservation;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", number=" + number +
                '}';
    }
}
