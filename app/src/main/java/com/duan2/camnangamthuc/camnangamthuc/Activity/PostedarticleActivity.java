package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.FoodViewHoder;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewCommunityUse;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewStatusHoder;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.Model.Food;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class PostedarticleActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference statuslist;
    String statusEmail="";
    Toolbar toolbar;
    FirebaseRecyclerAdapter<Community,ViewStatusHoder> adapter;
    ProgressDialog pDialog;
    AlertDialog dialogwaching;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postedarticle);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        statuslist = database.getReference("Communitys");
        toolbar = (Toolbar) findViewById(R.id.toolbar_status);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recystatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(PostedarticleActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if(getIntent() !=null){
                        statusEmail = getIntent().getStringExtra("StatusEmail");
                        if (!statusEmail.isEmpty()) {
                            loadliststatus(statusEmail);
                        }
                    }
                    ChayToolBar();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui lòng kết nối internet");
        }
    }
    private void ChayToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menutt) {
            Intent intent = new Intent(PostedarticleActivity.this,HomeUsers.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //đọc dữ liệu bai viết đã đăng
    private void loadliststatus(String statusEmail) {
        adapter = new FirebaseRecyclerAdapter<Community,ViewStatusHoder>(Community.class,R.layout.item_view_status,ViewStatusHoder.class,
                statuslist.orderByChild("emailusefood").equalTo(statusEmail)){//tìm kiếm : select * from Food where emailusefood
            @Override
            protected void populateViewHolder(ViewStatusHoder viewHolder, Community model, int position) {
                viewHolder.txtnamefoodstatus.setText(model.getNamefood());
                viewHolder.txtnamefoodstatus.setMaxLines(1);
                viewHolder.txtnamefoodstatus.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtnamestatus.setText(model.getNameusefood());
                viewHolder.txtngaydangstatus.setText(DateFormat.format("Đã đăng:"+"(HH:mm:ss) dd-MM-yyyy", model.getTimefood()));
                viewHolder.statusview.setText(model.getStatusfood());
                if(viewHolder.statusview.getText().toString().equalsIgnoreCase("Đang chờ phê duyệt")){
                    viewHolder.statusview.setTextColor(Color.parseColor("#f10619"));
                }else {
                    viewHolder.statusview.setTextColor(Color.parseColor("#42D752"));
                }
                Picasso.with(getBaseContext()).load(model.getImagefood()).into(viewHolder.imageviewstatus);
                Glide.with(getApplicationContext()).load(model.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imageusestatus);
                final Community community = model;
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoIntent = new Intent(PostedarticleActivity.this,ViewPosterActivity.class);
                        Common.communityten = community;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("StatusId",adapter.getRef(position).getKey());
                        startActivity(foodinfoIntent);
                    }
                });
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialogwaching.dismiss();
    }
}
