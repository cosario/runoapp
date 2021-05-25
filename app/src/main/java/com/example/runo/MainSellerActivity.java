package com.example.runo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainSellerActivity extends AppCompatActivity {

    private TextView nameIv,shopNameIv,emailIv,tabProductsIv,tabOrdersIv;
    private ImageButton logoutBtn,editProfileBtn,addProductBtn;
    private ImageView profileIv;
    private RelativeLayout productsRl,ordersRl;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        nameIv = findViewById(R.id.nameIv);
        shopNameIv = findViewById(R.id.shopNameIv);
        emailIv = findViewById(R.id.emailIv);
        logoutBtn = findViewById(R.id.logoutBtn);
        profileIv = findViewById(R.id.profileIv);
        tabProductsIv = findViewById(R.id.tabProductsIv);
        tabOrdersIv = findViewById(R.id.tabOrdersIv);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        addProductBtn = findViewById(R.id.addProducttBtn);
        productsRl = findViewById(R.id.productsRl);
        ordersRl = findViewById(R.id.ordersRl);



        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();
        showProductsUi();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMeOffline();

            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit profiel activity
                startActivity(new Intent(MainSellerActivity.this,ProfileEditSellerActivity.class));
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open add product activity
                startActivity(new Intent(MainSellerActivity.this,AddProductActivity.class));
            }
        });

        tabProductsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tab for products
                showProductsUi();
            }
        });

        tabOrdersIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tab for orders
                showOrdersUi();
            }
        });

    }

    private void showProductsUi(){
        //show products hide orders ui
        productsRl.setVisibility(View.VISIBLE);
        ordersRl.setVisibility(View.GONE);

        tabProductsIv.setTextColor(getResources().getColor(R.color.black));
        tabProductsIv.setBackgroundResource(R.drawable.shape_tab02);

        tabOrdersIv.setTextColor(getResources().getColor(R.color.black));
        tabOrdersIv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void showOrdersUi(){
        //show orders hide product ui
        productsRl.setVisibility(View.GONE);
        ordersRl.setVisibility(View.VISIBLE);

        tabOrdersIv.setTextColor(getResources().getColor(R.color.black));
        tabOrdersIv.setBackgroundResource(R.drawable.shape_tab02);

        tabProductsIv.setTextColor(getResources().getColor(R.color.black));
        tabProductsIv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }





    private void makeMeOffline(){
        //after logging in make user online
        progressDialog.setMessage("Logged out User..");

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        //Update Values to DB
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //update success
                        firebaseAuth.signOut();
                        checkUser();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        //update fail
                        progressDialog.dismiss();
                        Toast.makeText(MainSellerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user==null){
            startActivity(new Intent(MainSellerActivity.this,LoginActivity.class));
            finish();
        }
        else {
            loadMyInfo();
        }
    }

    private void loadMyInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            //retrieve data from db
                            String name = ""+ds.child("name").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String email = ""+ds.child("email").getValue();
                            String shopName = ""+ds.child("shopName").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();

                            //set data to UI
                            nameIv.setText(name);
                            emailIv.setText(email);
                            shopNameIv.setText(shopName);
                           try {
                               Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_yellow).into(profileIv);
                           }catch (Exception e){
                               profileIv.setImageResource(R.drawable.ic_person_yellow);
                           }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}