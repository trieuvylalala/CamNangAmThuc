package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.Model.FoodInfomation;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class ViewPosterActivity extends AppCompatActivity {
    TextView txttenviewposte,txtviewtextnguyenlieu,txtnguyenlieuviewposte,txtviewtextcongthuc,txtcongthucviewposte,
            txtnameuseviewposte,txtngaydangviewposte,toolbar_title_viewposte;
    ImageView imgageviewposter;
    CircleImageView imguseviewposte,imgviewpostetoobar;
    String StatusId="";
    FirebaseDatabase database;
    Toolbar toolbar;
    DatabaseReference viewcommnitylist;
    ProgressDialog pDialog;
    AlertDialog dialogwaching;
    com.getbase.floatingactionbutton.FloatingActionButton fab_dowviewposter, fab_shareviewposter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_poster);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        viewcommnitylist = database.getReference("Communitys");
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewposte);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        txttenviewposte = (TextView) findViewById(R.id.txttenviewposte);
        txtviewtextnguyenlieu = (TextView) findViewById(R.id.txtviewtextnguyenlieu);
        txtnguyenlieuviewposte = (TextView) findViewById(R.id.txtnguyenlieuviewposte);
        txtviewtextcongthuc = (TextView) findViewById(R.id.txtviewtextcongthuc);
        txtcongthucviewposte = (TextView) findViewById(R.id.txtcongthucviewposte);
        txtnameuseviewposte = (TextView) findViewById(R.id.txtnameuseviewposte);
        txtngaydangviewposte = (TextView) findViewById(R.id.txtngaydangviewposte);
        toolbar_title_viewposte = (TextView) findViewById(R.id.toolbar_title_viewposte);
        imgageviewposter = (ImageView) findViewById(R.id.imgageviewposter);
        imguseviewposte = (CircleImageView) findViewById(R.id.imguseviewposte);
        imgviewpostetoobar = (CircleImageView) findViewById(R.id.imgviewpostetoobar);
        fab_dowviewposter = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_dowviewposter);
        fab_shareviewposter = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_shareviewposter);
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(ViewPosterActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if(getIntent() !=null){
                        StatusId = getIntent().getStringExtra("StatusId");
                        if (!StatusId.isEmpty()) {
                            getFoodInfomation(StatusId);
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
    private void getFoodInfomation(final String statusId){
//        txtthongtintile.setTypeface(typeface);
        viewcommnitylist.child(statusId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Community community = dataSnapshot.getValue(Community.class);
                txttenviewposte.setText(community.getNamefood());
                txtnguyenlieuviewposte.setText(community.getResourcesfood());
                txtcongthucviewposte.setText(community.getRecipefood());
                txtnameuseviewposte.setText(community.getNameusefood());
                txtviewtextnguyenlieu.setText("Nguyên liệu");
                txtviewtextcongthuc.setText("Công thức");
                txtngaydangviewposte.setText(DateFormat.format("Đã đăng:"+"(HH:mm:ss) dd-MM-yyyy", community.getTimefood()));
                Glide.with(getApplicationContext()).load(community.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(imguseviewposte);
                Glide.with(getApplicationContext()).load(Common.communityten.getImagefood()).apply(RequestOptions.circleCropTransform()).into(imgviewpostetoobar);
                Picasso.with(getBaseContext()).load(community.getImagefood()).into(imgageviewposter);
                toolbar_title_viewposte.setText(Common.communityten.getNamefood());
                toolbar_title_viewposte.setSingleLine(true);;
                toolbar_title_viewposte.setEllipsize(TextUtils.TruncateAt.END);
                fab_shareviewposter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, txttenviewposte.getText().toString());
                        sendIntent.putExtra(Intent.EXTRA_TEXT, txtnguyenlieuviewposte.getText().toString());
                        Uri bmpUri = getLocalBitmapUri(imgageviewposter);
                        sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        sendIntent.setType("image/*");
                        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivity(Intent.createChooser(sendIntent, "Chia sẽ bài viết..."));
                    }
                });
                dialogwaching.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //lấy đường dẫn hình ảnh đưa vào bitmap
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png"+".jpeg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG.JPEG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
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
}
