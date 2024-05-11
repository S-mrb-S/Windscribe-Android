package sp.windscribe.vpn.qq;

import com.tencent.mmkv.MMKV;

public class MmkvManager {

    private static MMKV loginStorage;
    private static MMKV serverStorage;

    public static synchronized MMKV getLoginStorage() {
        if (loginStorage == null) {
            loginStorage = MMKV.mmkvWithID("login_storage", MMKV.MULTI_PROCESS_MODE);
        }
        return loginStorage;
    }

    public static synchronized MMKV getSettingsStorage() {
        if (serverStorage == null) {
            serverStorage = MMKV.mmkvWithID("server_storage", MMKV.MULTI_PROCESS_MODE);
        }
        return serverStorage;
    }

}
