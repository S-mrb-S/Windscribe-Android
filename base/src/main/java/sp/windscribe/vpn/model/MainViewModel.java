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
    private final MutableLiveData<Integer> isChanged = new MutableLiveData<>(); // 0 --> none, 1 --> data changed, 2 --> no data

    public LiveData<Integer> getIsChanged() {
        return isChanged;
    }

    public void retrieveIsChanged(Integer def) {
        if (getIsIsChanged() == null)
            isChanged.setValue(def);
    }

    public Integer getIsIsChanged() {
        return isChanged.getValue();
    }

    public void saveIsChanged(@NonNull Integer integer) {
        isChanged.setValue(integer);
    }
}