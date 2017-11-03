package me.wondertwo.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.wondertwo.sillysxh.TinyButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TinyButton tinyButton = (TinyButton) findViewById(R.id.tiny_toggle_button_1);
        tinyButton.setTinyStyle(TinyButton.Style.TINY_STYLE_A);
        tinyButton.setAnimatorEnable(true);
        tinyButton.setChecked(false);
        tinyButton.setAnimatorDuration(200);
        tinyButton.setOnToggleClickListener(new TinyButton.OnToggleClickListener() {
            @Override
            public void onToggleClick(TinyButton button, boolean isChecked) {
                // TODO: 2017.10.28
            }
        });
        ((TinyButton) findViewById(R.id.tiny_toggle_button_2)).setTinyStyle(
                TinyButton.Style.TINY_STYLE_B);
        ((TinyButton) findViewById(R.id.tiny_toggle_button_3)).setTinyStyle(
                TinyButton.Style.TINY_STYLE_C);

    }
}
