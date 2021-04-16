package energyaudit.example.com.enerngyauditing;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Db_Firebase extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Db_FireBase";
    DecimalFormat df2 = new DecimalFormat("#.##");
    DecimalFormat df3 = new DecimalFormat("#.###");
    DecimalFormat df4 = new DecimalFormat("#.####");
    double hrkWh =0;
    double dailykWh=0 ;
    double monthCost=0;
    double hrCost=0;
    double weekCost =0;
    double dailyCost=0;
    double weekWh=0;
    double monthkw=0;

    //Define the UI elements
    private EditText name;
    private EditText units;
    private EditText watts;
    private EditText hrs;
    private EditText correc;
    private TextView displayDetails;
    private TextView displayList;
    private TextView hrdisplay;
    private TextView monthdisplay;
    private TextView weekdisplay;
    private TextView daydisplay;
    private TextView hrCostdisplay;
    private TextView monthCostdisplay;
    private TextView weekCostdisplay;
    private TextView dayCostdisplay;
    private Button btnAdd;
    private Button btnUpd;
    private Button btnDel;
    private Button btnView;
    private Button btnNext;

    public static final String DEV_NAME="Electrical Device Name";
    public static final String DEV_UNIT="Device Units";
    public static final String DEV_WATTS="Electrical Device Watttage";
    public static final String DEV_HOURS="Usage Hours";

    //Connection to Firestore
    private FirebaseFirestore fbase = FirebaseFirestore.getInstance();
    private CollectionReference devDatacollectionRef = fbase.collection("Electrical Device List");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db__firebase);
        //Defining the UI elements with respect to XML file
        btnAdd = findViewById(R.id.btnAdd);
        btnUpd = findViewById(R.id.btnUpd);
        btnDel = findViewById(R.id.btnDel);
        btnView = findViewById(R.id.btnView);
        btnNext = findViewById(R.id.buttonNext);
        name = findViewById(R.id.etdevicename);
        units = findViewById(R.id.etdeviceunits);
        watts = findViewById(R.id.etdevicewatt);
        hrs = findViewById(R.id.etdevicehrs);
        correc = findViewById(R.id.etIDUD);
        displayDetails = findViewById(R.id.dDetails);
        displayList = findViewById(R.id.dailykWh);
        hrdisplay = findViewById(R.id.kWhhourop);
        daydisplay = findViewById(R.id.kWhdailyop);
        weekdisplay = findViewById(R.id.kWhweekop);
        monthdisplay = findViewById(R.id.kWhmonthop);
        hrCostdisplay = findViewById(R.id.costhourop);
        dayCostdisplay = findViewById(R.id.costdailyop);
        weekCostdisplay = findViewById(R.id.costweekop);
        monthCostdisplay = findViewById(R.id.costmonthop);
        btnUpd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        // Defining logic on the view button being pressed
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHomeDevices();

            }
        });

        // Defining logic on the add button being pressed
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHomeDevices();
            }
        });
    }

    //Defining the user details into Firebase
    private Map<String, Object> data() {
        String nam = name.getText().toString().trim();
        String unts= units.getText().toString().trim();
        String wattage = watts.getText().toString().trim();
        String oprs= hrs.getText().toString().trim();

        Map<String,Object> data = new HashMap<>();
        data.put(DEV_NAME,nam);
        data.put(DEV_UNIT,unts);
        data.put(DEV_WATTS,wattage);
        data.put(DEV_HOURS,oprs);

        return data;
    }

    //Adding Users Device Details to FireBase
    private void addHomeDevices() {
        data();
        devDatacollectionRef.add(data()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Db_Firebase.this, "Succuessfully saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(Db_Firebase.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Retrieve Users Device Details from FireBase
    public void viewHomeDevices(){
        devDatacollectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String deviceDetails = " ";
                        float devicekWh;
                        float tdevicekWh=0;
                        String viewList =" ";
                        String viewhrkWh =" ";
                        for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots){

                            String nameDev = snapshots.getString(DEV_NAME);
                            String unitDev = snapshots.getString(DEV_UNIT);
                            String wattDev = snapshots.getString(DEV_WATTS);
                            String hrsDev = snapshots.getString(DEV_HOURS);
                            String devId = snapshots.getId();

                            devicekWh= (Float.parseFloat(unitDev) * Float.parseFloat(wattDev) * Float.parseFloat(hrsDev))/1000;

                            deviceDetails +="Device ID: " + devId + "\n"
                                    + "Device Name:  " + nameDev + "\n"
                                    + "Device Units:  " + unitDev + "\n"
                                    + "Device Watt Reading:  " + wattDev + "\n"
                                    + "Device Usage Hours:  " + hrsDev +"\n"
                                    + "Total Device kWh Consumption:  " + devicekWh + "\n" +"\n" ;

                            tdevicekWh = tdevicekWh + devicekWh; //Computation of monthly kWh

                        }
                        //Computation of monthly cost per kWh
                        if(tdevicekWh>0 && tdevicekWh<=2000){
                            monthCost = (tdevicekWh*2.210)*30;
                        }
                        dailykWh=tdevicekWh;    //Computation of daily kWh
                        hrkWh = dailykWh/ 24; //Computation of hourly kWh
                        weekWh=dailykWh*7;//Computation of weekly kWh
                        monthkw=tdevicekWh*30; //Computation of monthly kwh
                        dailyCost=monthCost/30; //Computation of daily cost per kWh
                        hrCost= dailyCost/24; //Computation of hourly cost per kWh
                        weekCost=dailyCost*7; //Computation of weekly cost per kWh
                        displayDetails.setText(deviceDetails);
                        viewList += "LIST OF DEVICES ADDED:" + "\n";
                        viewhrkWh += df4.format(hrkWh);
                        displayList.setText(viewList);
                        hrdisplay.setText(viewhrkWh);
                        daydisplay.setText(String.valueOf(df3.format(dailykWh)));
                        weekdisplay.setText(String.valueOf(df3.format(weekWh)));
                        monthdisplay.setText(String.valueOf(df2.format(monthkw)));
                        hrCostdisplay.setText(String.valueOf(df2.format(hrCost)));
                        dayCostdisplay.setText(String.valueOf(df2.format(dailyCost)));
                        weekCostdisplay.setText(String.valueOf(df2.format(weekCost)));
                        monthCostdisplay.setText(String.valueOf(df2.format(monthCost)));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpd:
                updateHomeData();
                break;
            case R.id.btnDel:
                deleteHomeData();
                break;
            case R.id.buttonNext:
                nextActivity();
                break;

        }
    }

    private void nextActivity() {
        //Directing to Energy Saving Screen or Activity
        Intent intant = new Intent(getApplicationContext(), EnrgiSavingActivity.class);
        startActivity(intant);
    }

    //Delete function to delete Users Device Details from FireBase
    private void deleteHomeData() {
        String del = correc.getText().toString().trim();
        devDatacollectionRef.document(del).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Db_Firebase.this,
                                "Successfully Deleted",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: " + e.toString());
                    }
                });
    }

    //Update function to update Users Device Details from FireBase
    private void updateHomeData() {
        data();
        String upd = correc.getText().toString().trim();
        devDatacollectionRef.document(upd).update(data())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Db_Firebase.this,
                                "Successfully Updated",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: " + e.toString());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }
}

