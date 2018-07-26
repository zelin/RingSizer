package com.neberox.app.ringsizer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.neberox.lib.ringsizer.RingSizeModel;
import com.neberox.lib.ringsizer.RingSizer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ListView listView;
    private SeekBar seekbar;

    private ArrayList<RingSizeModel> sizes = new ArrayList<>();

    private RingSizer mRingSizer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        seekbar  = (SeekBar) findViewById(R.id.seekbar);

        mRingSizer = (RingSizer) findViewById(R.id.ringSizer);
        mRingSizer.setDiameter(9.91f);

        sizes = RingSizer.getRingSizes(this);

        seekbar.setMax(sizes.size() - 1);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser)
            {
                listView.setItemChecked(progress, true);
                listView.setSelection(progress);
                mRingSizer.setSize(sizes.get(progress).diameter , sizes.get(progress).usa);
            }
        });

        listView.setAdapter(new CustomArrayAdapterForRingSize());
        mRingSizer.setSize(sizes.get(0).diameter , sizes.get(0).usa);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                seekbar.setProgress(position);
            }

        });
    }

    /****************************************************************************************************
     *
     *
     *
     *
     *
     ****************************************************************************************************/
    static class ViewHolder
    {
        protected TextView size;
        protected LinearLayout bgLayout;
    }

    private class CustomArrayAdapterForRingSize extends ArrayAdapter<RingSizeModel>
    {
        private final LayoutInflater inflator;

        public CustomArrayAdapterForRingSize()
        {
            super(MainActivity.this, R.layout.adapter_sizer_item, sizes);
            inflator = getLayoutInflater();
        }

        @Override
        public int getCount()
        {
            return sizes.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View view = convertView;
            ViewHolder viewHolder = new ViewHolder();

            RingSizeModel size = sizes.get(position);

            if (convertView == null)
            {
                view = inflator.inflate(R.layout.adapter_sizer_item, parent, false);

                viewHolder.bgLayout = (LinearLayout)  view.findViewById(R.id.bgLayout);
                viewHolder.size = (TextView)  view.findViewById(R.id.size);
                view.setTag(viewHolder);
            }
            else
                viewHolder = (ViewHolder) view.getTag();

            if(position == listView.getCheckedItemPosition())
            {
                viewHolder.bgLayout.setBackgroundColor(Color.parseColor("#ffe6e6e6"));
            }
            else{

                viewHolder.bgLayout.setBackgroundColor(Color.TRANSPARENT);
            }

            viewHolder.size.setText(size.usa);

            return view;
        }
    }
}
