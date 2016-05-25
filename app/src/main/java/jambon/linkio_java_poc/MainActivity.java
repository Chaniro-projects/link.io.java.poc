package jambon.linkio_java_poc;

import android.app.ActionBar;
import android.app.Application;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import link.io.java.LinkIO;
import link.io.java.model.Event;
import link.io.java.model.EventListener;
import link.io.java.model.User;
import link.io.java.model.UserJoinListener;
import link.io.java.model.UserLeftListener;

public class MainActivity extends AppCompatActivity {
    private static final int TAKE_PHOTO = 0;
    private Canvas canvas;
    private Gson gson;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        LinkIOApplication.currentUser = LinkIOApplication.lio.getCurrentUser();

        canvas = (Canvas) findViewById(R.id.canvas);


        /*TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Tab one");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);*/


        final float[] lastX = {-1};
        final float[] lastY = {-1};
        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        JSONObject o = new JSONObject();
                        try {
                            o.put("fromX", (lastX[0] > 0 ? lastX[0] : event.getX()) / canvas.getWidth());
                            o.put("fromY", (lastY[0] > 0 ? lastY[0] : event.getY()) / canvas.getHeight());
                            o.put("toX", event.getX() / canvas.getWidth());
                            o.put("toY", event.getY() / canvas.getHeight());
                            o.put("color", "#2ecc71");
                        }catch (JSONException e) {
                        }
                        System.out.println("--");
                        canvas.drawLine((lastX[0] > 0 ? lastX[0] : event.getX()) / canvas.getWidth(),
                                (lastY[0] > 0 ? lastY[0] : event.getY()) / canvas.getHeight(),
                                (event.getX()) / canvas.getWidth(),
                                (event.getY()) / canvas.getHeight(),
                                "#2ecc71"
                        );
                        System.out.println("--");
                        lastX[0] = event.getX();
                        lastY[0] = event.getY();
                        LinkIOApplication.lio.send("line", o, false);
                        break;

                    case MotionEvent.ACTION_UP:
                        lastX[0] = -1;
                        lastY[0] = -1;
                        break;
                }

                return true;
            }
        });

        LinkIOApplication.lio.on("line", new EventListener() {
            @Override
            public void eventReceived(final Event event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            canvas.drawLine((double) event.getData().get("fromX"),
                                    (double) event.getData().get("fromY"),
                                    (double) event.getData().get("toX"),
                                    (double) event.getData().get("toY"),
                                    (String) event.getData().get("color"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        LinkIOApplication.lio.on("clear", new EventListener() {
            @Override
            public void eventReceived(Event event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        canvas.clear();
                    }
                });
            }
        });

        LinkIOApplication.lio.on("image", new EventListener() {
            @Override
            public void eventReceived(final Event event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            canvas.drawBase64Image(event.getObject(JSONObject.class).getString("img"),
                                    event.getObject(JSONObject.class).getDouble("x"),
                                    event.getObject(JSONObject.class).getDouble("y"),
                                    event.getObject(JSONObject.class).getDouble("w"),
                                    event.getObject(JSONObject.class).getDouble("h")
                            );
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });

            }
        });


        LinkIOApplication.lio.onUserJoinRoom(new UserJoinListener() {
            @Override
            public void userJoin(User user) {
                LinkIOApplication.users.add(user);
            }
        });

        LinkIOApplication.lio.onUserLeftRoom(new UserLeftListener() {
            @Override
            public void userLeft(User user) {
                LinkIOApplication.users.remove(user);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Uri selectedImage = imageReturnedIntent.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(
                                    selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                            }

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String filePath = cursor.getString(columnIndex);
                            cursor.close();

                            File imgFile = new File(filePath);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inDensity = 1;
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                            if (bitmap.getWidth() / bitmap.getHeight() < Canvas.RATIO)
                                bitmap = getResizedBitmap(bitmap, 905, 905 * bitmap.getHeight() / bitmap.getWidth());
                            else
                                bitmap = getResizedBitmap(bitmap, 460 * bitmap.getWidth() / bitmap.getHeight(), 460);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();

                            final String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            JSONObject o = new JSONObject();
                            try {
                                o.put("img", "data:image/jpeg;base64," + encoded);
                                o.put("x", 0);
                                o.put("y", 0);
                                o.put("w", 1);
                                o.put("h", 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            LinkIOApplication.lio.send("image", o, false);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    canvas.drawBase64Image("data:image/jpeg;base64," + encoded, 0, 0, 1, 1);
                                }
                            });
                        }
                    }.start();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, TAKE_PHOTO);
                break;
            case R.id.menu_clear:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        canvas.clear();
                    }
                });
                LinkIOApplication.lio.send("clear", "");
                break;
            case R.id.gotochat:
                Intent intent1 = new Intent(this, ChatActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_quit:
                break;
        }
        if (id == R.id.menu_quit) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://jambon.linkio_java_poc/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://jambon.linkio_java_poc/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
