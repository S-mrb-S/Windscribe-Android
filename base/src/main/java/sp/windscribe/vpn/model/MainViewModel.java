package sp.windscribe.vpn.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Integer> dataLeft = new MutableLiveData<>();

    public LiveData<Integer> getDataLeft() {
        return dataLeft;
    }

    public void retrieveDataLeft() {
        if (getIsDataLeft() == null)
            dataLeft.setValue(9999);
    }

    public Integer getIsDataLeft() {
        return dataLeft.getValue();
    }

    public void saveDataLeft(@NonNull Integer bool) {
        dataLeft.setValue(bool);
    }
}