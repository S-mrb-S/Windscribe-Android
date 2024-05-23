package sp.windscribe.vpn.sp;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import kotlin.Unit;
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

    public void showToast(String txt){
        MainApplicationExecuter(() -> Toast.makeText(mainApplication, txt, Toast.LENGTH_SHORT).show(), getMainApplication());
    }
}