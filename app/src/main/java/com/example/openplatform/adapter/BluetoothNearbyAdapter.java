package com.example.openplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.openplatform.R;

import java.util.List;

/**
 * 附近的蓝牙设备 适配器
 */
public class BluetoothNearbyAdapter extends RecyclerView.Adapter<BluetoothNearbyAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final List<String> list;
    private final Context mContent;

    private OnItemClickListener mOnItemClickListener = null;

    public BluetoothNearbyAdapter(Context context, List<String> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        mContent = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bluetooth_nearby, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = "" + list.get(position);
        holder.name.setText(name);

        if (mOnItemClickListener != null)
            holder.button.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.button, position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, button;


        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            button = view.findViewById(R.id.button);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
