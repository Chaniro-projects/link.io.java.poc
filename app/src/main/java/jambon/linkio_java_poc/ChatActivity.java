package jambon.linkio_java_poc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import link.io.java.model.Event;
import link.io.java.model.EventListener;
import link.io.java.model.User;

public class ChatActivity extends ActionBarActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        LinkIOApplication.lio.on("message", new EventListener() {
            @Override
            public void eventReceived(final Event event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ChatMessage m = new ChatMessage();
                            JSONObject tmp = (JSONObject) event.getData().get("author");
                            m.setMail((String) tmp.get("Mail"));
                            m.setName((String) tmp.get("Name"));
                            m.setFirstname((String) tmp.get("FirstName"));
                            m.setMessage((String) event.getData().get("text"));
                            displayMessage(m);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.gotoboard) {
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMail(LinkIOApplication.lio.getCurrentUser().getMail());
                chatMessage.setName(LinkIOApplication.lio.getCurrentUser().getName());
                chatMessage.setFirstname(LinkIOApplication.lio.getCurrentUser().getFirstName());
                chatMessage.setMessage(messageText);
                JSONObject o = new JSONObject();
                JSONObject o1 = new JSONObject();
                LinkIOApplication.currentUser = LinkIOApplication.lio.getCurrentUser();
                try {
                    o1.put("Name", LinkIOApplication.currentUser.getName());
                    o1.put("FirstName", LinkIOApplication.currentUser.getFirstName());
                    o1.put("Role", LinkIOApplication.currentUser.getRole());
                    o1.put("Mail", LinkIOApplication.currentUser.getMail());
                    o1.put("ID", LinkIOApplication.currentUser.getId());
                    o.put("author", o1);
                    o.put("text", messageText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                LinkIOApplication.lio.send("message", o, false);

                messageET.setText("");

                displayMessage(chatMessage);
            }
        });


    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }
}
