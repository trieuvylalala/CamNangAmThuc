package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.duan2.camnangamthuc.camnangamthuc.Adapter.CustomView;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.HomeViewHoderl;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.Category;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;

public class HomeUsers extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener{
    ListView listViewMenu;
    ArrayList<MenuHome> listArray = new ArrayList<>();
    CustomView customView;
    Bitmap xemdanhgiaIcon,xemtaiveIcon,gopyIcon,huongdanIcon,doimatkhauIon,thongtintkIcon,xoatkIcon,dangxuatIcon,baivietdadangIcon;
    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,HomeViewHoderl> adapter;
    ProgressDialog pDialog;
    LinearLayout gvcamnang,gvcongdong;
    TextView txtloginuse;
    ImageView imgloginuse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_homeuses);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //Khai báo Firebase
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        category = database.getReference("Category");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutuse);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewuse);
        navigationView.setNavigationItemSelectedListener(this);
        txtloginuse  = (TextView) findViewById(R.id.txtviewuse);
        imgloginuse  = (ImageView)findViewById(R.id.imgloginuse) ;
        txtloginuse.setText(Common.userten.getName());
        Picasso.with(getBaseContext()).load(Common.userten.getImage()).into(imgloginuse);
        Paper.init(this);
        //khai báo listview menu
        thongtintkIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.thongtintaikhoang);
        listArray.add(new MenuHome("Thông tin tài khoảng",thongtintkIcon));
        doimatkhauIon = BitmapFactory.decodeResource(this.getResources(),R.drawable.doimatkhau);
        listArray.add(new MenuHome("Đổi mật khẩu",doimatkhauIon));
        baivietdadangIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.baiviet);
        listArray.add(new MenuHome("Bài viết đã đăng",baivietdadangIcon));
        xemdanhgiaIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.danhgia);
        listArray.add(new MenuHome("Xem đánh dấu",xemdanhgiaIcon));
        xemtaiveIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.taive);
        listArray.add(new MenuHome("Xem tải về",xemtaiveIcon));
        gopyIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.icongmail);
        listArray.add(new MenuHome("Góp ý",gopyIcon));
        xoatkIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.xoatk);
        listArray.add(new MenuHome("Xóa tài khoảng",xoatkIcon));
        huongdanIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.huongdan);
        listArray.add(new MenuHome("Hướng dẫn",huongdanIcon));
        dangxuatIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.dangxau);
        listArray.add(new MenuHome("Đăng xuất",dangxuatIcon));
        //thêm vào adapter
        listViewMenu = (ListView) findViewById(R.id.listviewmenuuse);
        customView = new CustomView(this,R.layout.dolistviewmenu,listArray);
        listViewMenu.setAdapter(customView);
        listViewMenu.setOnItemClickListener(this);
        //Load dữ liệu ra home
        recyclerView = (RecyclerView)findViewById(R.id.recyMenuhomeuse);
      /*  layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/
        recyclerView.setLayoutManager(new GridLayoutManager(this ,2));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_fall);
        recyclerView.setLayoutAnimation(controller);
        gvcamnang =(LinearLayout)findViewById(R.id.gv_camnaguse);
        gvcamnang.setBackgroundResource(R.drawable.bachgrounk_item_list);
        gvcamnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeUsers.this,HomeUsers.class);
                startActivity(intent);

            }
        });
        gvcongdong =(LinearLayout)findViewById(R.id.gv_congdonguse);
        gvcongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomeUsers.this,CongDongActivity.class);
                startActivity(intent2);
            }
        });

        //kiểm tra xem đã kết nối internet hay chưa
        //khi có internet sẽ khởi chạy ViewFlipper và ActionBar
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            pDialog = new ProgressDialog(HomeUsers.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Đang tải dữ liệu...");
            pDialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    loadMenu();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui lòng kết nối internet");
        }
    }
    //lấy dữ liệu tên và img đổ ra màng hình
    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, HomeViewHoderl>(Category.class,R.layout.item_menu,HomeViewHoderl.class,category) {
            @Override
            protected void populateViewHolder(HomeViewHoderl viewHolder, Category model, int position) {
                viewHolder.txtMenụView.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imgMenuView);
                final Category clickitem = model;
                //sử lý sự kiện click cho dư liệu đổ về
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get id của Category và gửi qua activity mới
                        //Log.d("", "onClick: " +adapter.getRef(position).getKey());
                        Intent foodIntent = new Intent(HomeUsers.this,FoodActivity.class);
                        Common.categorygetten = clickitem;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodIntent.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodIntent);
                        //Toast.makeText(Home.this, ""+adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(HomeUsers.this,HomeAdmin.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //send mail góp ý
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"buivietphii@gmail.com"};
        String[] CC = {"buivietphii@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:buivietphii@gmail.com"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Thư Góp Ý");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin chào nhà phát triển App Cẩm Nang Ẩm Thực");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(HomeUsers.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    //sử lý sự kiện click cho listview Menu
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:

                break;
            case 1:
               /* Intent intent2 = new Intent(ChemGioActiviti.this, HuCauActiviti.class);
                startActivity(intent2);*/
                break;
            case 2:
                break;
            case 5:
                sendEmail();
                break;
            case 8:
                Paper.book().destroy();
                Intent longoutent = new Intent(HomeUsers.this,Home.class);
                longoutent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(longoutent);
                break;
        }
    }
}