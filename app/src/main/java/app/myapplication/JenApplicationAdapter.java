package app.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class JenApplicationAdapter extends BaseAdapter implements android.widget.ListAdapter {
    Context mContext;

    public String[] applications = new String[0];

    public JenApplicationAdapter(Context context){
        JenApplicationTable ja = new JenApplicationTable(context);
        applications = ja.getApplication();
        //Log.e("test", "app="+applications.toString());
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return applications.length;
    }

    @Override
    public ArrayList<String> getItem(int position) {
        ArrayList<String> p = new ArrayList<String>();

        if( applications[position] != null ){
            //Log.e("test", "pos="+position+":"+applications[position].toString());
            p.add(applications[position]);
        }
        return p;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.item_list_row, parent, false);
        }

        ArrayList<String> p = getItem(position);
        //Log.e("test", "p="+p.toString());
        //Log.e("test", "v="+v.toString());
        if( p != null ){
            TextView textViewItem = (TextView) v.findViewById(R.id.application_id);
            //Log.e("test", "textViewItem="+textViewItem.toString());
            textViewItem.setText(p.get(0));
            //Log.e("test", "text="+textViewItem.getText());
        }

        return v;
    }

}
