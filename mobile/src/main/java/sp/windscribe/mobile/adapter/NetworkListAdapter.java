/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sp.windscribe.mobile.R;
import sp.windscribe.mobile.networksecurity.viewholder.NetworkAdapterActionListener;
import sp.windscribe.mobile.networksecurity.viewholder.NetworkListViewHolder;
import sp.windscribe.vpn.Windscribe;
import sp.windscribe.vpn.localdatabase.tables.NetworkInfo;

public class NetworkListAdapter extends RecyclerView.Adapter<NetworkListViewHolder> {

    private NetworkAdapterActionListener mAdapterActionListener;

    private final List<NetworkInfo> mNetList;


    public NetworkListAdapter(List<NetworkInfo> mNetList) {
        this.mNetList = mNetList;
    }

    @Override
    public int getItemCount() {
        return mNetList != null ? mNetList.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final NetworkListViewHolder holder, int position) {
        NetworkInfo networkInfo = mNetList.get(position);
        holder.tvNetworkName.setText(networkInfo.getNetworkName());
        String protectionStatus = networkInfo.isAutoSecureOn() ? Windscribe.getAppContext()
                .getText(R.string.network_secured).toString()
                : Windscribe.getAppContext().getText(R.string.network_unsecured).toString();
        holder.tvProtection.setText(protectionStatus);

        holder.itemView.setOnClickListener(v -> mAdapterActionListener.onItemSelected(networkInfo));
        if (position == getItemCount() - 1) {
            holder.dividerView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public NetworkListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.network_list_card, parent, false);
        return new NetworkListViewHolder(itemView);
    }

    public void setAdapterActionListener(NetworkAdapterActionListener mListener) {
        this.mAdapterActionListener = mListener;
    }

}
