package com.example.contactsproviderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsproviderapp.R;
import com.example.contactsproviderapp.model.EmailItem;

import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    List<EmailItem> emailItems;
    Context context;

    public EmailAdapter(List<EmailItem> emailItems, Context context) {
        this.emailItems = emailItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_email, parent, false);

        return new EmailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmailItem emailItem = emailItems.get(position);
        holder.tvContactEmail.setText(emailItem.getContactItemEmail());
    }

    @Override
    public int getItemCount() {
        return emailItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContactEmail;

        ViewHolder(View itemView) {
            super(itemView);
            tvContactEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}
