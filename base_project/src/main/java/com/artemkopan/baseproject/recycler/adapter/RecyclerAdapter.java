package com.artemkopan.baseproject.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.artemkopan.baseproject.recycler.holder.BaseHolder;
import com.artemkopan.baseproject.recycler.listeners.OnItemClickListener;

/**
 * Created by Artem Kopan for jabrool
 * 25.02.2017
 */

public abstract class RecyclerAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private OnItemClickListener<M> onItemClickListener;

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getItemByPos(position), position);
    }

    public abstract void onBindViewHolder(VH holder, M model, int position);

    public abstract M getItemByPos(int position);

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        if (holder != null && holder instanceof BaseHolder) {
            ((BaseHolder) holder).clear();
        }
    }

    protected void callOnItemClick(ViewHolder holder, View view, View... transactionViews) {
        int position = holder.getAdapterPosition();
        if (onItemClickListener != null && position >= 0 && position < getItemCount()) {
            onItemClickListener.onItemClickListener(view, position, getItemByPos(position), transactionViews);
        }
    }

}
