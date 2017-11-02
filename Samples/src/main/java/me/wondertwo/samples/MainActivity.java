package me.wondertwo.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.wondertwo.tinytogglebtn.TinyToggleButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TinyToggleButton tinyToggleButton = (TinyToggleButton) findViewById(R.id.tiny_toggle_button_1);
        tinyToggleButton.setTinyStyle(TinyToggleButton.Style.TINY_STYLE_A);
        tinyToggleButton.setAnimatorEnable(true);
        tinyToggleButton.setChecked(false);
        tinyToggleButton.setAnimatorDuration(200);
        tinyToggleButton.setOnToggleClickListener(new TinyToggleButton.OnToggleClickListener() {
            @Override
            public void onToggleClick(TinyToggleButton button, boolean isChecked) {
                // TODO: 2017.10.28
            }
        });
        ((TinyToggleButton) findViewById(R.id.tiny_toggle_button_2)).setTinyStyle(
                TinyToggleButton.Style.TINY_STYLE_B);
        ((TinyToggleButton) findViewById(R.id.tiny_toggle_button_3)).setTinyStyle(
                TinyToggleButton.Style.TINY_STYLE_C);

    }
}
