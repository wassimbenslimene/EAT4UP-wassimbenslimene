package com.example.myapplication6.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication6.Interface.ItemClickListener;
import com.example.myapplication6.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtOrderId,txtOrderEmail,txtOrderTable,txtOrderPrice;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderEmail = (TextView) itemView.findViewById(R.id.order_email);
        txtOrderPrice = (TextView) itemView.findViewById(R.id.order_Price);
        txtOrderTable = (TextView) itemView.findViewById(R.id.order_table);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
          itemClickListener.onclick(view,getAdapterPosition(),false);
    }


}
