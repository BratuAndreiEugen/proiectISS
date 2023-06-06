package model;

public class EventRepresentationDTO {
    private Integer id;

    private String name;

    private String date;

    private Integer avb;

    private Integer sold;

    public EventRepresentationDTO(){};

    public EventRepresentationDTO(Integer id, String name, String date, Integer avb, Integer sold) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
