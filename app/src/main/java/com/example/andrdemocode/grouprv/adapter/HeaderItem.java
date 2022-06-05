package com.example.andrdemocode.grouprv.adapter;

public class HeaderItem implements ListItem {
    public int groupNum;

    public HeaderItem(int groupNum) {
        this.groupNum = groupNum;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
