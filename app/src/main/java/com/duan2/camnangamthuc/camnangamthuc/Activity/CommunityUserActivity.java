package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.CustomView;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.HomeViewHoderl;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.Category;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class CommunityUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener{
    ListView listViewMenu;
    ArrayList<MenuHome> listArray = new ArrayList<>();
    CustomView customView;
    Bitmap xemdanhgiaIcon,xemtaiveIcon,gopyIcon,huongdanIcon,doimatkhauIon,thongtintkIcon,dangxuatIcon,baivietdadangIcon;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,HomeViewHoderl> adapter;
    ProgressDialog pDialog;
    LinearLayout gvcamnang,gvcongdong;
    TextView txtviewcongdonguse;
    CircleImageView imglogincongdonguse;
    AlertDialog.Builder builder;
    AlertDialog b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_congdonguser);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutcongdonguse);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewcongdonguse);
        navigationView.setNavigationItemSelectedListener(this);
        txtviewcongdonguse  = (TextView) findViewById(R.id.txtviewcongdonguse);
        imglogincongdonguse  = (CircleImageView)findViewById(R.id.imglogincongdonguse) ;
        txtviewcongdonguse.setText(Common.userten.getName());
        /*        Picasso.with(getBaseContext()).load(Common.userten.getImage()).into(imgloginuse);*/
        Glide.with(getApplicationContext()).load(Common.userten.getImage()).apply(RequestOptions.circleCropTransform()).into(imglogincongdonguse);
        Paper.init(this);
        //khai báo listview menu
        thongtintkIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.thongtintaikhoang);
        listArray.add(new MenuHome("Thông tin tài khoản",thongtintkIcon));
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
        huongdanIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.huongdan);
        listArray.add(new MenuHome("Hướng dẫn",huongdanIcon));
        dangxuatIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.dangxau);
        listArray.add(new MenuHome("Đăng xuất",dangxuatIcon));
        //thêm vào adapter
        listViewMenu = (ListView) findViewById(R.id.listviewcongdonguse);
        customView = new CustomView(this,R.layout.dolistviewmenu,listArray);
        listViewMenu.setAdapter(customView);
        listViewMenu.setOnItemClickListener(this);
        //Load dữ liệu ra home
/*        recyclerView = (RecyclerView)findViewById(R.id.recyMenuhomeuse);
      *//*  layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*//*
        recyclerView.setLayoutManager(new GridLayoutManager(this ,2));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_fall);
        recyclerView.setLayoutAnimation(controller);*/
        gvcamnang =(LinearLayout)findViewById(R.id.gv_camnagusepaster);
        gvcamnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CommunityUserActivity.this,HomeUsers.class);
                startActivity(intent);

            }
        });
        gvcongdong =(LinearLayout)findViewById(R.id.gv_congdongusepaster);
        gvcongdong.setBackgroundResource(R.drawable.bachgrounk_item_list);
        gvcongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(CommunityUserActivity.this,CommunityUserActivity.class);
                startActivity(intent2);
            }
        });
    }
    //lấy dữ liệu tên và img đổ ra màng hình

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
            Intent intent = new Intent(CommunityUserActivity.this,HomeAdmin.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_seach) {
            builder = new AlertDialog.Builder(CommunityUserActivity.this);
            builder.setTitle("Nhập tên món cần tìm");
            builder.setMessage("Viết hoa chữ cái đầu tiên || Nhập tên món phải có dấu");
            LayoutInflater layoutInflater = CommunityUserActivity.this.getLayoutInflater();
            final View sendcode = layoutInflater.inflate(R.layout.item_seach, null);
            final EditText editseach = (EditText) sendcode.findViewById(R.id.edtseachname);
            final Button bntthoat = (Button) sendcode.findViewById(R.id.btn_thoat);
            final Button bnttim = (Button) sendcode.findViewById(R.id.btn_tim);
            builder.setView(sendcode);
            final AlertDialog b = builder.create();
            b.show();
            bntthoat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    b.dismiss();
                }
            });
            bnttim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String seach = editseach.getText().toString();
                    if (seach.isEmpty()) {
                        editseach.setError("Vui lòng nhập tên cần tim");
                        editseach.requestFocus();
                        return;
                    } else {
                        Intent foodinfoIntent = new Intent(CommunityUserActivity.this, LoadSeachActivity.class);
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("KeyGet", seach);
                        startActivity(foodinfoIntent);
                        b.dismiss();
                    }
                }
            });
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
            Toast.makeText(CommunityUserActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    //sử lý sự kiện click cho listview Menu
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Intent account = new Intent(CommunityUserActivity.this,AccountinformationActivity.class);
                account.putExtra("KeyEmail", Common.userten.getEmail());
                startActivity(account);
                break;
            case 1:
                builder = new AlertDialog.Builder(CommunityUserActivity.this);
                builder.setTitle("Đổi mật khẩu");
                builder.setMessage("Nhập mật khẩu");
                LayoutInflater layoutInflater = CommunityUserActivity.this.getLayoutInflater();
                final View viewdoimk = layoutInflater.inflate(R.layout.item_changpass_shaw,null);
                final EditText editmkcu = (EditText) viewdoimk.findViewById(R.id.editmkcu);
                final EditText editmkmoi = (EditText) viewdoimk.findViewById(R.id.editmkmoi);
                final EditText editmkmoinl = (EditText) viewdoimk.findViewById(R.id.editmkmoinl);
                final Button bntsendmk = (Button) viewdoimk.findViewById(R.id.btnsenddoimk);
                builder.setView(viewdoimk);
                builder.setIcon(R.drawable.ic_vpn_key_black_24dp);
                b = builder.create();
                b.show();
                bntsendmk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog dialogwaching = new SpotsDialog(CommunityUserActivity.this);
                        dialogwaching.show();
                        if(editmkcu.getText().toString().equals(Common.userten.getPassword())) {
                            if (editmkmoi.getText().toString().equals(editmkmoinl.getText().toString())) {
                                final Map<String, Object> doimatkhau = new HashMap<>();
                                doimatkhau.put("password", editmkmoi.getText().toString());
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            postSnapshot.getValue(Users.class);
                                            reference.child(postSnapshot.getKey()).updateChildren(doimatkhau)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            b.dismiss();
                                                            dialogwaching.dismiss();
                                                            Toast.makeText(CommunityUserActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CommunityUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                b.dismiss();
                                dialogwaching.dismiss();
                                Toast.makeText(CommunityUserActivity.this, "Đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            b.dismiss();
                            dialogwaching.dismiss();
                            Toast.makeText(CommunityUserActivity.this, "Mật khẩu không chính xát", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 2:
                break;
            case 5:
                sendEmail();
                break;
            case 7:
                Paper.book().destroy();
                Intent longoutent = new Intent(CommunityUserActivity.this,CongDongActivity.class);
                longoutent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(longoutent);
                break;
        }
    }
}
