package pack.mp_team5project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LineView extends View {

    private Paint paint;
    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defaultatt) {
        super(context, attrs, defaultatt);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(5); // 선의 두께
        paint.setColor(Color.rgb(39, 66, 145));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 시작점과 끝점 좌표 설정
        float startX = 0;
        float startY = 50;
        float endX = getWidth();
        float endY = 50;

        // 선 그리기
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
}

