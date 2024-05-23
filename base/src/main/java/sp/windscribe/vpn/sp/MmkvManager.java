package sp.windscribe.vpn.sp;

import com.tencent.mmkv.MMKV;

public class MmkvManager {

    private static MMKV serviceStorage;
    private static MMKV serverStorage;

    public static synchronized MMKV getServiceStorage() {
        if (serviceStorage == null) {
            serviceStorage = MMKV.mmkvWithID("service_storage", MMKV.MULTI_PROCESS_MODE);
        }
        return serviceStorage;
    }

    public static synchronized MMKV getSettingsStorage() {
        if (serverStorage == null) {
            serverStorage = MMKV.mmkvWithID("server_storage", MMKV.MULTI_PROCESS_MODE);
        }
        return serverStorage;
    }

}
