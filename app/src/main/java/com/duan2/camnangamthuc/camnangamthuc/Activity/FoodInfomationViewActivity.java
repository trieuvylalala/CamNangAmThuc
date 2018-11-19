package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.FoodInfomation;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class FoodInfomationViewActivity extends AppCompatActivity {
    TextView foodinfoview_name,foodinfoview_info,foodinfoview_infoview,foodinfoview_happy;
    ImageView foodinfoview_imginfo,foodinfoview_imginfoview;
    String FoodInfomationId="";
    FirebaseDatabase database;
    Toolbar toolbar;
    DatabaseReference foodInfomationviewlist;
    ProgressDialog pDialog;
    com.getbase.floatingactionbutton.FloatingActionButton fab_dow, fab_face, fab_share;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(sharePhotoContent);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_food_infomation_view);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        foodInfomationviewlist = database.getReference("FoodInfomation");
        toolbar = (Toolbar) findViewById(R.id.toolbar_chitiet);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        foodinfoview_name = (TextView) findViewById(R.id.titleviewchitiet);
        foodinfoview_info = (TextView) findViewById(R.id.infochitiet);
        foodinfoview_infoview = (TextView) findViewById(R.id.infoviewchitiet);
        foodinfoview_happy = (TextView) findViewById(R.id.happychitiet);
        foodinfoview_imginfo = (ImageView) findViewById(R.id.imginfo);
        foodinfoview_imginfoview = (ImageView) findViewById(R.id.imginfoview);
        fab_dow = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_dow);
        fab_face = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_face);
        fab_share = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_share);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            pDialog = new ProgressDialog(FoodInfomationViewActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Đang tải dữ liệu...");
            pDialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if(getIntent() !=null){
                        FoodInfomationId = getIntent().getStringExtra("FoodInfomationId");
                        if (!FoodInfomationId.isEmpty()) {
                            getFoodInfomation(FoodInfomationId);
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
    //lấy key từ fb
    private void keyhast(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.duan2.camnangamthuc.camnangamthuc", PackageManager
                    .GET_SIGNATURES);
            for(android.content.pm.Signature signature:info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHashFB",Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
    private void getFoodInfomation(final String foodinfomationId){
        final TextView txtthongtintile;
//        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/JustDieAlready.ttf");
        txtthongtintile = (TextView)findViewById(R.id.toolbar_title_chitiet);
//        txtthongtintile.setTypeface(typeface);
        foodInfomationviewlist.child(foodinfomationId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final FoodInfomation foodInfomation = dataSnapshot.getValue(FoodInfomation.class);
                foodinfoview_name.setText(foodInfomation.getName());
                foodinfoview_info.setText(foodInfomation.getInfomation());
                Picasso.with(getBaseContext()).load(foodInfomation.getImage()).into(foodinfoview_imginfo);
                foodinfoview_infoview.setText(Html.fromHtml(foodInfomation.getInfomationView()));
                Picasso.with(getBaseContext()).load(foodInfomation.getImageView()).into(foodinfoview_imginfoview);
                foodinfoview_happy.setText(Html.fromHtml(foodInfomation.getHappy()));
                txtthongtintile.setText(Common.foodinfogetten.getName());
                txtthongtintile.setSingleLine(true);;
                txtthongtintile.setEllipsize(TextUtils.TruncateAt.END);
                fab_face.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getBaseContext()).load(foodInfomation.getImage()).into(target);
                    }
                });
                fab_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, foodinfoview_name.getText().toString());
                        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(foodinfoview_infoview.getText().toString()));
                        Uri bmpUri = getLocalBitmapUri(foodinfoview_imginfoview);
                        sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        sendIntent.setType("image/*");
                        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivity(Intent.createChooser(sendIntent, "Chia sẽ bài viết..."));
                    }
                });
                pDialog.dismiss();
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
