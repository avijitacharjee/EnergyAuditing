package energyaudit.example.com.enerngyauditing;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegistrationPage extends AppCompatActivity {

    //Define the UI elements
    private EditText regName;
    private EditText regPassword;
    private EditText regMeterNo;
    private EditText regAddress;
    private Button registerBtn;

    // Create an object of the class Credentials
    public static energyaudit.example.com.enerngyauditing.Credentials credentials;

    public static final String KEY_UNAME="User Name";
    public static final String KEY_UMETERNO="User Meter No.";
    public static final String KEY_UADDRESS="User Address";
    public static final String KEY_UPAASS="User Password";

    //Connection to Firestore
    private FirebaseFirestore fbase = FirebaseFirestore.getInstance();
    private CollectionReference devDatacollectionRef = fbase.collection("House Meter Data List");

    //Connection to local database
    SharedPreferences sr ;
    SharedPreferences.Editor srEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        //Defining the UI elements with respect to XML file
        regName = findViewById(R.id.etName);
        regPassword = findViewById(R.id.etRPassword);
        regMeterNo = findViewById(R.id.etRMeter);
        regAddress = findViewById(R.id.etAddress);
        registerBtn = findViewById(R.id.registerBtn);
        sr = getApplicationContext().getSharedPreferences("Electricity User Data", MODE_PRIVATE);
        srEdit = sr.edit();

        // Defining logic on the register button being pressed
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String password = regPassword.getText().toString().trim();
                    String metrno = regMeterNo.getText().toString().trim();

                addMeterDetails();

                //Verification of fields being valid
                if(validate(metrno, password))
                {
                    //Add the credentials into local database
                    credentials = new Credentials(metrno, password);
                    srEdit.putString("Key_MetrNo", metrno);
                    srEdit.putString("Key_PassKey", password);

                    srEdit.apply();
                    Toast.makeText(RegistrationPage.this, "Congrats! Successfully Registered.", Toast.LENGTH_SHORT).show();

                    //Directing to LogIn Screen or Activity
                    startActivity(new Intent(RegistrationPage.this, LogInActivity.class));
                }
            }
        });
    }

    //Defining the user details into Firebase
    private Map<String, Object> data() {
        String name = regName.getText().toString().trim();
        String pass= regMeterNo.getText().toString().trim();
        String address = regAddress.getText().toString().trim();
        String emailmtr = regPassword.getText().toString().trim();

        Map<String,Object> data = new HashMap<>();
        data.put(KEY_UNAME,name);
        data.put(KEY_UMETERNO,pass);
        data.put(KEY_UADDRESS,address);
        data.put(KEY_UPAASS,emailmtr);

        return data;
    }

    //Adding User Personal Details to FireBase
    private void addMeterDetails() {
        data();
        devDatacollectionRef.add(data());

    }

    // Validating function of the user input credentials
    boolean validate(String meterNum, String password)
    {
        // Validating if the Meter No. is empty and password field is 6 characters
        if(meterNum.isEmpty() || (password.length() < 6))
        {
            Toast.makeText(this, "Please enter your meter number and ensure password is more than 6 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
