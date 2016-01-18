package customlistview.magadistudio.com.keepnotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;

public class NoteDetailActivity extends AppCompatActivity {

    private TextView title, date, content;
    private ImageButton deleteButton, updateButton, sharingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.detailTitle);
        date = (TextView ) findViewById(R.id.detailsDateText);
        content = (EditText) findViewById(R.id.detailsTextView);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton); //for deleting
        updateButton = (ImageButton) findViewById(R.id.updateButton); //for updating

        Bundle extras = getIntent().getExtras();

        if(extras != null) {

            title.setText(extras.getString("title"));
            date.setText("Created: " + extras.getString("date"));
            content.setText(extras.getString("content"));

            final int id = extras.getInt("id");                     //for deleting
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteButtonHandler();
                }
            });

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    String v1 = title.getText().toString();
                    if(v1.isEmpty()) {
                        String temp;
                        temp = content.getText().toString();
                        if(content.getText().toString().length() >= 10) {
                            temp = content.getText().toString().substring(0, 9);
                            title.setText(temp);
                        }
                        else {
                            temp = content.getText().toString();
                            title.setText(temp);
                        }
                        v1 = title.getText().toString();
                    }
                    String v2 = content.getText().toString();
                    dba.update_byID(id, v1, v2);
                    Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NoteDetailActivity.this, DisplayNotesActivity.class));
                }
            });
        }
    }

    //Setting Up delete with dialog

    public void deleteButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder (NoteDetailActivity.this);
        //setting dialog title
        alertDialog.setTitle("Delete Note");
        //setting alert message
        alertDialog.setMessage("Are you sure ?");
        //setting positive yes
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Bundle extras = getIntent().getExtras();
                final int id = extras.getInt("id");
                DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                dba.deleteWish(id);
                Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NoteDetailActivity.this, DisplayNotesActivity.class));
            }
        });
        //setting No
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(NoteDetailActivity.this, DisplayNotesActivity.class);
                startActivity(i);
            }
        });
        //show dialog box at the end VERY VERY IMPORTANT
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        title = (TextView) findViewById(R.id.detailTitle);
        date = (TextView ) findViewById(R.id.detailsDateText);
        content = (EditText) findViewById(R.id.detailsTextView);
        backPressedHandler();
        return;
    }

    public void backPressedHandler () {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            final int id = extras.getInt("id");
            DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
            String v1 = title.getText().toString();
            if(v1.isEmpty()) {
                String temp;
                temp = content.getText().toString();
                if(content.getText().toString().length() >= 10) {
                    temp = content.getText().toString().substring(0, 9);
                    title.setText(temp);
                }
                else {
                    temp = content.getText().toString();
                    title.setText(temp);
                }
                v1 = title.getText().toString();
            }
            String v2 = content.getText().toString();

            dba.update_byID(id, v1, v2);

            startActivity(new Intent(NoteDetailActivity.this, DisplayNotesActivity.class));
        }
    }
}
