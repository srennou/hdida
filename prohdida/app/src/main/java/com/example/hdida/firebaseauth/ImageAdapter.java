package com.example.hdida.firebaseauth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hdida.firebaseauth.frags.BuyFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;


    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
        NetworkConnection networkConnection = new NetworkConnection();
        if (networkConnection.isConnectedToInternet(mContext)
                || networkConnection.isConnectedToMobileNetwork(mContext)
                || networkConnection.isConnectedToWifi(mContext)) {

        } else {
            networkConnection.showNoInternetAvailableErrorDialog(mContext);
            return;
        }

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewPrice.setText(uploadCurrent.getPrice() + " DH");
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_loading)
                .fit()
                .centerInside()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPrice;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            imageView = itemView.findViewById(R.id.image_view_upload);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BuyFragment buyFragment = new BuyFragment();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Upload current = mUploads.get(position);
                    String name = current.getName();
                    bundle.putInt("position", position);
                    bundle.putString("name", name);
                    bundle.putString("price", current.getPrice());

                    if (imageView != null)
                        bundle.putString("imageUrl", current.getImageUrl());
                    else
                        bundle.putString("imageUrl", null);
                    bundle.putString("userName", current.getUserName());
                    bundle.putString("date", current.getDate());
                    bundle.putString("desc", current.getDesc());
                    bundle.putString("email", current.getEmail());
                    bundle.putString("key", current.getKey());
                    bundle.putString("string_villes", current.getString_villes());
                    bundle.putString("string_carbu", current.getString_carbu());
                    bundle.putString("string_marque", current.getString_marque());
                    bundle.putString("string_model", current.getString_model());
                    bundle.putString("string_puissance", current.getString_puissance());
                    bundle.putString("string_boite", current.getString_boite());
                    bundle.putString("string_nbporte", current.getString_nbporte());
                    bundle.putString("string_main", current.getString_main());
                    bundle.putString("string_kilo", current.getString_kilo());
                    bundle.putString("string_annee", current.getString_annee());
                    bundle.putString("string_tel", current.getString_tel());
                    current.getSelectedOptions().toString();
                    bundle.putString("selectedOptions", current.getSelectedOptions().toString());


                    buyFragment.setArguments(bundle);


                    ((FragmentActivity) mContext)
                            .getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frag_container, buyFragment)
                            .addToBackStack(null).commit();


                }
            });
        }


    }
}
