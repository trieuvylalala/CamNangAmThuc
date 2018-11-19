package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewCommunityUse extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtnamefoodcongdong;
    public TextView txtnamecongdonguse;
    public TextView txtngaydangcongdong;
    public ImageView viewimagecongdonguse;
    public ImageView likefood;
    public ImageView commentfood;
    public CircleImageView imageusecongdong;
    private ItemClickListerner itemClickListerner;
    public ViewCommunityUse(View itemView) {
        super(itemView);
        txtnamefoodcongdong = (TextView)itemView.findViewById(R.id.txtnamefoodcongdong);
        txtnamecongdonguse = (TextView)itemView.findViewById(R.id.txtnamecongdonguse);
        txtngaydangcongdong = (TextView)itemView.findViewById(R.id.txtngaydangcongdong);
        viewimagecongdonguse = (ImageView)itemView.findViewById(R.id.viewimagecongdonguse);
        likefood = (ImageView)itemView.findViewById(R.id.likefood);
        commentfood = (ImageView)itemView.findViewById(R.id.commentfood);
        imageusecongdong = (CircleImageView) itemView.findViewById(R.id.imageusecongdong);
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
