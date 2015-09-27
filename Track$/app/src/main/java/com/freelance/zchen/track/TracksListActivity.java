package com.freelance.zchen.track;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.util.List;

public class TracksListActivity extends Activity {

    private ParseQueryAdapter<Purchases> purchasesAdapter;

    private LayoutInflater inflater;
    private ListView purchasesListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("HERERERERERERERE");

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_list);

        purchasesListView = (ListView) findViewById(R.id.purchasesList);

        ParseQueryAdapter.QueryFactory<Purchases> factory = new ParseQueryAdapter.QueryFactory<Purchases>(){
            public ParseQuery<Purchases> create(){
                ParseQuery<Purchases> query = Purchases.getQuery();
                query.orderByDescending("createdAt");
                query.fromLocalDatastore();
                return query;
            }
        };

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        purchasesAdapter = new PurchaseListAdapter(this, factory);

        //  Attach the query adapter to the view
        ListView purhcasesListView = (ListView) findViewById(R.id.purchasesList);
        purhcasesListView.setAdapter(purchasesAdapter);

        purhcasesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Purchases purhcase = purchasesAdapter.getItem(position);
                openEditView(purhcase);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        syncPurchasesToParse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracks_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            syncPurchasesToParse();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void openEditView(Purchases purchase){
        System.out.println("HERE:");
        System.out.println(purchase.getNameOfPurchase() + "  " + purchase.getCostOfPurchase());
    }

    private void loadFromParse(){
        ParseQuery<Purchases> query = Purchases.getQuery();
        query.findInBackground(new FindCallback<Purchases>() {
            @Override
            public void done(List<Purchases> purchases, ParseException e) {
                if (e == null){
                    ParseObject.pinAllInBackground((List<Purchases>) purchases,
                            new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null){
                                        if (!isFinishing()){
                                            purchasesAdapter.loadObjects();
                                        }
                                    } else{
                                        Log.i("PurchaseListActivity",
                                                "laodfromparsefailed to pin to background"
                                        + e.getMessage());
                                    }
                                }
                            });
                } else{
                    Log.i("PurchaseListActivity",
                            "loadfromparse failed"
                                    + e.getMessage());
                }
            }
        });
    }

    private void syncPurchasesToParse(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if((ni != null) && (ni.isConnected())){
            ParseQuery<Purchases> query = Purchases.getQuery();
            query.findInBackground(new FindCallback<Purchases>() {
                @Override
                public void done(List<Purchases> purchases, ParseException e) {
                    if (e == null){
                        for (final Purchases purchase : purchases){
                            purchase.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null){
                                        if(isFinishing()){
                                            purchasesAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                        }
                    }
                    else{
                        Log.i("PurchaseListActivity",
                                "syncTodosToParse: Error finding pinned todos: "
                                        + e.getMessage());
                    }
                }
            });
        }
        else{
            Toast.makeText(
                    getApplicationContext(), "Your device appears to be offline. Some Purchases may not have been synced to Parse.com",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class PurchaseListAdapter extends ParseQueryAdapter<Purchases>{
        public PurchaseListAdapter(Context context,
                                   ParseQueryAdapter.QueryFactory<Purchases> queryFactory){
            super(context, queryFactory);
        }

        @Override
        public View getItemView(Purchases purchase, View view, ViewGroup parent){
            ViewHolder holder;
            if (view == null){
                view = inflater.inflate(R.layout.list_item_purchase, parent, false);
                holder = new ViewHolder();
                holder.purchaseTitle = (TextView) view.findViewById(R.id.purhcaseName);
                holder.purchaseCost = (TextView) view.findViewById(R.id.PurchaseCost);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();
            }
            TextView purchaseTitle = holder.purchaseTitle;
            TextView purchaseCost = holder.purchaseCost;
            purchaseTitle.setText(purchase.getNameOfPurchase());
            purchaseCost.setText(String.valueOf(purchase.getCostOfPurchase()));
            return view;
        }
    }

    private static class ViewHolder{
        TextView purchaseTitle;
        TextView purchaseCost;
    }
}
