package jambon.linkio_java_poc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import link.io.java.LinkIO;
import link.io.java.LinkIOSetup;
import link.io.java.Main;
import link.io.java.model.ConnectionListener;
import link.io.java.model.JoinRoomListener;
import link.io.java.model.User;
import link.io.java.model.UserJoinListener;
import link.io.java.model.UserLeftListener;

public class LoginActivity extends Activity{
    private EditText usermailView;
    private String usermail;
    private EditText userPasswordView;
    private String password;
    private EditText roomView;
    private String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usermailView = (EditText) findViewById(R.id.user_mail);
        userPasswordView = (EditText) findViewById(R.id.user_password);
        roomView = (EditText) findViewById(R.id.user_room);

        Button signInButton = (Button) findViewById(R.id.login_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void tryLogin() {
        String usermail = usermailView.getText().toString().trim();
        if (TextUtils.isEmpty(usermail)) {
            usermailView.setError("Email required");
            return;
        }
        this.usermail = usermail;

        String password = userPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            userPasswordView.setError("Password required");
            return;
        }
        this.password = password;

        final String room = roomView.getText().toString().trim();
        if (TextUtils.isEmpty(room)) {
            roomView.setError("Room required");
            return;
        }
        this.room = room;

        final LoginActivity tmp = this;

        //Each field is completed, we try to connect to the server
        LinkIOSetup.getInstance().connectTo(Constants.SERVER_ADDRESS)
                .withMail(this.usermail)
                .withPassword(this.password)
                .withAPIKey("BCHY8PwT8foOpn23lJLL")
                .connect(new ConnectionListener() {
                    @Override
                    public void connected(final LinkIO linkIO) {

                        linkIO.joinRoom(room, new JoinRoomListener() {
                            @Override
                            public void roomJoined(String roomID, ArrayList<User> usersInRoom) {
                                linkIO.onUserJoinRoom(new UserJoinListener() {
                                    @Override
                                    public void userJoin(User user) {
                                        System.out.println("in: " + user.getMail());
                                    }
                                });

                                linkIO.onUserLeftRoom(new UserLeftListener() {
                                    @Override
                                    public void userLeft(User user) {
                                        System.out.println("out: " + user.getMail());
                                    }
                                });
                            }
                        });
                        LinkIOApplication.lio = linkIO;
                        Intent intent = new Intent(tmp, MainActivity.class);
                        startActivity(intent);
                    }
                });
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }
}
