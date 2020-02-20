package com.example.contactsproviderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsproviderapp.R;
import com.example.contactsproviderapp.model.NumberItem;

import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {
    List<NumberItem> numberItems;
    Context context;

    public NumberAdapter(List<NumberItem> numberItems, Context context) {
        this.numberItems = numberItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_number, parent, false);

        return new NumberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NumberItem numberItem = numberItems.get(position);
        holder.tvContactNumber.setText(numberItem.getContactItemNumber());
    }

    @Override
    public int getItemCount() {
        return numberItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvContactNumber;

          ViewHolder(View itemView) {
              super(itemView);
              tvContactNumber = itemView.findViewById(R.id.tvNumber);
          }
      }
}
