/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.gpsspoofing;

import javax.inject.Inject;

import sp.windscribe.vpn.ActivityInteractor;

public class GpsSpoofingPresenterImp implements GpsSpoofingPresenter {

    private final ActivityInteractor gpsSpoofingInteractor;

    private final GpsSpoofingSettingView gpsSpoofingSettingView;

    @Inject
    public GpsSpoofingPresenterImp(GpsSpoofingSettingView gpsSpoofingSettingView,
                                   ActivityInteractor activityInteractor) {
        this.gpsSpoofingInteractor = activityInteractor;
        this.gpsSpoofingSettingView = gpsSpoofingSettingView;
    }

    @Override
    public void onError() {
        gpsSpoofingInteractor.getAppPreferenceInterface().setGpsSpoofing(true);
        gpsSpoofingSettingView.onError();
    }

    @Override
    public void onSuccess() {
        gpsSpoofingInteractor.getAppPreferenceInterface().setGpsSpoofing(true);
        gpsSpoofingSettingView.onSuccess();
    }
}
