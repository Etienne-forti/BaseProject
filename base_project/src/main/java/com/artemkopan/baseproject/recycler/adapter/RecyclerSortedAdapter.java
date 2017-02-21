package com.artemkopan.baseproject.recycler.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.util.SortedList.Callback;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.baseproject.recycler.listeners.OnItemClickListener;

/**
 * Created by Artem Kopan for jabrool
 * 21.02.17
 */

public abstract class RecyclerSortedAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private SortedList<M> list;
    private OnItemClickListener<M> onItemClickListener;

    public void initList(Class<M> clazz, Callback<M> callback) {
        list = new SortedList<>(clazz, callback);
    }

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getItemByPos(position), position);
    }

    public abstract void onBindViewHolder(VH holder, M model, int position);

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SortedList<M> getList() {
        if (list == null) throw new NullPointerException("List is null. Please call iniList() method");
        return list;
    }

    public M getItemByPos(int position) {
        return getList().get(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public int getItemIndex(M item) {
        return getList().indexOf(item);
    }

    public int getObjectIndex(Object item) {
        if (item == null) return RecyclerView.NO_POSITION;

        M findItem;

        for (int i = 0; i < getList().size(); i++) {
            findItem = getList().get(i);
            if (item.equals(findItem)) {
                return i;
            }
        }

        return RecyclerView.NO_POSITION;
    }

    public void remove(M item) {
        getList().remove(item);
    }

    public void clear() {
        getList().clear();
    }

    protected void callOnItemClick(View view, int pos, M object, View... transactionViews) {
        if (onItemClickListener != null && pos >= 0) {
            onItemClickListener.onItemClickListener(view, pos, object, transactionViews);
        }
    }
}
