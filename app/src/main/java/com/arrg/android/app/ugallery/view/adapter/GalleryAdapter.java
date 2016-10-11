package com.arrg.android.app.ugallery.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.arrg.android.app.ugallery.R;
import com.arrg.android.app.ugallery.model.entity.PhoneAlbum;
import com.bumptech.glide.Glide;
import com.thefinestartist.utils.content.Res;
import com.thefinestartist.utils.ui.DisplayUtil;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends DragSelectRecyclerViewAdapter<GalleryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(ViewHolder viewHolder, View itemView, int position);

        void onLongClick(ViewHolder viewHolder, View itemView, int position);
    }

    private static final String MTV_REG = "^.*\\.(mp4|3gp)$";
    private static final String MP3_REG = "^.*\\.(mp3|wav)$";
    private static final String JPG_REG = "^.*\\.(gif|jpg|png)$";

    private ArrayList<PhoneAlbum> albumArrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public GalleryAdapter(ArrayList<PhoneAlbum> albumArrayList, Context context) {
        this.albumArrayList = albumArrayList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        PhoneAlbum phoneAlbum = albumArrayList.get(position);

        Glide.with(context).load(phoneAlbum.getCoverMedia()).crossFade().into(holder.ivCover);

        holder.cardContainer.setLayoutParams(new CardView.LayoutParams(DisplayUtil.getWidth() / Res.getInteger(R.integer.grid_count_gallery), Math.round((DisplayUtil.getWidth() / Res.getInteger(R.integer.grid_count_gallery)) * 1.5f)));
        holder.tvAlbumName.setText(phoneAlbum.getAlbumName());
        holder.tvNumberOfFiles.setText(String.format(Locale.US, "(%d)", phoneAlbum.getPhoneMedias().size()));

        holder.cbIsSelected.setVisibility(isIndexSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.cbIsSelected.setChecked(phoneAlbum.getSelected());
        holder.ivPlay.setVisibility(phoneAlbum.getCoverMedia().matches(MTV_REG) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_layout, parent, false), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }

    public PhoneAlbum getAlbum(int position) {
        return albumArrayList.get(position);
    }

    public void setChecked(int position, boolean isChecked) {
        albumArrayList.get(position).setSelected(isChecked);
    }

    public int getSelectedItems() {
        int i = 0;

        for (PhoneAlbum album : albumArrayList) {
            if (album.getSelected()) {
                i++;
            }
        }

        return i;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.ivCover)
        ImageView ivCover;
        @BindView(R.id.ivPlay)
        ImageView ivPlay;
        @BindView(R.id.tvAlbumName)
        TextView tvAlbumName;
        @BindView(R.id.tvNumberOfFiles)
        TextView tvNumberOfFiles;
        @BindView(R.id.cbIsSelected)
        CheckBox cbIsSelected;
        @BindView(R.id.cardContainer)
        CardView cardContainer;

        private OnItemClickListener onItemClickListener;

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
                onItemClickListener.onClick(this, itemView, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onLongClick(this, itemView, getAdapterPosition());
            }
            return true;
        }
    }
}
