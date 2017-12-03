package cn.edu.zju.powerpower;

import android.content.Context;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import java.util.List;
import rx.functions.Action1;

/**
 * Created by Austin on 2017/11/1.
 */

public class PackageAdapter extends CommonRecyclerAdapter<String> implements Action1<List<String>> {

    public PackageAdapter(Context context) {
        super(context, R.layout.item_app_info);
    }

    @Override public void onUpdate(BaseAdapterHelper helper, String item, int position) {
        helper.setText(R.id.item_pkg, ProcessManager.getApplicationNameByPackageName(mContext, item));
    }

    @Override public void call(List<String> items) {
        if(null != items && items.size() > 0){
            replaceAll(items);
        } else {
            clear();
        }
    }
}
