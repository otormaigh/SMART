package ie.teamchile.smartapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by user on 5/26/15.
 */
public class Announcement {
    @Expose
    private Boolean blocking;
    @Expose
    private String date;
    @Expose
    private Integer id;
    @Expose
    private String note;

    public Boolean getBlocking() {
        return blocking;
    }

    public void setBlocking(Boolean blocking) {
        this.blocking = blocking;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
