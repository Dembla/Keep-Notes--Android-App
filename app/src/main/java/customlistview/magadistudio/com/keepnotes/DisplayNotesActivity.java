package customlistview.magadistudio.com.keepnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.DatabaseHandler;
import model.MyNote;

public class DisplayNotesActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<MyNote> dbWishes = new ArrayList<>();
    private WishAdapter wishAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list);
        refreshData();
    }

    private void refreshData() {

        dbWishes.clear();
        dba = new DatabaseHandler(getApplicationContext());
        ArrayList<MyNote> wishesFromDB = dba.getWishes();

        for(int i=0; i<wishesFromDB.size(); i++) {
            String title = wishesFromDB.get(i).getTitle();
            String dateText = wishesFromDB.get(i).getRecordDate();
            String content = wishesFromDB.get(i).getContent();

            int mid = wishesFromDB.get(i).getItemId();  //for deleting

            MyNote myWish = new MyNote();
            myWish.setTitle(title);
            myWish.setContent(content);
            myWish.setRecordDate(dateText);

            myWish.setItemId(mid);  //for deleting

            dbWishes.add(myWish);
        }
        dba.close();
        //setup Adapter
        wishAdapter = new WishAdapter(DisplayNotesActivity.this, R.layout.wish_row, dbWishes);
        listView.setAdapter(wishAdapter);
        wishAdapter.notifyDataSetChanged();
    }

    public class WishAdapter extends ArrayAdapter<MyNote> {

        Activity activity;
        int layoutResource;
        MyNote wish;
        ArrayList<MyNote> mData = new ArrayList<>();

        public WishAdapter(Activity act, int resource, ArrayList<MyNote> data) {
            super(act, resource, data);

            activity = act;
            layoutResource = resource;
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public MyNote getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public int getPosition(MyNote item) {
            return super.getPosition(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            ViewHolder holder = null;

            if(row == null || (row.getTag()) == null) {

                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResource, null);
                holder = new ViewHolder();

                holder.mTitle = (TextView) row.findViewById(R.id.name);
                holder.mDate = (TextView) row.findViewById(R.id.dateText);

                row.setTag(holder);
            }
            else {
                holder = (ViewHolder) row.getTag();
            }

            holder.myWish = getItem(position);

            holder.mTitle.setText(holder.myWish.getTitle());
            holder.mDate.setText(holder.myWish.getRecordDate());

            final ViewHolder finalHolder = holder;
            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String text = finalHolder.myWish.getContent().toString();
                    String dateText = finalHolder.myWish.getRecordDate().toString();
                    String title = finalHolder.myWish.getTitle().toString();

                    int mid = finalHolder.myWish.getItemId();  //for deleting

                    Intent j = new Intent(DisplayNotesActivity.this, NoteDetailActivity.class);
                    j.putExtra("content", text);
                    j.putExtra("date", dateText);
                    j.putExtra("title", title);
                    j.putExtra("id", mid);  //for deleting

                    startActivity(j);
                }
            });
            return row;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        class ViewHolder {

            MyNote myWish;
            TextView mTitle;
            TextView mId;
            TextView mContent;
            TextView mDate;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(DisplayNotesActivity.this, MainActivity.class);
        startActivity(i);
    }
}
