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

public class LoginActivity extends AppCompatActivity {
    EditText editloginemail,editloginpassword;
    Button bntlogin,bntchanreque;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference listuser;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editloginemail = (EditText) findViewById(R.id.edtEmaillogin);
        editloginpassword = (EditText)findViewById(R.id.edtpasswordlogin);
        bntlogin = (Button) findViewById(R.id.btnlogin);
        bntchanreque = (Button) findViewById(R.id.btnLinkToLoginScreen);
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

                                    if (users.getEmail().equals(editloginemail.getText().toString()) &&
                                            users.getPassword().equals(editloginpassword.getText().toString())
                                            && users.getRole().equalsIgnoreCase("admin")) {
                                        Intent adminIntent = new Intent(LoginActivity.this, HomeAdmin.class);
                                        Common.userten  = users;
                                        adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(adminIntent);
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    if (users.getEmail().equals(editloginemail.getText().toString()) &&
                                            users.getPassword().equals(editloginpassword.getText().toString())
                                            && users.getRole().equalsIgnoreCase("user")) {
                                        Intent userIntent = new Intent(LoginActivity.this, HomeUsers.class);
                                        Common.userten  = users;
                                        userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(userIntent);
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
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
