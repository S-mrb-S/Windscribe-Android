/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.upgradeactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import sp.windscribe.mobile.R;
import sp.windscribe.mobile.base.BaseActivity;
import sp.windscribe.mobile.dialogs.ErrorDialog;


public class UpgradeActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, UpgradeActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_upgrade, false);
        ErrorDialog.show(this, getString(R.string.billing_unavailable), null, true);
    }
}
