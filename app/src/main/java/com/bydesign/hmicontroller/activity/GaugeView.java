package com.bydesign.hmicontroller.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.activity.CustomGauge;
import com.bydesign.hmicontroller.fragments.CurrentActivityFragment;
import com.bydesign.hmicontroller.model.ParameterForGaugeView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.IllegalFormatException;

import info.bydesign.hmicontroller.R;

public class GaugeView extends Activity {
    LinearLayout linearLayout;


    TableRow tr;
    TableLayout tl;
    CustomGauge customGauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauge_view);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT ,LinearLayout.TEXT_ALIGNMENT_CENTER);
        customGauge = new CustomGauge(this);
        try {
            addContentView(customGauge, params);

        }catch (Exception e){
            e.printStackTrace();

        }

    }



}

