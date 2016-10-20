package com.arrg.android.app.ugallery.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.arrg.android.app.ugallery.R;
import com.arrg.android.app.ugallery.model.entity.PhoneMedia;
import com.bumptech.glide.Glide;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaSearchAdapter extends DragSelectRecyclerViewAdapter<MediaSearchAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder, View itemView, int position);

        void onLongItemClick(ViewHolder viewHolder, View itemView, int position);
    }

    private static final String MTV_REG = "^.*\\.(mp4|3gp)$";
    private static final String MP3_REG = "^.*\\.(mp3|wav)$";
    private static final String JPG_REG = "^.*\\.(gif|jpg|png)$";

    private ArrayList<PhoneMedia> mediaArrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public MediaSearchAdapter(Context context, ArrayList<PhoneMedia> mediaArrayList) {
        this.context = context;
        this.mediaArrayList = mediaArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaSearchAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_media_item_layout, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        PhoneMedia phoneMedia = mediaArrayList.get(position);

        Glide.with(context).load(phoneMedia.getMediaPath()).dontAnimate().into(holder.ivMedia);
        holder.tvTitle.setText(phoneMedia.getTitle());
        holder.tvPath.setText(phoneMedia.getMediaPath());

        holder.ivPlay.setVisibility(phoneMedia.getMediaPath().matches(MTV_REG) ? View.VISIBLE : View.INVISIBLE);
        holder.container.setBackgroundResource(isIndexSelected(position) ?  R.color.alphaColorAccent : android.R.color.transparent);
    }

    @Override
    public int getItemCount() {
        return mediaArrayList.size();
    }

    public PhoneMedia getMedia(int position) {
        return mediaArrayList.get(position);
    }

    public void setChecked(int position, boolean checked) {
        mediaArrayList.get(position).setChecked(checked);
    }

    public int getSelectedItems() {
        int selectedItems = 0;

        for (int i = 0; i < getItemCount(); i++) {
            if (isIndexSelected(i)) {
                selectedItems++;
            }
        }

        return selectedItems;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private OnItemClickListener onItemClickListener;

        @BindView(R.id.container)
        AutoLinearLayout container;

        @BindView(R.id.ivTitle)
        ImageView ivMedia;

        @BindView(R.id.ivPlay)
        ImageView ivPlay;

        @BindView(R.id.tvName)
        TextView tvTitle;

        @BindView(R.id.tvPath)
        TextView tvPath;

        @BindView(R.id.cbIsSelected)
        CheckBox cbIsSelected;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.onItemClickListener = onItemClickListener;
            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(this, itemView, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onLongItemClick(this, itemView, getAdapterPosition());
            }
            return true;
        }
    }
}
