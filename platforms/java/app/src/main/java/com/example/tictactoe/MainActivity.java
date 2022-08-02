package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.rubicon.game.impl.FieldCell;
import org.rubicon.game.impl.Game;
import org.rubicon.game.impl.events.GameCellEvent;
import org.rubicon.game.impl.events.GameEventType;
import org.rubicon.game.impl.events.GameOverEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    final private Game game = new Game();
    final private LinkedHashMap<String, Button> uiButtons = new LinkedHashMap<>();

    Function1 onGameOver = o -> {
        final GameOverEvent event = (GameOverEvent) o;
        final TextView textView = findViewById(R.id.textWinner);
        String winnerMessage = "";
        switch (event.getWinner()) {
            case NONE:
                winnerMessage = "Ничья";
                break;
            case ZERO:
                winnerMessage = "Выиграли O";
                break;
            case CROSS:
                winnerMessage = "Выиграли X";
                break;
        }
        textView.setText(winnerMessage);
        textView.setVisibility(View.VISIBLE);
        findViewById(R.id.buttonPlayAgain).setVisibility(View.VISIBLE);
        return Unit.INSTANCE;
    };

    Function1 onGameCellChange = o -> {
        final GameCellEvent event = (GameCellEvent) o;
        Log.d("GAME", event.getFieldCell().getX() + " " + event.getFieldCell().getY());
        final FieldCell fieldCell = event.getFieldCell();
        final String hash = makeHash(fieldCell.getX(), fieldCell.getY());
        final Button uiButton = this.uiButtons.get(hash);
        if (uiButton == null) {
            return Unit.INSTANCE;
        }
        if (!uiButton.hasOnClickListeners()) {
            uiButton.setOnClickListener(view -> {
                fieldCell.click();
            });
        }
        switch (event.getFieldCell().getState()) {
            case NONE:
                uiButton.setText("");
                break;
            case ZERO:
                uiButton.setText("O");
                break;
            case CROSS:
                uiButton.setText("X");
                break;
        }
        return Unit.INSTANCE;
    };

    @NonNull
    private String makeHash(int x, int y) {
        return String.format("%s-%s", x, y);
    }

    private void collectButtons() {
        final LinearLayout rowLayout = findViewById(R.id.fieldLinearLayout);
        for (int y = 0; y < rowLayout.getChildCount(); y++) {
            final View child = rowLayout.getChildAt(y);
            final LinearLayout cellLayout = child instanceof LinearLayout ? (LinearLayout) child : null;
            if (cellLayout == null) {
                continue;
            }
            for (int x = 0; x < cellLayout.getChildCount(); x++) {
                final View subChild = cellLayout.getChildAt(x);
                final Button button = subChild instanceof Button ? (Button) subChild : null;
                if (button != null) {
                    uiButtons.put(makeHash(x, y), button);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.collectButtons();

        final Button buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(v -> {
            game.on(GameEventType.GAME_OVER, onGameOver);
            game.on(GameEventType.CHANGE_CELL, onGameCellChange);
            game.play();
            buttonPlay.setVisibility(View.INVISIBLE);
            findViewById(R.id.fieldLinearLayout).setVisibility(View.VISIBLE);
        });

        final Button buttonPlayAgain = findViewById(R.id.buttonPlayAgain);
        buttonPlayAgain.setOnClickListener(v -> {
            findViewById(R.id.textWinner).setVisibility(View.INVISIBLE);
            game.reset();
            buttonPlayAgain.setVisibility(View.INVISIBLE);
        });
    }
}