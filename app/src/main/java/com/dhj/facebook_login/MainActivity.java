package com.dhj.facebook_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.util.Arrays;

import javax.security.auth.PrivateCredentialPermission;

public class MainActivity extends AppCompatActivity {
    TextView t;
    LoginButton b;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=findViewById(R.id.textView);
        b=findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
       b.setPermissions(Arrays.asList("email","user_birthday"));
       b.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {
               Toast.makeText(MainActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCancel() {

           }

           @Override
           public void onError(FacebookException error) {

           }
       });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tc= new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null)
            {
                t.setText("");
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }
            else
            {
                profile(currentAccessToken);
            }

        }
    };


    private void profile(AccessToken newAccessToken) {
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object!=null)
                {
                    try {
                        String email=object.getString("email");
                        String id=object.getString("id");
                        t.setText(email);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString(
                "fields",
                "id, name, email ");
        request.setParameters(parameters);
        request.executeAsync();
    }
}