package sp.windscribe.vpn.sp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import sp.windscribe.vpn.model.MainViewModel;

// by MRB
public class Global extends GlobalHelper {
    public Global(Application context) {
        setMainApplication(context);
    }

    @NonNull
    public MainViewModel getmViewModel() {
        if (this.mViewModel == null) {
            this.mViewModel =
                    new ViewModelProvider.AndroidViewModelFactory((Application) getMainApplication().getApplicationContext()).create(MainViewModel.class);
        }
        return this.mViewModel;
    }

    public String getMyApi() {
        return this.getApi();
    }

    @NonNull
    public Application getMainApplication() {
        assert this.mainApplication != null;
        return this.mainApplication;
    }

    public void setMainApplication(@NonNull Application mainApplication) {
        this.mainApplication = mainApplication;
    }
}