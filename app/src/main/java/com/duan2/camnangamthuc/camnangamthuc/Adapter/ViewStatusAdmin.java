package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStatusAdmin  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtnamefoodstatusadmin;
    public TextView txtnamestatusadmin;
    public TextView txtngaydangstatusadmin;
    public ImageView imageviewstatusadmin;
    public ImageView likefoodstatusadmin;
    public ImageView commentfoodstatusadmin;
    public ImageView deletestatusadmin;
    public CircleImageView imageusestatusadmin;
    private ItemClickListerner itemClickListerner;
    public ViewStatusAdmin(View itemView) {
        super(itemView);
        txtnamefoodstatusadmin = (TextView)itemView.findViewById(R.id.txtnamefoodstatusadmin);
        txtnamestatusadmin = (TextView)itemView.findViewById(R.id.txtnamestatusadmin);
        txtngaydangstatusadmin = (TextView)itemView.findViewById(R.id.txtngaydangstatusadmin);
        imageviewstatusadmin = (ImageView)itemView.findViewById(R.id.imageviewstatusadmin);
        likefoodstatusadmin = (ImageView)itemView.findViewById(R.id.likefoodstatus);
        commentfoodstatusadmin = (ImageView)itemView.findViewById(R.id.commentfoodstatusadmin);
        deletestatusadmin = (ImageView)itemView.findViewById(R.id.deletestatusadmin);
        imageusestatusadmin = (CircleImageView) itemView.findViewById(R.id.imageusestatusadmin);
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
