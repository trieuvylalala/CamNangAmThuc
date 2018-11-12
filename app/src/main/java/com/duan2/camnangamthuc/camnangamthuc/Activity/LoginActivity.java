package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText editloginemail,editloginpassword;
    Button bntlogin,bntchanreque;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference listuser;
    ProgressDialog pDialog;
    CheckBox checkBoxghinho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editloginemail = (EditText) findViewById(R.id.edtEmaillogin);
        editloginpassword = (EditText)findViewById(R.id.edtpasswordlogin);
        bntlogin = (Button) findViewById(R.id.btnlogin);
        bntchanreque = (Button) findViewById(R.id.btnLinkToLoginScreen);
        checkBoxghinho = (CheckBox) findViewById(R.id.checkghinho);
        Paper.init(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        listuser = firebaseDatabase.getReference("Users");
                bntchanreque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent requeIntent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(requeIntent);
            }
        });
                bntlogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkBoxghinho.isChecked()){
                            Paper.book().write(Common.USE_KEY,editloginemail.getText().toString());
                            Paper.book().write(Common.PAW_KEY,editloginpassword.getText().toString());
                        }
                        pDialog = new ProgressDialog(LoginActivity.this);
                        pDialog.setTitle("Đăng nhập");
                        pDialog.setMessage("Vui lòng đợi...");
                        pDialog.show();
                        listuser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                pDialog.dismiss();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Users users = postSnapshot.getValue(Users.class);
                                    //so sánh dữ liệu từ database lấy về  với sự kiện gõ lên
                                    //sự kiện gõ lên không trùng với database
                                    if (!users.getEmail().equals(editloginemail.getText().toString()) &&
                                            !users.getPassword().equals(editloginpassword.getText().toString())) {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                                    //ngược lại trùng
                                    }else {
                                        //so sánh dữ liệu từ database lấy về  với sự kiện gõ lên
                                        //truyền vào 1 hàm equalsIgnoreCase lấy ra từ 1 biến trong database
                                        //biến truyền về từ database là admin
                                        if (users.getEmail().equals(editloginemail.getText().toString()) &&
                                                users.getPassword().equals(editloginpassword.getText().toString())
                                                && users.getRole().equalsIgnoreCase("admin")) {
                                            Intent adminIntent = new Intent(LoginActivity.this, HomeAdmin.class);
                                            Common.userten = users;
                                            //chuyển qua tab mới//đóng tab hiện tại
                                            adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(adminIntent);
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        }
                                        //so sánh dữ liệu từ database lấy về  với sự kiện gõ lên
                                        //truyền vào 1 hàm equalsIgnoreCase lấy ra từ 1 biến trong database
                                        //biến truyền về từ database là use
                                        if (users.getEmail().equals(editloginemail.getText().toString()) &&
                                                users.getPassword().equals(editloginpassword.getText().toString())
                                                && users.getRole().equalsIgnoreCase("user")) {
                                            Intent userIntent = new Intent(LoginActivity.this, HomeUsers.class);
                                            Common.userten = users;
                                            userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(userIntent);
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
    }
}
