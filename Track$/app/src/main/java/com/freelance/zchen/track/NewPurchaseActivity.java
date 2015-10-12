package com.freelance.zchen.track;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewPurchaseActivity extends Activity {

    private EditText nameOfPurchase;
    private EditText costOfPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_purchase);

        nameOfPurchase  = (EditText) findViewById(R.id.nameOfPurchaseInput);
        costOfPurchase = (EditText)findViewById(R.id.costOfPurchaseInput);
    }

    public void savePurchase(View v){
        System.out.println("here");
        if (nameOfPurchase.getText().length() > 0 && costOfPurchase.getText().length() > 0){
            Purchases p = new Purchases();
            p.setNameOfPurchase(nameOfPurchase.getText().toString());
            float temp = Float.valueOf(costOfPurchase.getText().toString());
            p.setCostOfPurchase(temp);
            p.saveEventually();
            nameOfPurchase.setText("");
            costOfPurchase.setText("");
            Toast.makeText(getApplicationContext(), "New purchase added to list", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Missing Purchase Details", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelPurchase(View v){
        System.out.println("here");
        finish();
    }
}
