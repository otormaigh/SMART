package ie.teamchile.smartapp.retrofit;

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

    /**
     *
     * @return
     * The blocking
     */
    public Boolean getBlocking() {
        return blocking;
    }

    /**
     *
     * @param blocking
     * The blocking
     */
    public void setBlocking(Boolean blocking) {
        this.blocking = blocking;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The note
     */
    public String getNote() {
        return note;
    }

    /**
     *
     * @param note
     * The note
     */
    public void setNote(String note) {
        this.note = note;
    }
}
