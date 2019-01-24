package com.example.mydiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WritingNoteActivity extends AppCompatActivity {

    private TextView dateTextView;
    private EditText editText;

    private String path = null;

    private String email;

    private String title, subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_note);

        dateTextView = findViewById(R.id.dateTextViewId);
        editText = findViewById(R.id.noteEditTextId);

        path = getFilesDir().getPath();
        email = getIntent().getStringExtra("email");

        title = getIntent().getStringExtra("noteTitle");
        subtitle = getIntent().getStringExtra("date");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_edit_delete,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.action_save) {
            try {
                saveText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(WritingNoteActivity.this,ListViewActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
        switch (item.getItemId()) {
            case R.id.action_edit:
                onEdit();
                return true;
            case R.id.action_delete:
                onDelete();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onEdit() {
        //startActivity(new Intent(this, AddEditCardActivity.class).putExtra("card", card));
    }

    private void onDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete note?")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCard();
                            }
                        })
                .setNegativeButton("No", null)
                .create()
                .show();
    }


    private void deleteCard() {
//        Log.d(getString(R.string.app_name), "Delete card: " + card.toString());

        /*CardService cardService = new CardService(this);
        cardService.deleteCard(card);

        Toast.makeText(this, R.string.card_delete_success, Toast.LENGTH_SHORT).show();*/

        finish();
    }

    private void saveText() throws IOException {
        String newPath = path + "/" + email;
        File dir = new File(newPath);

        if(!dir.exists()) {
            dir.mkdir();
        }

        int pos = 0;
        while(true) {
            File file = new File(dir, toString(pos) + ".txt");
            if(!file.exists()) {
                break;
            }
            ++pos;
        }

        File file = new File(dir, toString(pos) + ".txt");
        file.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        String temp = file.getAbsolutePath() + "\n" + title + "\n" + subtitle + "\n" + editText.getText().toString();

        bufferedWriter.write(temp);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private String toString(int n) {
        if(n == 0) {
            return "0";
        }
        String number = "";
        while(n != 0) {
            number = (char) (n % 10 + '0') + number;
            n /= 10;
        }
        return number;
    }
}
