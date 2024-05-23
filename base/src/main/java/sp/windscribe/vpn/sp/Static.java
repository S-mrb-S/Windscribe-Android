package sp.windscribe.vpn.sp;

import android.app.Application;

import androidx.annotation.NonNull;

// Static objects
public class Static {
    // All data
    private static Global globalData;

    @NonNull
    public static Global getGlobalData() {
        assert globalData != null;
        return globalData;
    }

    public static void setGlobalData(@NonNull Application context) {
        // is should be to null
        assert globalData == null;
        globalData = new Global(context);
    }
}