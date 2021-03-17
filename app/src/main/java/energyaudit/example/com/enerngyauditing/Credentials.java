package energyaudit.example.com.enerngyauditing;



public class Credentials {
    private String Password;
    private String MeterNo;

    //Constructor
    Credentials(String meterNo, String password){
        this.Password = password;
        this.MeterNo = meterNo;

    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String meterNo) {
        MeterNo = meterNo;
    }

}

