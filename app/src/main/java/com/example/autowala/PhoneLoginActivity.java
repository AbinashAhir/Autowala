package com.example.autowala;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chaos.view.PinView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.bson.Document;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.result.InsertOneResult;

public class PhoneLoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    CountryCodePicker ccp;
    private EditText phoneEdittext;
    private PinView firstPinView;
    private TextView enterOtptext;
    private ConstraintLayout phoneLayout;
    private String selected_country_code = "+91";
    private static final int CREDENTIAL_PICKER_REQUEST =1011 ;
    private ProgressBar progressBar;

//////////////////////////Firebase phone auth////////////////////////
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
//////////////////////////Firebase phone auth////////////////////////

//    ///////////MongoDB/////////////////
    String Appid = "autowala-uoqmy";
    private App app;

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    private EditText dataEditText;
//    //////////////MongoDB//////////////////////
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());

        requestPhoneNumber();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        phoneEdittext = (EditText) findViewById(R.id.phoneNumber);
        firstPinView = (PinView) findViewById(R.id.firstPinView);
        phoneLayout = (ConstraintLayout) findViewById(R.id.phoneLayout);
        enterOtptext = (TextView) findViewById(R.id.enterOtptext);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        dataEditText = (EditText) findViewById(R.id.phoneNumber);



//        MongoDB Credentials
        io.realm.mongodb.Credentials credentials = Credentials.emailPassword("ahirabinash6@gmail.com","Ahir1234");
        app.loginAsync(credentials, new App.Callback<User>() {
         @Override
         public void onResult(App.Result<User> result) {
             if (result.isSuccess()){
                 Log.v("User","Logged in Successfully.");

                 User user = app.currentUser();
                 mongoClient = user.getMongoClient("mongodb-atlas");
                 mongoDatabase = mongoClient.getDatabase("AutowalaDB");
                 MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Autowala");

                 mongoCollection.insertOne(new Document("userid", user.getId()).append("phoneNumber",dataEditText.getText().toString())).getAsync(result1 -> {
                     if (result1.isSuccess()){
                         Log.v("Data","Data Inserted Successfully");
                     }
                     else {
                         Log.v("Data","Error "+result.getError().toString());
                     }
                 });
//                     if (result1.isSuccess()){
//                         Log.v("Data","Data Inserted Successfully");
//                     }
//                     else {
//                         Log.v("Data","Error "+result.getError().toString());
//                     }
                 ;

             }
             else{
                 Log.v("User","LoginFailed");
             }
         }
     });


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
            }
        });

//        Phone Text Watcher
        phoneEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 10){
                    sendOtp();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        OTP text watcher
        firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6){
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,firstPinView.getText().toString().trim());
                    signInWithAuthCredential(credential);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        Auto phone Selector api

     ///////////OTP Callbacks///////////////////
     callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
         @Override
         public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                firstPinView.setText(code);

                signInWithAuthCredential(phoneAuthCredential);


            }
         }

         @Override
         public void onVerificationFailed(@NonNull FirebaseException e) {
             Toast.makeText(PhoneLoginActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
             progressBar.setVisibility(View.GONE);
             phoneLayout.setVisibility(View.VISIBLE);
             firstPinView.setVisibility(View.GONE);
         }

         @Override
         public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
             super.onCodeSent(verificationId, token);

             mVerificationId = verificationId;
             mResentToken = token;

             Toast.makeText(PhoneLoginActivity.this, "6 digit OTP Sent!", Toast.LENGTH_SHORT).show();

             progressBar.setVisibility(View.GONE);
             phoneLayout.setVisibility(View.GONE);
             firstPinView.setVisibility(View.VISIBLE);
         }
     };
    }


    private void sendOtp() {

     progressBar.setVisibility(View.VISIBLE);

     String phoneNumber = selected_country_code + phoneEdittext.getText().toString();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                .setTimeout(60L, TimeUnit.SECONDS)
                        .setPhoneNumber(phoneNumber)
                        .setActivity(PhoneLoginActivity.this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    protected void requestPhoneNumber() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    phoneEdittext.setText(credential.getId().substring(3));


                } else {
                    Toast.makeText(this, "No phone number found.", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signInWithAuthCredential(PhoneAuthCredential credentials) {
        mAuth.signInWithCredential(credentials)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(PhoneLoginActivity.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PhoneLoginActivity.this, CreateProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(PhoneLoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PhoneLoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Animatoo.animateSlideRight(PhoneLoginActivity.this);
                        }
                    }
                });
    }
}