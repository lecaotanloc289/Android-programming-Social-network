package com.home.intagramapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RagisterActivity extends AppCompatActivity {

    EditText username ,fullname,password,email;
    Button ragister;
    TextView text_login;
    FirebaseAuth mauth;
    DatabaseReference reference;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ragister);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        ragister = findViewById(R.id.ragister_btn);
        text_login = findViewById(R.id.txt_login);
        mauth = FirebaseAuth.getInstance();
        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RagisterActivity.this,LoginActivity.class));
            }
        });

        ragister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              pd = new ProgressDialog(RagisterActivity.this);
              pd.setMessage("Please wait...");
              pd.show();
              String str_username= username.getText().toString();
                String str_fullname= fullname.getText().toString();
                String str_email= email.getText().toString();
                String str_password= password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(
                        str_fullname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){

                    Toast.makeText(RagisterActivity.this,"All Filds are required",Toast.LENGTH_SHORT).show();

                }else if(str_password.length()<6) {
                    Toast.makeText(RagisterActivity.this,"Password must have 6 characters",Toast.LENGTH_SHORT).show();

                }else {

                    register(str_username,str_fullname,str_email,str_password);

                }

                }


        });




    }

    private  void register (final String username , final String fullname, final String email , String password){

        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RagisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = mauth.getCurrentUser();
                    String userid  = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                    HashMap<String,Object> hashMap = new HashMap<>();

                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("fullname",fullname);
                    hashMap.put("bio","");
                    hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/instagramappdatabase.appspot.com/o/120x120profile.png?alt=media&token=2858c7bc-58da-430a-9d20-89f6ea2f62e7");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                pd.dismiss();
                                Intent intent = new Intent(RagisterActivity.this ,StartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(RagisterActivity.this,"You can not register with this email and password",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }



}
