package jambon.linkio_java_poc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages){
        messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        int layout = -1;
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message;
                break;
            case Message.TYPE_LOG:
                layout = R.layout.item_log;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.item_action;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Message message = messages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsermail(message.getmUsermail());
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    @Override
    public int getItemViewType(int position){
        return messages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView usermailView;
        private TextView messageView;

        public ViewHolder (View itemView){
            super(itemView);

            usermailView = (TextView) itemView.findViewById(R.id.user_mail);
            messageView = (TextView) itemView.findViewById(R.id.message);
        }

        public void setUsermail(String usermail){
            if(usermailView == null) return;
            usermailView.setText(usermail);
        }

        public void setMessage(String message){
            if(messageView == null) return;
            messageView.setText(message);
        }
    }

}
