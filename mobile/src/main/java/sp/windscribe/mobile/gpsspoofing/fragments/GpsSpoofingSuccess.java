/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.gpsspoofing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.OnClick;
import sp.windscribe.mobile.R;
import sp.windscribe.mobile.gpsspoofing.GpsSpoofingFragmentListener;


public class GpsSpoofingSuccess extends Fragment {

    private GpsSpoofingFragmentListener mListener;

    public GpsSpoofingSuccess() {

    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof GpsSpoofingFragmentListener) {
            mListener = (GpsSpoofingFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.gps_spoofing_success, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.close)
    public void onCloseClick() {
        if (mListener != null) {
            mListener.exit();
        }
    }
}
