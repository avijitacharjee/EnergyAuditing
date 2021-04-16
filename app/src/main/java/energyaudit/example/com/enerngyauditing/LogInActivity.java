package energyaudit.example.com.enerngyauditing;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LogInActivity extends AppCompatActivity {

    //Define the UI elements
    private EditText logMeterno;
    private EditText logPassword;
    private TextView eAttemptsInfo;
    private Button logBtn;
    private TextView tvSignup;

    private int count = 5;
    boolean isValid = false;

    //Connection to local database
    SharedPreferences sr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //Defining the UI elements with respect to XML file
        logMeterno = findViewById(R.id.etLogMeter);
        logPassword = findViewById(R.id.etLogPassword);
        eAttemptsInfo = findViewById(R.id.tvAttempts);
        logBtn = findViewById(R.id.buttonLogin);
        tvSignup = findViewById(R.id.tvRegister);
        sr = getApplicationContext().getSharedPreferences("Electricity User Data", MODE_PRIVATE);


        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Directing to RegistrationPage Screen or Activity
                startActivity(new Intent(LogInActivity.this, RegistrationPage.class));
            }
        });

        if(sr!=null){
            String userMeterno = sr.getString("Key_MetrNo", "");
            String userPasskey = sr.getString("Key_PassKey", "");

            RegistrationPage.credentials = new Credentials(userMeterno,userPasskey);
        }

        // Defining logic on the log in button being pressed
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userMeterNo = logMeterno.getText().toString();
                String userPassword = logPassword.getText().toString();

                // Validating if the user inputs are empty
                if(userMeterNo.isEmpty() || userPassword.isEmpty())
                {
                    Toast.makeText(LogInActivity.this, "Please enter meter number and password!", Toast.LENGTH_LONG).show();

                }else {

                    //Verification of fields being valid
                    isValid = validate(userMeterNo, userPassword);

                    // Defining logic if user input is not valid
                    if (!isValid) {

                        //Decrease the count value if user input is not valid
                        count--;

                        //Displaying remaining attempts value
                        eAttemptsInfo.setText("Total attempts remaining: " + String.valueOf(count));

                        //Logic to disable the login button on all attempts
                        if (count == 0) {
                            logBtn.setEnabled(false);
                            Toast.makeText(LogInActivity.this, "You have used all your attempts try again later!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LogInActivity.this, "Incorrect meter no or password, please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                    //Defining logic if user input is valid
                    else {

                        Toast.makeText(LogInActivity.this, "Congrats! Successfully Logged In.", Toast.LENGTH_SHORT).show();
                        //Directing to Db_Firebase Screen or Activity
                        startActivity(new Intent(LogInActivity.this, Db_Firebase.class));
                    }

                }
            }
        });
    }

    private boolean validate(final String metr, final String userPassword){
        if(RegistrationPage.credentials != null){
            if(metr.equals(RegistrationPage.credentials.getMeterNo()) && userPassword.equals(RegistrationPage.credentials.getPassword())){
                return true;
            }
        }
        return false;
    }
}
