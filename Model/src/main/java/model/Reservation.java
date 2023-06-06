package model;

public class Reservation {
    private Integer id;
    private EventRepresentation event;
    private User user;
    private String date;

    public Reservation(){};

    public Reservation(Integer id, EventRepresentation event, User user, String date) {
        this.id = id;
        this.event = event;
        this.user = user;
        this.date = date;
    }

    public Reservation(EventRepresentation event, User user, String date) {
        this.event = event;
        this.user = user;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventRepresentation getEvent() {
        return event;
    }

    public void setEvent(EventRepresentation event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", event=" + event +
                ", user=" + user +
                ", date='" + date + '\'' +
                '}';
    }
}
