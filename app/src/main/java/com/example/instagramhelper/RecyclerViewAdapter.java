package com.example.instagramhelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mImages = new ArrayList<>();
    private int heightImage;
    private Context mContext;
    private final OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(String mImages, SquareImageView images);
    }

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImages, OnItemClickListener listener) {
        this.mImages = mImages;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_image_gridview, viewGroup, false);
        heightImage = viewGroup.getHeight();
        view.setLayoutParams(new RecyclerView.LayoutParams(heightImage, RecyclerView.LayoutParams.MATCH_PARENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
                holder.image.setMaxWidth(10);
        holder.image.setMaxHeight(10);
        holder.image.setPadding(5, 5, 5, 5);
        holder.bind(mImages.get(position), holder.image, listener);

        Picasso.get()
                .load("file://"+ mImages.get(position))
                .resize(heightImage/2, heightImage/2)
                .centerCrop()
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        SquareImageView image;
        ProgressBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.gridImageView);
            this.mProgressBar = itemView.findViewById(R.id.gridImageProgressbar);
        }

        public void bind(final String item, final SquareImageView images, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, images);
                }
            });
        }
    }



}
