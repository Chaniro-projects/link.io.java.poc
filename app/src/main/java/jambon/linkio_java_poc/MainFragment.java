package jambon.linkio_java_poc;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import link.io.java.LinkIO;
import link.io.java.model.Event;
import link.io.java.model.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView messagesView;
    private EditText inputMessageView;
    private List<Message> messages = new ArrayList<>();
    private RecyclerView.Adapter messageAdapter;
    private String usermail;
    private String password;
    private String room;

    private LinkIO lio;

    public MainFragment(){
        super();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        messageAdapter = new MessageAdapter(activity, messages);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //linkio.on("message", (ConnectIO.Listener) onNewMessage);
        //linkio.on("user joined", (ConnectIO.Listener) onUserJoined);
        //linkio.on("user left", (ConnectIO.Listener) onUserLeft);

        //startLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messagesView = (RecyclerView) view.findViewById(R.id.messages);
        messagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesView.setAdapter(messageAdapter);

        inputMessageView = (EditText) view.findViewById(R.id.message_input);
        inputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    trySend();
                    return true;
                }
                return false;
            }
        });

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySend();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK != resultCode){
            getActivity().finish();
            return;
        }

        usermail = data.getStringExtra("usermail");
        addLog("Welcome to Link.IO Chat");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
    }

    private void addLog(String message){
        messages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        messageAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void addMessage(String usermail, String message){
        messages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .usermail(usermail)
                .message(message)
                .build());
        messageAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void trySend(){
        String message = inputMessageView.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            inputMessageView.requestFocus();
            return;
        }

        inputMessageView.setText("");
        addMessage(usermail, message);

        //linkio.send("message", message);
    }

    /*private void startLogin(){
        usermail = null;
        password = null;
        room = null;

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }*/

    private void leave(){
        usermail = null;
        //linkio.disconnect();
        //startLogin();
    }

    private void scrollToBottom() {
        messagesView.scrollToPosition(messageAdapter.getItemCount()-1);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Event ev = (Event) args[0];
                    //String usermail = ev.getString("usermail");
                    //String message = ev.get("message", String.class);
                    //addMessage(usermail, message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // Event ev = (Event) args[0];
                   // usermail = ev.get("usermail", String.class);
                    //addLog("{" + usermail + "} joined");
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Event ev = (Event) args[0];
                    //String usermail = ev.get("usermail", String.class);
                    //addLog("{" + usermail + "} left");
                }
            });
        }
    };
}
