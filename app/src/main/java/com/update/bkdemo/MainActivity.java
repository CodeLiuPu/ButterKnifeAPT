package com.update.bkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.update.apt_annotation.BindView;

/**
 * @author liupu
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_content)
    TextView tcContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
