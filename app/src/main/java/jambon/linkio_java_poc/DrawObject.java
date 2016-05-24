package jambon.linkio_java_poc;

import android.graphics.Color;
import android.graphics.Paint;

public abstract class DrawObject {
    protected static Paint paint;

    public DrawObject(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#3498db"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10f);
    }

    public abstract void draw(android.graphics.Canvas canvas);
}
