package app.com.minesweeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends Activity {

    public static ToggleButton clickEvent;
    public static BlockButton[][] block = new BlockButton[9][9];
    public static int[][] position = new int[9][9];
    TableLayout table;
    TextView mines;
    Dialog dialog;
    int flag = 0, dem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Mapping();
        CreateTableLayout();
    }

    private void Mapping() {
        table = (TableLayout) findViewById(R.id.Table);
        clickEvent = (ToggleButton) findViewById(R.id.ClickEvent);
        mines = (TextView) findViewById(R.id.Mines);
    }

    private void CreateTableLayout() {
        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < 9; j++) {
                block[i][j] = new BlockButton(this, i, j);
                TableRow.LayoutParams layoutParams =
                        new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT,
                                1.0f);
                block[i][j].setLayoutParams(layoutParams);
                block[i][j].setOutlineProvider(null);
                tableRow.addView(block[i][j]);
                ClickEvent(i, j);
            }
            table.addView(tableRow);
        }
        CreateMines();
    }

    private void CreateMines() {
        float ratio = (float) 0.05;
        dem = 0;
        int i, j;
        for (i = 0; i <= 8; i++) {
            for (j = 0; j <= 8; j++)
                position[i][j] = 0;
        }
        while (dem < 10) {
            do {
                i = (int) ((8 - 1) * Math.random()) + 1;
                j = (int) ((8 - 2) * Math.random()) + 1;
            } while (position[i][j] != 0);
            if (position[i][j] == 0)
                Init(i, j, ratio);
        }
    }

    private void Init(int i, int j, float ratio) {
        if (Math.random() < ratio) {
            position[i][j] = -1;
            for (int k = i - 1; k <= i + 1 && k <= 8; k++)
                for (int h = j - 1; h <= j + 1 && h <= 8; h++)
                        if (position[k][h] != -1) {
                            position[k][h]++;
                        }
            dem++;
            for (int k = i - 1; k <= i + 1 && k <= 8; k++)
                for (int h = j - 1; h <= j + 1 && h <= 8; h++)
                    if (k > 0 && h > 0 && position[k][h] != -1 && dem < 10)
                        Init(k, h, ratio);
        }
    }

    private void ClickEvent(int i, int j) {
        block[i][j].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEvent.isChecked() == false && clickEvent.getTag() == null) {
                    if (block[i][j].isFocusable()) ((BlockButton) v).BreakBlock(v);
                }
                if (clickEvent.isChecked() == true && clickEvent.getTag() == null) {
                    if (block[i][j].getText() == "+" || flag < 10) {
                        ((BlockButton) v).ToggleFlag(v);
                        if (block[i][j].getText() == "+") {
                            flag++;
                        } else {
                            flag--;
                        }
                        mines.setText("mines : " + (10 - flag));
                    }
                }
                if (clickEvent.getTag() != null) {
                    Result();
                }

            }
        });
    }

    private void Result() {
        if (dialog != null) dialog.dismiss();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.result);
        AppCompatButton replay = (AppCompatButton) dialog.findViewById(R.id.Replay);
        TextView result = (TextView) dialog.findViewById(R.id.Result);
        if (clickEvent.getTag().equals("1")) {
            result.setText("You Win");
        } else result.setText("Game Over");
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        dialog.show();
    }

}
