package com.freelance.zchen.track;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.parse.ParseObject;

/**
 * Created by zchen on 10/11/2015.
 */
public class DeletePurchase extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .3));
    }

    public void delPurchase(View v){

        Intent intent = getIntent();
        String objId = intent.getStringExtra("purchase");
        //Purchases purch = (Purchases) intent.getSerializableExtra("purchase");
        System.out.println("delete pushed");


        Purchases purchase = ParseObject.createWithoutData(Purchases.class, objId);
        System.out.println(purchase.getNameOfPurchase());
       // System.out.println(purch.getNameOfPurchase());
        /*
        *
        * code for how to delete purchase once it is parceable and passed in
        * purchase.deleteEventually();
          finish();
        *
        * */
    }

    public void cancelDel(View v){
        finish();
    }


}
