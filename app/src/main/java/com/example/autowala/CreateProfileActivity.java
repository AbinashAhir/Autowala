package com.example.autowala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class CreateProfileActivity extends AppCompatActivity {


    public EditText editPersonName;
    EditText editTextMail;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        editPersonName = (EditText) findViewById(R.id.editPersonName);
        editTextMail = (EditText) findViewById(R.id.editTextEmail);
        submitButton = (Button) findViewById(R.id.submitButton);

        String personName = editPersonName.getText().toString();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}