package com.xj.framedemo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
//    @Bind(R.id.buttlay)
//    LinearLayout buttlay;
//    @Bind(R.id.sample_text)
//    XJTextView sample_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

//        buttlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ((Button)findViewById(R.id.click)).setTextColor(getResources().getColor(R.color.colorAccent));
//            }
//        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.unbind(this);
    }
}
