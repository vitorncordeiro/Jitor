package logging;

import java.time.LocalDateTime;

public class Logger {
    public static void time(){
        String now = LocalDateTime.now().toString();
        System.out.println(now.substring(0, 19) + " ");
    }
    public static void info(String msg){
        time();
        System.out.print("[INFO]: " + msg);
    }
    public static void error(String msg){
        time();
        System.out.print("[ERROR]: " + msg);
    }
}
