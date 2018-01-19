package oogh.yuanxiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Lost;

/**
 * Created by oogh on 17-10-17.
 */

public class LostRecyclerViewAdapter extends RecyclerView.Adapter<LostRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = LostRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<Lost> mDataset = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mListener;

    public LostRecyclerViewAdapter() {

    }

    public void setDataset(List<Lost> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public LostRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_lost, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, (Lost) itemView.getTag());
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LostRecyclerViewAdapter.ViewHolder holder, int position) {
        Lost item = mDataset.get(position);
        holder.title.setText(item.getTitle());
        holder.describe.setText(item.getDescribe());
        holder.time.setText(item.getCreatedAt());
        holder.phone.setText(item.getPhone());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, describe, time, phone;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_lost_title);
            describe = (TextView) itemView.findViewById(R.id.tv_lost_describe);
            time = (TextView) itemView.findViewById(R.id.tv_lost_time);
            phone = (TextView) itemView.findViewById(R.id.tv_lost_phone);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Lost l);
    }
}
