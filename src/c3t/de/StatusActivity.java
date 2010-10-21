package c3t.de;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class StatusActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

        TextView textview = new TextView(this);
        textview.setText("Hier ist später der Clubstatus.");
        // setContentView(textview);

    }
}
