package com.artemkopan.baseproject.recycler.adapter;


import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.recycler.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public abstract class RecyclerBaseAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final String TAG = "RecyclerBaseAdapter";
    protected List<M> mList;
    private OnItemClickListener<M> mOnItemClickListener;

    public RecyclerBaseAdapter() {
    }

    public RecyclerBaseAdapter(List<M> list) {
        this.mList = list;
    }

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getItemByPos(position), position);
    }

    public abstract void onBindViewHolder(VH holder, M model, int position);

    @Nullable
    public M removeItem(Object model) {
        int index = mList.indexOf(model);
        if (index != -1)
            return removeItem(index);
        else
            return null;
    }

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public M removeItem(int position) {
        final M model = mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
        return model;
    }

    public void addItem(M model) {
        mList.add(model);
        notifyItemInserted(mList.size() - 1);
    }

    public void addItem(int position, M model) {
        mList.add(position, model);
        notifyItemInserted(position);
    }

    public void addItemRanged(Collection<M> collection) {
        if (collection == null) {
            Log.e(TAG, "Collection is empty");
            return;
        }
        int size = mList.size();
        mList.addAll(collection);
        notifyItemRangeInserted(size, mList.size() - size);
    }


    public void moveItem(int fromPosition, int toPosition) {
        final M model = mList.remove(fromPosition);
        mList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Nullable
    public M getItemByPos(int position) {
        if (mList == null) {
            Log.e("getItemByPos: ", new NullPointerException("mList is null"));
            return null;
        } else if (position >= mList.size() || position < 0) {
            Log.e("getItemByPos: ", new ArrayIndexOutOfBoundsException(
                    "position " + position + " mList size " + mList.size()));
            return null;
        }

        return mList.get(position);
    }

    public List<M> getList() {
        return mList;
    }

    public void setList(List<M> list) {
        this.mList = null;
        this.mList = list;
        notifyDataSetChanged();
    }

    public void createList(boolean dropIfExist) {
        if (dropIfExist && mList != null) {
            setList(new ArrayList<M>());
        } else if (mList == null) {
            setList(new ArrayList<M>());
        }
    }

    public int getItemIndex(Object object) {
        return mList != null ? mList.indexOf(object) : -1;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    protected void callOnItemClick(View view, int pos, M object, View... transactionViews) {
        if (pos >= 0) {
            mOnItemClickListener.onItemClickListener(view, pos, object, transactionViews);
        }
    }
}
