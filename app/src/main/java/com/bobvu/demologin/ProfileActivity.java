package com.bobvu.demologin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView profileImage;

    private TextView name, email, id;
    private Button signoutBtn;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        id = findViewById(R.id.id);
        signoutBtn = findViewById(R.id.signoutBtn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        signoutBtn.setOnClickListener(view -> {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
                if (status.isSuccess()) {
                    gotoMainActivity();
                } else {
                    Toast.makeText(ProfileActivity.this, "Logout false!", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void gotoMainActivity() {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSigninResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();



            name.setText(account.getDisplayName());
            email.setText(account.getEmail());
            id.setText(account.getId());
            String idToken = account.getIdToken();

           // Toast.makeText(ProfileActivity.this, idToken, Toast.LENGTH_SHORT).show();

            String serverAuthCode = account.getServerAuthCode();
            Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profileImage);
        } else {
            gotoMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();

            handleSigninResult(result);
        } else {
            opr.setResultCallback(result -> {
                handleSigninResult(result);
            });
        }
    }
}
















