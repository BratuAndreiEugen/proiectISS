package model;

public class ReservationDTO {
    private Integer id;

    private String event;

    private String eventDate;

    private String date;

    private String seatList;

    public ReservationDTO(){};

    public ReservationDTO(Integer id, String event, String date, String eventDate, String seatList) {
        this.id = id;
        this.event = event;
        this.eventDate = eventDate;
        this.date = date;
        this.seatList = seatList;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getSeatList() {
        return seatList;
    }

    public void setSeatList(String seatList) {
        this.seatList = seatList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
