package com.neberox.app.ringsizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neberox.lib.ringsizer.RingSizer;

public class MainActivity extends AppCompatActivity
{
    private RingSizer mRingSizer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRingSizer = (RingSizer) findViewById(R.id.ringSizer);
        mRingSizer.setDiameter(9.91f);

    }
}
