package sp.windscribe.vpn.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    // data left (dailyQuotaLeft, quotaLeft) حجم باقی مانده از کل
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

    // refresh list
    private final MutableLiveData<Boolean> isChanged = new MutableLiveData<>();

    public LiveData<Boolean> getIsChanged() {
        return isChanged;
    }

    public void retrieveIsChanged(Boolean def) {
        if (getIsIsChanged() == null)
            isChanged.setValue(def);
    }

    public Boolean getIsIsChanged() {
        return isChanged.getValue();
    }

    public void saveIsChanged(@NonNull Boolean integer) {
        isChanged.setValue(integer);
    }
}