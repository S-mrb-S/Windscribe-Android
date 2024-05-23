package sp.windscribe.vpn.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    // data left (quotaLeft) حجم باقی مانده از کل
    private final MutableLiveData<Integer> dataLeft = new MutableLiveData<>();

    public LiveData<Integer> getDataLeft() {
        return dataLeft;
    }

    public void retrieveDataLeft(int def) {
        if (getIsDataLeft() == null)
            dataLeft.setValue(def);
    }

    public Integer getIsDataLeft() {
        return dataLeft.getValue();
    }

    public void saveDataLeft(@NonNull Integer integer) {
        dataLeft.setValue(integer);
    }

    // data daily left (dailyQuotaLeft) حجم باقی مانده از کل در مصرف روزانه
    private final MutableLiveData<Integer> dataDailyLeft = new MutableLiveData<>();

    public LiveData<Integer> getDataDailyLeft() {
        return dataDailyLeft;
    }

    public void retrieveDataDailyLeft(int def) {
        if (getIsDataDailyLeft() == null)
            dataDailyLeft.setValue(def);
    }

    public Integer getIsDataDailyLeft() {
        return dataDailyLeft.getValue();
    }

    public void saveDataDailyLeft(@NonNull Integer integer) {
        dataDailyLeft.setValue(integer);
    }
}