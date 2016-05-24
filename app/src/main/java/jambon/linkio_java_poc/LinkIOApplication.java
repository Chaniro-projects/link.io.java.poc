package jambon.linkio_java_poc;

import android.app.Application;

import java.util.ArrayList;

import link.io.java.LinkIO;
import link.io.java.model.User;

public class LinkIOApplication extends Application {
    public static ArrayList<DrawObject> objects = new ArrayList<DrawObject>();
    public static User currentUser;
    public static ArrayList<User> users = new ArrayList<User>();
    public static LinkIO lio;

    public LinkIOApplication(){
        super();
    }
}

