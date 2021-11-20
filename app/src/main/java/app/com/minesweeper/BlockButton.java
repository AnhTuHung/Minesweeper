package app.com.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ToggleButton;

public class BlockButton extends androidx.appcompat.widget.AppCompatButton {

    ToggleButton clickEvent = new MainActivity().clickEvent;
    BlockButton[][] block = new MainActivity().block;
    Context view;
    int[][] position = new MainActivity().position;
    int m = 8, n = 8, dem = 0, i, j;

    public BlockButton(Context view, int i, int j) {
        super(view);
        this.view = view;
        this.i = i;
        this.j = j;
    }

    public void BreakBlock(View view) {
        if (position[i][j] != 0 && position[i][j] != -1) {
            Open(i, j);
        } else OpenBlock(i, j);
    }

    private void Open(int i, int j) {
        if (block[i][j].isFocusable() && position[i][j] != -1) {
            block[i][j].setText(String.valueOf(position[i][j]));
            block[i][j].setBackgroundColor(Color.parseColor("#EEEEEE"));
            block[i][j].setFocusable(false);
            CheckWin();
        }
    }

    private void OpenBlock(int i, int j) {
        if (position[i][j] == -1) {
            if (block[i][j].getText() != "+") {
                Loss();
            }
        }

        if (block[i][j].isFocusable() && position[i][j] != -1) {
            block[i][j].setFocusable(false);
            block[i][j].setBackgroundColor(Color.parseColor("#EEEEEE"));
            for (int h = i - 1; h <= i + 1; h++)
                for (int k = j - 1; k <= j + 1; k++)
                    if (h >= 0 && h <= m && k >= 0 && k <= n) {
                        if (position[h][k] == 0 && block[h][k].isFocusable())
                            OpenBlock(h, k);
                        else {
                            Open(h, k);
                        }
                        CheckWin();
                    }
        }
    }

    private void CheckWin() {
        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= n; j++) {
                if (block[i][j].isFocusable() == false) dem++;
            }
        if (dem == 71) clickEvent.setTag(1);
        dem = 0;
    }

    private void Loss() {
        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= n; j++) {
                if (position[i][j] == -1) block[i][j].setText(R.string.bomb);
                clickEvent.setTag(0);
            }
    }

    public void ToggleFlag(View view) {
        BlockButton block = (BlockButton) view;
        if (block.getText() != "") {
            block.setText("");
            block.setFocusable(true);
        } else {
            block.setText("+");
            block.setFocusable(false);
        }
    }
}
