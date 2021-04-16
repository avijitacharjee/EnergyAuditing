package energyaudit.example.com.enerngyauditing;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread t=new Thread(){

            public void run()
            {
                try{
                    //Time to display Splash Screen
                    sleep(3000);
                }catch (Exception x)
                {
                    x.printStackTrace();
                }finally {
                    //Directing to LogIn Screen or Activity
                    Intent intnt= new Intent(SplashScreenActivity.this,LogInActivity.class);
                    startActivity(intnt);
                }
            }
        }; t.start();
    }
}
