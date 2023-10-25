package com.dev.llamas_new.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.llamas_new.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
    private final ArrayList<String> carousel_list;
    final Context context;
    private OnClickListener onClickListener;
    public CarouselAdapter(ArrayList<String> carouselList, Context context) {
        this.carousel_list = carouselList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caroucel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.with(context).load(carousel_list.get(position)).into(holder.getImageView());
        holder.getImageView().setOnClickListener(view -> {
//                if (onClickListener != null) {
//                    onClickListener.onClick(position, carousel_list.get(position));
//                }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Category model);
    }
    @Override
    public int getItemCount() {
        return carousel_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView =  view.findViewById(R.id.carousel_image_view);


        }
        public ImageView getImageView() {
            return imageView;
        }

    }

}
