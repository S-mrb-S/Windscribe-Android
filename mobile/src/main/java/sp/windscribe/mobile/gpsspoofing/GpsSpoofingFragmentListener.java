/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.gpsspoofing;

public interface GpsSpoofingFragmentListener {

    void checkSuccess();

    void exit();

    void openDeveloperSettings();

    void openSettings();

    void setFragment(int index);

}
