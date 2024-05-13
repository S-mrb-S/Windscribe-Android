/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.commonutils;


import androidx.annotation.Keep;

import java.io.File;

import ch.qos.logback.core.util.FileSize;
import sp.windscribe.vpn.constants.VpnPreferenceConstants;

@Keep
public class SizeBasedTriggerForLog<E> extends ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy<E> {

    @Override
    public boolean isTriggeringEvent(File activeFile, E event) {
        return (activeFile.length() >= FileSize.valueOf(VpnPreferenceConstants.DEBUG_FILE_SIZE_LOWER).getSize());
    }
}
