package com.example.haletuyen.noteapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haletuyen.noteapp.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtPw;
    FloatingActionButton fab;

    // Kiem tra dang nhap
    SharedPreferences sharedPrefs; // luu du lieu password
    SharedPreferences.Editor editor; // thay doi du lieu trong sharedPref
    int confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getSharedPreferences("diary", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        setContentView(R.layout.activity_sign_in);
        initView();
        fab.setOnClickListener(this);
        changePasswordHint();
    }

    private void changePasswordHint() {
        confirm = sharedPrefs.getInt("confirm", 0);
        switch (confirm) {
            case 0:
                edtPw.setHint(getResources().getText(R.string.password_hint));
                break;
            case 1:
                edtPw.setHint(getResources().getText(R.string.pw_confirm));
                break;
            case 2:
                edtPw.setHint(getResources().getText(R.string.password_hint));
                break;
        }
    }

    private void initView() {
        edtPw = findViewById(R.id.edtPassword);
        fab = findViewById(R.id.fab);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                String notify = "";
                confirm = sharedPrefs.getInt("confirm", 0);

                // xac dinh mat khau, moi nhap hay nhap lai
                if (edtPw.getText().toString().compareTo("") == 0) {
                    notify = getResources().getText(R.string.password_emty).toString();
                    Toast.makeText(SignInActivity.this, notify, Toast.LENGTH_SHORT).show();
                } else {
                    if (confirm == 0) {
                        editor.putInt("confirm", 1);
                        editor.putString("password", edtPw.getText().toString());
                        editor.commit();
                        edtPw.setText("");
                        edtPw.setHint(getResources().getText(R.string.pw_confirm));
                    } else if (confirm == 1) {
                        String pw = edtPw.getText().toString();
                        String pwd = sharedPrefs.getString("password", null);
                        if (pwd.compareTo("") == 0 || pwd.compareTo(pw) != 0) {
                            editor.putInt("confirm", 0);
                            editor.putString("password", null);
                            editor.commit();

                            notify = getResources().getText(R.string.pw_confirm_err).toString();
                            Toast.makeText(SignInActivity.this, notify, Toast.LENGTH_SHORT).show();
                            edtPw.setText("");
                            edtPw.setHint(getResources().getText(R.string.password_hint));
                        } else {
                            editor.putInt("confirm", 2);
                            editor.commit();

                            notify = getResources().getText(R.string.pw_confirm_seccessful).toString();
                            Toast.makeText(SignInActivity.this, notify, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else if (confirm == 2) {
                        String pw = edtPw.getText().toString();
                        String pwd = sharedPrefs.getString("password", null);
                        if (pwd.compareTo("") == 0 || pwd.compareTo(pw) != 0) {
                            notify = getResources().getText(R.string.pw_wrong).toString();
                            Toast.makeText(SignInActivity.this, notify, Toast.LENGTH_SHORT).show();
                            edtPw.setText("");
                        } else {
                            notify = getResources().getText(R.string.password_correct).toString();
                            Toast.makeText(SignInActivity.this, notify, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
