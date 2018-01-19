package oogh.yuanxiao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

import oogh.yuanxiao.R;

/**
 * Created by oogh on 17-10-22.
 */

public class PoiRecyclerViewAdapter extends RecyclerView.Adapter<PoiRecyclerViewAdapter.ViewHolder> {


    private List<PoiInfo> mDataset;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mListener;

    public void setDataset(List<PoiInfo> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi_result, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, (PoiInfo) itemView.getTag());
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoiInfo item = mDataset.get(position);
        holder.name.setText(item.name);
        holder.address.setText(item.address);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_poi_name);
            address = (TextView) itemView.findViewById(R.id.tv_poi_address);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, PoiInfo info);
    }
}
