package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewComment;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewCommunityUse;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Comment;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Date;

import dmax.dialog.SpotsDialog;

public class CommentActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference commentlist;
    AlertDialog.Builder builder;
    AlertDialog b;
    AlertDialog dialogwaching;
    ProgressDialog pDialog;
    EditText inputcomment;
    FloatingActionButton fadsendcomment;
    String commentId = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Comment,ViewComment> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        commentlist = database.getReference("Comments");
        inputcomment = (EditText) findViewById(R.id.inputcomment);
        fadsendcomment = (FloatingActionButton) findViewById(R.id.fadsendcomment);
        fadsendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postcomment();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.list_of_comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(CommentActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    if (getIntent() != null) {
                        commentId = getIntent().getStringExtra("commentId");
                        if (!commentId.isEmpty()) {
                            loadComment(commentId);
                        }
                    }
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui lòng kết nối internet");
        }
    }

    private void loadComment(String commentId) {
        adapter = new FirebaseRecyclerAdapter<Comment,ViewComment>(Comment.class,R.layout.item_commnent_list,ViewComment.class,
                commentlist.orderByChild("commentId").equalTo(commentId)){//tìm kiếm : select * from Food where emailusefood
            @Override
            protected void populateViewHolder(ViewComment viewHolder, final Comment model, final int position) {
                viewHolder.comment_user.setText(model.getNameusecomment());
                viewHolder.comment_test.setText(model.getNamecomment());
                viewHolder.comment_time.setText(DateFormat.format("(HH:mm:ss) dd-MM-yyyy", model.getTimecomment()));
                Glide.with(getApplicationContext()).load(model.getImageusecomment()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imgcomment);
                final Comment comment = model;
                //nếu như email của tài khoản giống với email của bình luận thì sẽ hiễn thị 2 chức năng xóa và sửa
                if(Common.userten.getEmail().equals(model.getEmailusecomment())){
                    viewHolder.detele_comment.setVisibility(View.VISIBLE);
                    viewHolder.edit_comment.setVisibility(View.VISIBLE);
                    //button sửa
                    viewHolder.edit_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    //buttom xóa
                    viewHolder.detele_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String xoa = "Bạn có muốn xóa bình luận của <font color='green'> <Strong>"+"mình" + "</Strong></font> không";
                            AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommentActivity.this);
                            dialogxoa.setTitle("Xóa bình luận");
                            dialogxoa.setIcon(R.drawable.ic_deletestatus);
                            dialogxoa.setMessage(Html.fromHtml(xoa));
                            dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //lấy vị trí hiện tại
                                    deletecomment(adapter.getRef(position).getKey());
                                }
                            });
                            dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dialogxoa.show();
                        }
                    });
                    //ngược lại email của tài khoản không giống với email của bình luận thì sẽ ẩn 2 chức năng xóa và sửa
                }else {
                    viewHolder.detele_comment.setVisibility(View.INVISIBLE);
                    viewHolder.edit_comment.setVisibility(View.INVISIBLE);
                }
                //nếu như email của tài khoản giống với email của bài viết thì sẽ hiễn thị 2 chức năng xóa và sửa
                if (Common.userten.getEmail().equals(Common.communityten.getEmailusefood())){
                    viewHolder.detele_comment.setVisibility(View.VISIBLE);
                    viewHolder.edit_comment.setVisibility(View.VISIBLE);
                    viewHolder.detele_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String xoa = "Bạn có muốn xóa bình luận của <font color='green'> <Strong>"+model.getNameusecomment() + "</Strong></font> không";
                            AlertDialog.Builder dialogxoa = new AlertDialog.Builder(CommentActivity.this);
                            dialogxoa.setTitle("Xóa bình luận");
                            dialogxoa.setIcon(R.drawable.ic_deletestatus);
                            dialogxoa.setMessage(Html.fromHtml(xoa));
                            dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //lấy vị trí hiện tại
                                    deletecomment(adapter.getRef(position).getKey());
                                }
                            });
                            dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dialogxoa.show();
                        }
                    });
                }
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialogwaching.dismiss();
    }

    private void deletecomment(String key) {
        commentlist.child(key).removeValue();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }

    //post comment
    private void postcomment(){
        pDialog = new ProgressDialog(CommentActivity.this);
        pDialog.setTitle("Đang đăng bài");
        pDialog.show();
        Comment comment = new Comment();
        String nameusecomment=  Common.userten.getName();
        String emailusecomment =Common.userten.getEmail();
        String imageusecomment =Common.userten.getImage();
        String namefoodcomment =Common.communityten.getNamefood();
        String commentid =Common.communityten.getId();
        String id = commentlist.push().getKey();
        long timecomment = new Date().getTime();
        comment.setId(id);
        comment.setNamecomment(inputcomment.getText().toString());
        comment.setNamefoodcomment(namefoodcomment);
        comment.setCommentId(commentid);
        comment.setNameusecomment(nameusecomment);
        comment.setEmailusecomment(emailusecomment);
        comment.setImageusecomment(imageusecomment);
        comment.setTimecomment(timecomment);
        if(comment !=null){
            commentlist.child(id).setValue(comment);
        }
        inputcomment.setText("");
        Toast.makeText(CommentActivity.this, "Cảm ơn bạn đã bình luận", Toast.LENGTH_SHORT).show();
        pDialog.dismiss();
    }
}
