/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.adapter;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import sp.windscribe.vpn.serverlist.entity.ServerListData;
import sp.windscribe.vpn.serverlist.interfaces.ListViewClickListener;

public class ExpandedAdapter extends SearchRegionsAdapter {

    public ExpandedAdapter(List<? extends ExpandableGroup> groups, ServerListData serverListData,
                           ListViewClickListener mListener) {
        super(groups, serverListData, mListener);
    }


}