package com.example.android.advertisers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.ViewHolder>{

    private List<AdvertismentClass> ads;
    private Context context;

    public RecyclerViewAdapterClass(List<AdvertismentClass> ads, Context context) {
        this.ads = ads;
        this.context = context;
    }

    @NonNull
    @Override
    // Called whenever a ViewHolder instance is created
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    // Called after calling the above onCreateViewHolder
    // Binds the Recycler View with data
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdvertismentClass ad = ads.get(position);

        holder.adTitle.setText(ad.getTitle());
        holder.adDesc.setText(ad.getDescription());

        Picasso.with(context).load(ad.getImgURL()).into(holder.adImg);
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView adImg;
        public TextView adTitle;
        public TextView adDesc;

        public ViewHolder(View itemView) {
            super(itemView);

            adImg = (ImageView) itemView.findViewById(R.id.adImg);
            adTitle = (TextView) itemView.findViewById(R.id.adTitle);
            adDesc = (TextView) itemView.findViewById(R.id.adDesc);
        }
    }
}
