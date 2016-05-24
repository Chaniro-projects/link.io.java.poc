package jambon.linkio_java_poc;

public class Message {
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    private int type;
    private String message;
    private String usermail;

    public int getType(){
        return type;
    }

    public String getMessage(){
        return message;
    }

    public String getmUsermail(){
        return usermail;
    }

    public static class Builder{
        private int type;
        private String usermail;
        private String message;

        public Builder(int type){
            type = type;
        }

        public Builder usermail(String usermail){
            usermail = usermail;
            return this;
        }

        public Builder message(String message){
            message = message;
            return this;
        }

        public Message build(){
            Message message = new Message();
            message.type = type;
            message.message = this.message;
            message.usermail = usermail;
            return message;
        }
    }
}