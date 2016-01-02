package ie.teamchile.smartapp.activities.MidwiferyLog;

import java.util.List;

import ie.teamchile.smartapp.activities.Base.BaseViewSec;
import ie.teamchile.smartapp.model.ResponsePregnancyNote;

/**
 * Created by elliot on 31/12/2015.
 */
public interface MidwiferyLogView extends BaseViewSec {
    void updatePregnancyList(List<ResponsePregnancyNote> responsePregnancyNotes);

    void showAddNoteDialog();
}
