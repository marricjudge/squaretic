package scm.aurichter2.TTT81;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class background extends View {

    public background(Context context) {
        super(context);
    }

    public background(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public background(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.DKGRAY);

    }
}
