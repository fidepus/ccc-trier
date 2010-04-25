package c3t.de;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("Hier ist später die Karte");
        setContentView(textview);
    }
}