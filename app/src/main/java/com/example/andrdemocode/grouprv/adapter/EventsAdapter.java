package com.example.andrdemocode.grouprv.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andrdemocode.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);

        }

    }

    private static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        EventViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.event);
        }

    }

    /**
     * 分组了的数据
     */
    @NonNull
    private List<ListItem> items = new ArrayList<>();

    public EventsAdapter(@NonNull List<ListItem> items) {
        this.items = items;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.view_list_item_header, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_EVENT: {
                View itemView = inflater.inflate(R.layout.view_list_item_event, parent, false);
                return new EventViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                HeaderItem header = (HeaderItem) items.get(position);
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
                // your logic here
                holder.header.setText("group " + header.groupNum);
                break;
            }
            case ListItem.TYPE_EVENT: {
                EventItem event = (EventItem) items.get(position);
                EventViewHolder holder = (EventViewHolder) viewHolder;
                // your logic here
                holder.title.setText(event.event.title);
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * 关键：重写getItemViewType，
     * 每条数据带type，不同type填充不同布局
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

}