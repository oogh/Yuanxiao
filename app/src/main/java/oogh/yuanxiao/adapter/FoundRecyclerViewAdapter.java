package oogh.yuanxiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Found;

/**
 * Created by oogh on 17-10-17.
 */

public class FoundRecyclerViewAdapter extends RecyclerView.Adapter<FoundRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = FoundRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<Found> mDataset = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mListener;

    public FoundRecyclerViewAdapter() {

    }

    public void setDataset(List<Found> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public FoundRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_lost, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, (Found) itemView.getTag());
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoundRecyclerViewAdapter.ViewHolder holder, int position) {
        Found item = mDataset.get(position);
        holder.title.setText(item.getTitle());
        holder.describe.setText(item.getDescribe());
        holder.time.setText(item.getCreatedAt());
        holder.phone.setText(item.getPhone());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + mDataset.size());
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, describe, time, phone;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_found_title);
            describe = (TextView) itemView.findViewById(R.id.tv_found_describe);
            time = (TextView) itemView.findViewById(R.id.tv_found_time);
            phone = (TextView) itemView.findViewById(R.id.tv_found_phone);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Found l);
    }
}
