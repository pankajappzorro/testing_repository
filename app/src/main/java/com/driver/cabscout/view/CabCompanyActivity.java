package com.driver.cabscout.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.cabscout.controler.CabCompaniesManager;
import com.driver.cabscout.model.Constant;
import com.driver.cabscout.model.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Map;

import app.driver.cabscout.R;
/**
 * Created by pankaj on 23/1/17.
 */
public class CabCompanyActivity extends AppCompatActivity implements View.OnClickListener  {
    private final String TAG = CabCompanyActivity.class.getSimpleName();
    Activity activity = this;
    Toolbar toolbar;
    TextView next_register, selectCab,alreadyAccount;
    BottomSheetDialog bottomSheetDialog;
    ArrayList<String> cabCompaniesList;
    ArrayList<Integer> cabIdList;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    String selected_cab = "";
    int cab_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_company);
        initViews();
    }
    public void initViews() {

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("CREATE ACCOUNT");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("app.cabscout.driver", PackageManager.GET_SIGNATURES);
//            for (android.content.pm.Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hashxxxx key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        next_register = (TextView)findViewById(R.id.btRegister);
        selectCab = (TextView)findViewById(R.id.selectCab);
        alreadyAccount = (TextView)findViewById(R.id.alreadyAccount);

        alreadyAccount.setOnClickListener(this);
        next_register.setOnClickListener(this);
        selectCab.setOnClickListener(this);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        listView = (ListView)bottomSheetDialog.findViewById(R.id.listView);
        cabCompaniesList = new ArrayList<>();
        cabIdList = new ArrayList<>();
        for (Map.Entry<Integer,String> entry : CabCompaniesManager.cabCompaniesList.entrySet()) {
            Log.e(TAG, "cab id--"+ entry.getKey());
            cabCompaniesList.add(entry.getValue());
            cabIdList.add(entry.getKey());
        }
  // set the campany list in array adapter
        arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, cabCompaniesList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectCab.setText(cabCompaniesList.get(position));
                selected_cab = cabCompaniesList.get(position);
                cab_id = cabIdList.get(position);
                bottomSheetDialog.dismiss();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alreadyAccount:
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btRegister:
                if (selected_cab.isEmpty())
                    Toast.makeText(activity, "Please select your cab company", Toast.LENGTH_SHORT).show();
                else {
                    Intent intCab = new Intent(activity, SignUp.class);
                    intCab.putExtra("cab_id", String.valueOf(cab_id));
                    startActivity(intCab);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                break;
            case R.id.selectCab:
                bottomSheetDialog.show();
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.CAB_COMPANIES_SUCCESS:
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
