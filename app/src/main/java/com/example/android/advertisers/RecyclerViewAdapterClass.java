package com.example.android.advertisers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.ViewHolder> {
    private View.OnClickListener mOnClickListener;

    private List<AdvertismentClass> ads;
    private Context context;
    private RecyclerView mRecyclerView;

    public RecyclerViewAdapterClass(List<AdvertismentClass> ads, Context context) {
        this.ads = ads;
        this.context = context;
    }

    @NonNull
    @Override
    // Called whenever a ViewHolder instance is created
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false);

        mOnClickListener  = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                AdvertismentClass item = ads.get(itemPosition);
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, EditAdvActivity.class);
                i.putExtra("adPosition", itemPosition);
                context.startActivity(i);
            }
        };
        v.setOnClickListener(mOnClickListener);
        return new ViewHolder(v);
    }

    @Override
    // Called after calling the above onCreateViewHolder
    // Binds the Recycler View with data
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdvertismentClass ad = ads.get(position);

        holder.adTitle.setText(ad.getTitle());
        holder.adDesc.setText(ad.getDescription());

//        Log.d("Show Ad Img",ad.getImgURL());
        Picasso.with(context).load(ad.getImgURL()).into(holder.adImg);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
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
