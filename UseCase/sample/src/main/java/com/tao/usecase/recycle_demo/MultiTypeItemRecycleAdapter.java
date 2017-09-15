package com.tao.usecase.recycle_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by SDT14324 on 2017/9/15.
 */

public  class MultiTypeItemRecycleAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;
    protected ItemViewManager itemViewManager;
    public MultiTypeItemRecycleAdapter(Context context,List<T> ds){
        itemViewManager = new ItemViewManager();
        mDatas = ds;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewController itemViewController = itemViewManager.getItemViewDelegate(viewType);
        int layoutId = itemViewController.getItemViewLayoutId();
        MyViewHolder holder = MyViewHolder.createViewHolder(mContext,parent,layoutId);
        onViewHolderCreated(holder,holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }

    public void convert(MyViewHolder holder, T t) {
        itemViewManager.convert(holder, t, holder.getAdapterPosition());
    }


    public void onViewHolderCreated(MyViewHolder holder,View itemView){

    }
    protected OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder,  int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder,  int position);
    }
    protected void setListener(final ViewGroup parent, final MyViewHolder viewHolder, int viewType) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder , position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });

    }

    /*@Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }*/
}
