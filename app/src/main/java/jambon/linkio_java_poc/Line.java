package jambon.linkio_java_poc;

import android.graphics.*;

/**
 * Created by Francois on 23/05/2016.
 */
public class Line extends DrawObject {
    private static Line lastLine;
    private int color;
    private Path path;

    public static Line getOrNew(int color){
        if(lastLine != null && lastLine.getColor() == color)
            return lastLine;
        else{
            Line l = new Line(color);
            lastLine = l;
            return l;
        }
    }

    public static void clear(){
        lastLine = null;
    }

    private Line(int color){
        super();
        this.color = color;
        this.path = new Path();
    }

    public void line(int x1, int y1, int x2, int y2) {
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
    }

    @Override
    public void draw(android.graphics.Canvas canvas) {
        paint.setColor(getColor());
        canvas.drawPath(getPath(), paint);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
