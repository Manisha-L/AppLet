package com.W9221214.AppLet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.W9221214.AppLet.R;
import com.W9221214.AppLet.model.AppLet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppLetAdapter extends RecyclerView.Adapter<AppLetAdapter.ViewHolder> {

    private List<AppLet> appLetList;
    private int selectedPosition = 0;
    private Context context;
    private OnItemSelectedListener listener = null;
    private String currency;

    public AppLetAdapter(Context context, List<AppLet> appLetList, String currency) {
        this.context = context;
        this.appLetList = appLetList;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listining_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        AppLet appLet = appLetList.get(position);

        holder.description.setText(appLet.getLongDescription());

        int price = -1;
        try {
            price = Integer.parseInt(appLet.getPrice());
        } catch (Exception ignored) {
        }

        if (price != -1) {




                holder.price.setText("£" + price);


        } else {
            holder.price.setText(context.getString(R.string.price_not_set_yet));
        }

        holder.title.setText(appLet.getTitle());
        holder.status.setText(appLet.getStatus());
        Picasso.get().load(appLet.getPhotos().get(0)).into(holder.imageView);

        if (selectedPosition == position) {
            holder.cardView.setBackgroundColor(
                    context.getResources().getColor(R.color.colorCustomGrey));
        } else {
            holder.cardView.setBackgroundColor(
                    context.getResources().getColor(R.color.white));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                listener.onSelection(position);
            }
        });
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public int getItemCount() {
        if (appLetList == null) return 0;
        return appLetList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;
        private TextView description;
        private TextView price;
        private CardView cardView;
        private TextView status;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.listining_item_image_view);
            title = itemView.findViewById(R.id.listining_item_type);
            description = itemView.findViewById(R.id.listining_item_description);
            price = itemView.findViewById(R.id.listining_item_price);
            cardView = itemView.findViewById(R.id.listining_item_card_view);
            status = itemView.findViewById(R.id.listining_item_status);
        }
    }

    public void setOnSelectionItem(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onSelection(int position);
    }
}
