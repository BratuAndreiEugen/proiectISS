package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventRepresentation {
    private Integer id;
    private Event event;
    private String date;
    private Integer avb;
    private Integer sold;

    public EventRepresentation(){};

    public EventRepresentation(Integer id, Event event, String date, Integer avb, Integer sold) {
        this.id = id;
        this.event = event;
        this.date = date;
        this.avb = avb;
        this.sold = sold;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAvb() {
        return avb;
    }

    public void setAvb(Integer avb) {
        this.avb = avb;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "EventRepresentation{" +
                "id=" + id +
                ", event=" + event +
                ", date='" + date + '\'' +
                ", avb=" + avb +
                ", sold=" + sold +
                '}';
    }
}
