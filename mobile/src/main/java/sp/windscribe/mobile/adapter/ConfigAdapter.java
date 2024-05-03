/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sp.windscribe.mobile.R;
import sp.windscribe.mobile.holder.ConfigViewHolder;
import sp.windscribe.mobile.holder.RemoveConfigHolder;
import sp.windscribe.vpn.serverlist.entity.ConfigFile;
import sp.windscribe.vpn.serverlist.entity.ServerListData;
import sp.windscribe.vpn.serverlist.entity.PingTime;
import sp.windscribe.vpn.serverlist.interfaces.ListViewClickListener;

import java.util.List;

public class ConfigAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ConfigFile> configFiles;

    private final ServerListData serverListData;

    private final ListViewClickListener listViewClickListener;

    public ConfigAdapter(List<ConfigFile> configFiles, ServerListData serverListData,
            ListViewClickListener listViewClickListener) {
        this.configFiles = configFiles;
        this.listViewClickListener = listViewClickListener;
        this.serverListData = serverListData;
    }

    public List<ConfigFile> getConfigFiles() {
        return configFiles;
    }

    @Override
    public int getItemCount() {
        return configFiles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return configFiles.get(position).getType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConfigFile configFile = configFiles.get(holder.getAdapterPosition());
        if (holder instanceof ConfigViewHolder) {
            ConfigViewHolder configViewHolder = (ConfigViewHolder) holder;
            int pingTime = getPingTime(configFile);
            configViewHolder.onBind(configFile, listViewClickListener, serverListData, pingTime);
        } else {
            RemoveConfigHolder removeConfigHolder = (RemoveConfigHolder) holder;
            int pingTime = getPingTime(configFile);
            removeConfigHolder.onBind(configFile, listViewClickListener, serverListData, pingTime);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.config_layout, parent, false);
            return new ConfigViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.remove_config_layout, parent, false);
            return new RemoveConfigHolder(view);
        }

    }

    private int getPingTime(ConfigFile city) {
        for (PingTime pingTime : serverListData.getPingTimes()) {
            if (city.getPrimaryKey() == pingTime.ping_id) {
                return pingTime.getPingTime();
            }
        }
        return -1;
    }
}
