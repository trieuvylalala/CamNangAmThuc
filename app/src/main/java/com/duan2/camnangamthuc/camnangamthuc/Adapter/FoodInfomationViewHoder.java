package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.FoodInfomation;
import com.duan2.camnangamthuc.camnangamthuc.R;

import java.util.List;

public class FoodInfomationViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtInfomationViewName;
    public TextView txtInfomationViewInfo;
    public ImageView imgFoodInfomationView;
    private ItemClickListerner itemClickListerner;
    public FoodInfomationViewHoder(View itemView) {
        super(itemView);
        txtInfomationViewName = (TextView)itemView.findViewById(R.id.txttitle);
        txtInfomationViewInfo = (TextView)itemView.findViewById(R.id.txtinfo);
        imgFoodInfomationView = (ImageView)itemView.findViewById(R.id.imghinhanh);
        itemView.setOnClickListener(this);
    }
    public void setItemListener(ItemClickListerner itemClickListerner){
        this.itemClickListerner = itemClickListerner;
    }
    @Override
    public void onClick(View view) {
        itemClickListerner.onClick(view,getAdapterPosition(),false);
    }
}
