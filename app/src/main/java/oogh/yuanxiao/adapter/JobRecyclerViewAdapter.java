package oogh.yuanxiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Job;

/**
 * Created by oogh on 17-10-17.
 */

public class JobRecyclerViewAdapter extends RecyclerView.Adapter<JobRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = JobRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<Job> mDataset = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mListener;

    public JobRecyclerViewAdapter() {

    }

    public void setDataset(List<Job> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public JobRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_job, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, (Job) itemView.getTag());
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobRecyclerViewAdapter.ViewHolder holder, int position) {
        Job item = mDataset.get(position);
        holder.title.setText(item.getTitle());
        holder.company_describe.setText(item.getCompany_describe());
        String extra = item.getExtra();
        String[] extras = extra.split("周");
        String extraInfo = extras[0] + "周   " + extras[1];
        holder.extra.setText(extraInfo);
        holder.logo.setImageURI(item.getLogoUri());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, company_describe, extra;
        SimpleDraweeView logo;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_job_title);
            company_describe = (TextView) itemView.findViewById(R.id.tv_job_company_describe);
            extra = (TextView) itemView.findViewById(R.id.tv_job_extra);
            logo = (SimpleDraweeView) itemView.findViewById(R.id.sdv_job);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Job j);
    }
}
