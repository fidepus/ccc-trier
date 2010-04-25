package c3t.de;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NewsActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView textview = new TextView(this);
        textview.setText("Hier sind später die News");
        setContentView(textview);
    }
}
