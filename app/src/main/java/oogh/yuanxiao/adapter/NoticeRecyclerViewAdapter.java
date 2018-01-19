package oogh.yuanxiao.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Notice;


/**
 * Created by oogh on 17-10-23.
 */

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = NoticeRecyclerViewAdapter.class.getSimpleName();

    private List<Notice> mDataset = new ArrayList<>();

    private OnItemClickListener mClickListener;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setDataset(List<Notice> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, (Notice) itemView.getTag());
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice item = mDataset.get(position);
        holder.logo.setText(item.getTag().substring(0, 1));
        holder.tag.setText(item.getTag());
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + mDataset.size());
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView logo, tag, title, date;

        public ViewHolder(View itemView) {
            super(itemView);
            logo = (TextView) itemView.findViewById(R.id.tv_logo);
            tag = (TextView) itemView.findViewById(R.id.tv_notice_tag);
            title = (TextView) itemView.findViewById(R.id.tv_notice_title);
            date = (TextView) itemView.findViewById(R.id.tv_notice_date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Notice notice);
    }
}
