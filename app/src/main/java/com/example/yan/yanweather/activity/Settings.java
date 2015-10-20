package com.example.yan.yanweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yan.yanweather.R;
import com.example.yan.yanweather.utils.PreferenceSetting;

/**
 * Created by Yan on 10/17/2015.
 */
public class Settings extends AppCompatActivity {
    private PreferenceSetting mPreferenceSetting;
    private TextView mTextView;
    private EditText mEditText;
    private Button mDefault;
    private Button mTempUnit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mPreferenceSetting = new PreferenceSetting();
        mEditText = (EditText) findViewById(R.id.test);
        mTextView = (TextView)findViewById(R.id.instruction);
        mDefault = (Button) findViewById(R.id.defaultSetting);
        mTempUnit =(Button) findViewById(R.id.unit);
        mEditText.setText(mPreferenceSetting.getValue(this,mPreferenceSetting.PREFS_DAYS));
        mTempUnit.setText(mPreferenceSetting.getValue(this,mPreferenceSetting.PREFS_UNIT));
        //test
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mPreferenceSetting.save(getBaseContext(), mPreferenceSetting.PREFS_DAYS, mEditText.getText().toString());
                    mTextView.setText(mEditText.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
        mDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferenceSetting.defautSetting(getBaseContext());
                mEditText.setText(mPreferenceSetting.getValue(getBaseContext(), mPreferenceSetting.PREFS_DAYS));
                mTempUnit.setText(mPreferenceSetting.getValue(getBaseContext(), mPreferenceSetting.PREFS_UNIT));
            }
        });

        mTempUnit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if("Fahrenheit".equals(mTempUnit.getText())) {
                    mTempUnit.setText("Celsius");
                    mPreferenceSetting.save(getBaseContext(), mPreferenceSetting.PREFS_UNIT, "Celsius");
                }
                else  {
                    mTempUnit.setText("Fahrenheit");
                    mPreferenceSetting.save(getBaseContext(), mPreferenceSetting.PREFS_UNIT, "Fahrenheit");
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

}
