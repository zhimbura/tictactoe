package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.rubicon.game.impl.Game;
import org.rubicon.game.impl.events.GameCellEvent;
import org.rubicon.game.impl.events.GameEventType;
import org.rubicon.game.impl.events.GameOverEvent;

import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private Game game = new Game();

    Function1 onGameOver = o -> {
        GameOverEvent event = (GameOverEvent) o;
        Log.d("GAME", event.getWinner().name());
        return null;
    };
    Function1 onGameCellChange = o -> {
        GameCellEvent event = (GameCellEvent) o;
        Log.d("GAME", event.getFieldCell().getX() + " " + event.getFieldCell().getY());
        return null;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.playButton);
        button.setOnClickListener(v -> {
            game.on(GameEventType.GAME_OVER, onGameOver);
            game.on(GameEventType.CHANGE_CELL, onGameCellChange);
            game.play();
            button.setVisibility(View.INVISIBLE);
        });
    }


}