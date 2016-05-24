package jambon.linkio_java_poc;

import android.graphics.*;

public class Image extends DrawObject{
    private Bitmap bitmap;
    private Rect bitmapZone;
    private Rect drawZone;
    private int x;
    private int y;
    private int w;
    private int h;

    public Image(Bitmap bitmap, int x, int y, int w, int h){
        super();
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        bitmapZone = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawZone = new Rect(x, y, x+w, y+h);
    }

    @Override
    public void draw(android.graphics.Canvas canvas){
        canvas.drawBitmap(getBitmap(), getBitmapZone(), getDrawZone(), paint);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getBitmapZone() {
        return bitmapZone;
    }

    public void setBitmapZone(Rect bitmapZone) {
        this.bitmapZone = bitmapZone;
    }

    public Rect getDrawZone() {
        return drawZone;
    }

    public void setDrawZone(Rect drawZone) {
        this.drawZone = drawZone;
    }
}
