package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStatusHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtnamefoodstatus;
    public TextView txtnamestatus;
    public TextView txtngaydangstatus;
    public TextView statusview;
    public ImageView imageviewstatus;
    public ImageView likefoodstatus;
    public ImageView commentfoodstatus;
    public CircleImageView imageusestatus;
    private ItemClickListerner itemClickListerner;
    public ViewStatusHoder(View itemView) {
        super(itemView);
        txtnamefoodstatus = (TextView)itemView.findViewById(R.id.txtnamefoodstatus);
        txtnamestatus = (TextView)itemView.findViewById(R.id.txtnamestatus);
        txtngaydangstatus = (TextView)itemView.findViewById(R.id.txtngaydangstatus);
        statusview = (TextView)itemView.findViewById(R.id.statusview);
        imageviewstatus = (ImageView)itemView.findViewById(R.id.imageviewstatus);
        likefoodstatus = (ImageView)itemView.findViewById(R.id.likefoodstatus);
        commentfoodstatus = (ImageView)itemView.findViewById(R.id.commentfoodstatus);
        imageusestatus = (CircleImageView) itemView.findViewById(R.id.imageusestatus);
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
