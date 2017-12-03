package cn.edu.zju.powerpower;

import android.app.ActivityManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.classic.adapter.CommonRecyclerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static cn.edu.zju.powerpower.utils.AppUtil.getAvailMemory;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import com.classic.adapter.CommonRecyclerAdapter;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import java.text.SimpleDateFormat;

public class MemoryCleanActivity extends AppCompatActivity {

    private Button quickBtn, cleverBtn, checkBtn ,returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_clean);

        quickBtn = (Button)findViewById(R.id.quick_save);
        cleverBtn = (Button)findViewById(R.id.clever_save);
        checkBtn = (Button)findViewById(R.id.check_save);
        returnBtn = (Button)findViewById(R.id.rtn);

        quickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MemoryCleanActivity.this, Quick_save.class);
                startActivity(i1);
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(MemoryCleanActivity.this, Check_save.class);
                startActivity(i2);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MemoryCleanActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}