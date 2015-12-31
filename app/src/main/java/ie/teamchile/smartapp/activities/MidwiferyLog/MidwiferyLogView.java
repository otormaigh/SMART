package ie.teamchile.smartapp.activities.MidwiferyLog;

import java.util.List;

import ie.teamchile.smartapp.activities.Base.BaseView;
import ie.teamchile.smartapp.model.PregnancyNote;

/**
 * Created by elliot on 31/12/2015.
 */
public interface MidwiferyLogView extends BaseView {
    void updatePregnancyList(List<PregnancyNote> pregnancyNotes);

    void showAddNoteDialog();
}
