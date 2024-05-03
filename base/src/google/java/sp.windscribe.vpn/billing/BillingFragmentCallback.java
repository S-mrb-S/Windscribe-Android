/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.billing;

import com.amazon.device.iap.model.Product;
import com.android.billingclient.api.ProductDetails;

public interface BillingFragmentCallback {

    void onContinuePlanClick(ProductDetails productDetails, int selectedIndex);

    void onContinuePlanClick(Product selectedSku);

    void onRestorePurchaseClick();

    void onTenGbFreeClick();

    void onTermsClick();

    void onPolicyClick();
}