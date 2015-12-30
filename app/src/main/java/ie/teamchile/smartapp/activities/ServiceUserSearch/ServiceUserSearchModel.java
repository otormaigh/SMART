package ie.teamchile.smartapp.activities.ServiceUserSearch;

import java.util.List;

import ie.teamchile.smartapp.model.BaseResponseModel;
import ie.teamchile.smartapp.model.FeedingHistory;
import ie.teamchile.smartapp.model.HearingHistory;
import ie.teamchile.smartapp.model.NbstHistory;
import ie.teamchile.smartapp.model.VitKHistory;

/**
 * Created by elliot on 27/12/2015.
 */
public interface ServiceUserSearchModel {
    void saveVitKToRealm(List<VitKHistory> vitKHistories);

    void saveHearingToRealm(List<HearingHistory> hearingHistories);

    void saveNbstToRealm(List<NbstHistory> nbstHistories);

    void saveFeedingHistoriesToRealm(List<FeedingHistory> feedingHistories);

    void saveServiceUserToRealm(BaseResponseModel baseResponseModel);

    void deleteDataFromRealm();
}
