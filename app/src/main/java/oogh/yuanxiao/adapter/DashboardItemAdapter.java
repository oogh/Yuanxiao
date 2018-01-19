package oogh.yuanxiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.DashboardItem;

/**
 * Created by oogh on 17-9-19.
 */

public class DashboardItemAdapter extends RecyclerView.Adapter<DashboardItemAdapter.ViewHolder> implements View.OnClickListener {
    private List<DashboardItem> mDataSet;
    private Context mContext;

    private OnDashboardItemClickListener mListener;

    public DashboardItemAdapter(List<DashboardItem> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_dashboard, parent, false);
        itemView.setOnClickListener(this);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DashboardItem item = mDataSet.get(position);
        holder.logo.setImageResource(item.getImage());
        holder.title.setText(item.getTitle());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onClick(View v) {
        mListener.onDashboardItemClicked(v, ((int) v.getTag()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView logo;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            logo = (ImageView) itemView.findViewById(R.id.iv_logo);
        }
    }

    public void setOnDashboardItemClickListener(OnDashboardItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnDashboardItemClickListener {
        void onDashboardItemClicked(View v, int position);
    }
}
