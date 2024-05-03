/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.mobile.adapter;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import sp.windscribe.vpn.serverlist.entity.ServerListData;
import sp.windscribe.vpn.serverlist.interfaces.ListViewClickListener;

import java.util.List;

public class SearchRegionsAdapter extends RegionsAdapter {


    public SearchRegionsAdapter(List<? extends ExpandableGroup> groups, ServerListData serverListData,
            ListViewClickListener mListener) {
        super(groups, serverListData, mListener);
    }

}
