/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.networksecurity.viewholder;

import sp.windscribe.vpn.localdatabase.tables.NetworkInfo;

public interface NetworkAdapterActionListener {

    void onItemSelected(NetworkInfo networkInfo);
}
