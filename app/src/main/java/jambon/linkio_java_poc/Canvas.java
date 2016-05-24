package jambon.linkio_java_poc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;

public class Canvas extends View{
    private Paint paint;
    public static final float RATIO = 905 / 460;
    private Drawable savedCanvas;

    public Canvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#3498db"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10f);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        for(DrawObject d : LinkIOApplication.objects)
            d.draw(canvas);
    }

    public void drawLine(double fromX, double fromY, double toX, double toY, String color) {
        Line l = Line.getOrNew(Color.parseColor(color));
        l.line((int) (fromX * this.getWidth()), (int) (fromY * this.getHeight()), (int) (toX * this.getWidth()), (int) (toY * this.getHeight()));
        if(!LinkIOApplication.objects.contains(l))
            LinkIOApplication.objects.add(l);
        invalidate();
    }

    public void drawBase64Image(String base64, double x, double y, double w, double h) {
        byte[] byteArray= Base64.decode(base64.substring(base64.indexOf(",") + 1), 0);
        Bitmap image= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        LinkIOApplication.objects.add(new Image(image, (int) (x * this.getWidth()), (int) (y * this.getHeight()), (int) (w * this.getWidth()), (int) (h * this.getHeight())));
        invalidate();
    }

    public void drawBinaryImage(byte[] binary, double x, double y, double w, double h) {
        Bitmap image= BitmapFactory.decodeByteArray(binary, 0, binary.length);
        LinkIOApplication.objects.add(new Image(image, (int) (x * this.getWidth()), (int) (y * this.getHeight()), (int) (w * this.getWidth()), (int) (h * this.getHeight())));
        invalidate();
    }

    public void clear() {
        LinkIOApplication.objects.clear();
        Line.clear();
        invalidate();
    }
}
