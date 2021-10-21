package com.example.nodejspehla;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;//yeh object server mein Retrofit library ke zariye data rakhwa dega
    private RetrofitInterface retrofitInterface;
    //private String BASE_URL = "http://10.0.2.2:3000";
    private String BASE_URL = "http://192.168.122.1/24:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginDialog();
            }
        });

        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignUpDialog();
            }
        });

    }
    private void handleLoginDialog() {
        //custom sa dialog banakar dikhayenge
        View view = getLayoutInflater().inflate(R.layout.login_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.show();

        Button loginBtn = view.findViewById(R.id.login);
        EditText emailEdit = view.findViewById(R.id.emailEdit);

        // $$$$$$$$ E! --> hum view ko rakh rahe hain bina usk bhi toh ho raha  hai na??
        // emailEdit=findViewById(R.id.emailEdit);
        EditText passwordEdit = view.findViewById(R.id.passwordEdit);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",emailEdit.getText().toString());
                map.put("password",passwordEdit.getText().toString());

                //make an object of Call class that will be holding LoginResult
                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                //now exceute http request
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        //tab call hoga jab server ne humari request ka kuch response diya ho
                        if(response.code()==200){
                            LoginResult result=response.body();
                            AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle(result.getName());
                            builder1.setMessage(result.getEmail());
                            builder1.show();
                        }
                        else if(response.code()==404){
                            Log.d("####","Galat credentials daala hai user");
                            Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.d("#####","HTTP request failed in LogIn in call.enqueue(...)");
                        Toast.makeText(MainActivity.this, "Some Error occured "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void handleSignUpDialog() {
        //custom sa dialog banakar dikhayenge
        View view = getLayoutInflater().inflate(R.layout.signup_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.show();

        Button signupBtn = view.findViewById(R.id.signUp);
        EditText nameEdit = view.findViewById(R.id.nameEdit);
        EditText emailEdit = view.findViewById(R.id.emailEdit);
        EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map= new HashMap<>();
                map.put("name",nameEdit.getText().toString());
                map.put("email",emailEdit.getText().toString());
                map.put("password",passwordEdit.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Log.d("####","signed up successfully");
                            Toast.makeText(MainActivity.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.code()==400){
                            Log.d("####","Already register tha lekin phir se kar raha hai");
                            Toast.makeText(MainActivity.this, "Already registered", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("#####","HTTP request failed in SignUp in call.enqueue(...)");
                        Toast.makeText(MainActivity.this, "Some Error occured "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


}