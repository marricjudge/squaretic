package scm.aurichter2.TTT81;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);


        Button btn_start_game = findViewById(R.id.button_start_game);
        btn_start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_start_game = new Intent(MainActivity.this, board.class);
                startActivity(intent_start_game);
            }
        });

        Button btn_howto = findViewById(R.id.button_howto);
        btn_howto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_howto = new Intent(MainActivity.this, HowTo.class);
                startActivity(intent_howto);
            }
        });

        Button btn_about = findViewById(R.id.button_about);
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent_about);
            }
        });

    }


}
