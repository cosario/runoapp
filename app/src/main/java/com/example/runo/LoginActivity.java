package com.example.runo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    //UI views
    private EditText emailEt,passwordEt;
    private TextView forgotIv,noAccountIv;
    private Button loginBtn;

    //firebase auth
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init of UI here
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        forgotIv = findViewById(R.id.forgotIv);
        loginBtn = findViewById(R.id.loginBtn);
        noAccountIv = findViewById(R.id.noAccountIv);

        // init auth array
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        noAccountIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterUserActivity.class));
            }
        });

            forgotIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                }
            });

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUser();
                }
            });
    }
        private String email,password;
        private  void loginUser(){
               email = emailEt.getText().toString().trim();
               password = passwordEt.getText().toString().trim();

               if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                   Toast.makeText(this, "Inavalid Email Pattern", Toast.LENGTH_SHORT).show();
                   return;
               }
               if(TextUtils.isEmpty(password)){
                   Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                   return;
               }

               progressDialog.setMessage("Logging In..");
               progressDialog.show();

               firebaseAuth.signInWithEmailAndPassword(email,password)
                       .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authResult) {
                              //logged in success
                               makeMeOnline();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                              //failed to logging in
                               progressDialog.dismiss();
                               Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
        }

        private void makeMeOnline(){
           //after logging in make user online
           progressDialog.setMessage("Checking User..");

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("online","true");

            //Update Values to DB
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //update success
                            checkUserType();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            //update fail
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        private void checkUserType(){
        //if user or a seller
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                String  accountType = ""+ds.child("accountType").getValue();
                                if(accountType.equals("Seller")){
                                    progressDialog.dismiss();
                                    //user is seller
                                    startActivity(new Intent(LoginActivity.this,MainSellerActivity.class));
                                    finish();
                                }
                                else{
                                    //user is client or buyer
                                    progressDialog.dismiss();
                                    startActivity(new Intent(LoginActivity.this,MainUserActivity.class));
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });

        }


}