package siplaundry;


import java.util.Timer;
import java.util.TimerTask;

import siplaundry.service.StatusService;
import siplaundry.view.auth.LoginView;

public class App {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
               StatusService coba = new StatusService();
               coba.ChangeToFinishAuto();
            }
        };

        timer.schedule(task, 0, 10000);
        LoginView.main(args);
    }
}
