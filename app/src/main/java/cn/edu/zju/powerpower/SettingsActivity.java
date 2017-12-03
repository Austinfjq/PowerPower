package cn.edu.zju.powerpower;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingsActivity extends  ListActivity {

    private List<Map<String, Object>> mData;
    private int selectItem = -1;
    private boolean[] switchSta = new boolean[100];
    private String fileName = "mySetting.set";
    private int[] myset = new int[100];
    private String[][] toastString = new String[10][2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getData();
        String s = getDataFromFile(this);
        //Toast.makeText(SettingsActivity.this, "the length of the string is :" + s, Toast.LENGTH_SHORT).show();
        if (s.length() > 0) {
            String[] splitString = s.split("\\|");
            for (int i = 0; i < 5; i++) {
                if (Boolean.parseBoolean(splitString[i]))
                    switchSta[i] = true;
                else
                    switchSta[i] = false;
            }
        }
//        String s1="";
//        for (int i = 0; i < 5; i++) {
//            s1 += switchSta[i];
//        }
//        Toast.makeText(SettingsActivity.this, "the string is :" + s, Toast.LENGTH_SHORT).show();

        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
    }

    public boolean[] getSwitch()
    {
        return switchSta;
    }

    private void saveDataToFile(Context context, String data) {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            /**
             * "data"为文件名,MODE_PRIVATE表示如果存在同名文件则覆盖，
             * 还有一个MODE_APPEND表示如果存在同名文件则会往里面追加内容
             */
            fileOutputStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDataFromFile(Context context) {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileInputStream = context.openFileInput(fileName);
            bufferedReader = new BufferedReader(
                    new InputStreamReader(fileInputStream));
            String result = "";
            while ((result = bufferedReader.readLine()) != null) {
                stringBuilder.append(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }


    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "无障碍模式");
        myset[0] = 0;
        list.add(map);
        toastString[0][0] = "无障碍模式开启";
        toastString[0][1] = "无障碍模式关闭";

        map = new HashMap<String, Object>();
        map.put("title", "内存加速模式");
        myset[1] = -1;
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "一键加速模式");
        myset[2] = 1;
        list.add(map);
        toastString[2][0] = "一键加速模式开启";
        toastString[2][1] = "一键加速模式关闭";

        map = new HashMap<String, Object>();
        map.put("title", "个性省电模式");
        myset[3] = -1;
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "商家模式");
        myset[4] = 2;
        list.add(map);
        toastString[4][0] = "商家模式开启";
        toastString[4][1] = "商家模式关闭";

        map = new HashMap<String, Object>();
        map.put("title", "学习模式");
        myset[5] = 3;
        list.add(map);
        toastString[5][0] = "学习模式开启";
        toastString[5][1] = "学习模式关闭";

        map = new HashMap<String, Object>();
        map.put("title", "聊天模式");
        myset[6] = 4;
        list.add(map);
        toastString[6][0] = "聊天模式开启";
        toastString[6][1] = "聊天模式关闭";

        return list;
    }

    // ListView 中某项被选中后的逻辑
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        selectItem = position;
        //Toast.makeText(SettingsActivity.this, " " + position, Toast.LENGTH_SHORT).show();
        Log.v("MyListView4-click", (String) mData.get(position).get("title"));
    }

    public final class ViewHolder1 {
        public TextView title;
        public Switch switchf;
    }

    public final class ViewHolder2 {
        public TextView title;
    }

    public String getSettings() {
        String s = "";
        s += switchSta[0];
        for (int i = 1; i < 5; i++) {
            s += "|";
            s += switchSta[i];
        }

        return s;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private final int TYPE_ONE = 1;
        private final int TYPE_TWO = 2;
        private int currentType;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        public int getItemViewType(int position) {
            if (myset[position] >= 0)
                return TYPE_ONE;
            else
                return TYPE_TWO;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            currentType = getItemViewType(position);
            if (currentType == TYPE_ONE) {
                ViewHolder1 holder = null;
                if (convertView == null) {
                    holder = new ViewHolder1();
                    convertView = mInflater.inflate(R.layout.activity_settings1, null);

                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    holder.switchf = (Switch) convertView.findViewById(R.id.switchf);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder1) convertView.getTag();
                }

                holder.title.setText((String) mData.get(position).get("title"));
                holder.switchf.setChecked(switchSta[myset[position]]);
                holder.switchf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //按下按钮
                        if (switchSta[myset[position]]) {
                            Toast.makeText(SettingsActivity.this, toastString[position][1], Toast.LENGTH_SHORT).show();
                            switchSta[myset[position]] = false;
                        } else {
                            Toast.makeText(SettingsActivity.this, toastString[position][0], Toast.LENGTH_SHORT).show();
                            switchSta[myset[position]] = true;
                        }
                        String result = getSettings();
                        saveDataToFile(SettingsActivity.this, result);
                    }
                });
            } else if (currentType == TYPE_TWO) {
                ViewHolder2 holder2 = null;
                if (convertView == null) {
                    holder2 = new ViewHolder2();
                    convertView = mInflater.inflate(R.layout.activity_settings3, null);

                    holder2.title = (TextView) convertView.findViewById(R.id.title);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }

                holder2.title.setText((String) mData.get(position).get("title"));
                String result = getSettings();
                saveDataToFile(SettingsActivity.this, result);
            }
            return convertView;
        }
    }
}