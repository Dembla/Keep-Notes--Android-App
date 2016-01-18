package customlistview.magadistudio.com.keepnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.MyNote;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private EditText content;
    private ImageButton saveButton;
    private ImageButton editOld;

    public DatabaseHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dba = new DatabaseHandler(MainActivity.this);

        title = (TextView) findViewById(R.id.titleEditText);
        content = (EditText) findViewById(R.id.wishEditText);
        saveButton = (ImageButton) findViewById(R.id.saveButton);
        editOld = (ImageButton) findViewById(R.id.editOld);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!content.getText().toString().isEmpty()) {
                    String text;
                    //text = content.getText().toString();
                    if(content.getText().toString().length() >= 10) {
                        text = content.getText().toString().substring(0, 9);
                        title.setText(text);
                    }
                    else {
                        text = content.getText().toString();
                        title.setText(text);
                    }
                    saveToDb();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Note is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DisplayNotesActivity.class);
                startActivity(i);
            }
        });
    }

    private void saveToDb() {

        MyNote wish = new MyNote();
        wish.setTitle(title.getText().toString().trim());
        wish.setContent(content.getText().toString().trim());

        dba.addWishes(wish);
        dba.close();

        //clear
        title.setText("");
        content.setText("");

        Intent i = new Intent(MainActivity.this, DisplayNotesActivity.class);
        startActivity(i);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}
