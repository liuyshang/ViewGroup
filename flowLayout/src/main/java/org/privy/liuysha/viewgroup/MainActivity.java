package org.privy.liuysha.viewgroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout llView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llView = (LinearLayout) findViewById(R.id.ll_view);

        for (int i=0; i<5; i++){
            FlowLayout flowLayout = new FlowLayout(MainActivity.this);
            FlowLayout.MarginLayoutParams layoutParams = new FlowLayout.MarginLayoutParams(
                    FlowLayout.MarginLayoutParams.WRAP_CONTENT,FlowLayout.MarginLayoutParams.WRAP_CONTENT);
            flowLayout.setLayoutParams(layoutParams);
            for (int j=0; j<10; j++){
                TextView textView = new TextView(MainActivity.this);
                textView.setTextAppearance(MainActivity.this, R.style.text_view);
                textView.setText(j + "Hellow");
                flowLayout.addView(textView,layoutParams);
            }
            llView.addView(flowLayout);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
