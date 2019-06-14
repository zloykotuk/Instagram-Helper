package com.example.instagramhelper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    Button btn, btn_login;
    TextView result;
    EditText id_post;
    private String CSRFToken = "";
    private String Cookies = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        btn = findViewById(R.id.btn_get);
        btn_login = findViewById(R.id.btn_login);
        id_post = findViewById(R.id.et_id_post);
        result = findViewById(R.id.tv_result);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int id = Integer.parseInt(id_post.getText().toString());
                result.setText("");
                NetworkService.getInstance()
                        .getJSONApi()
                        .getPostWithID("lll", Cookies)
                        .enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                Post post = response.body();
                                result.setText(String.valueOf(post.getGraphql().getUser().getId()));
                                CSRFToken = NetworkService.getInstance().getCSRFToken();
                                Cookies = NetworkService.getInstance().getCookieValue();
                                Log.d("1", CSRFToken);
                                Log.d("1", Cookies);
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {

                            }
                        });
            }
        });

    }


}
