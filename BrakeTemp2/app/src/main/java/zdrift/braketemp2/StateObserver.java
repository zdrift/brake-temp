package zdrift.braketemp2;

import android.net.NetworkInfo;

/**
 * Created by Martin on 13.02.2017.
 */

//This class trys to figure out the vehicle state based on measurement data
public class StateObserver {
    double old_speed;
    double delta_speed;
    double brake_level; //acceleration level below this level the observer detects that the vehicle is braking

    StateObserver(double old_speed, double brake_level, double delta_speed){
        this.old_speed   = old_speed;
        this.brake_level = brake_level;
        this.delta_speed = delta_speed;
    }

    StateObserver(){
        this.old_speed = 0.0;
        this.brake_level = 0.1;
        this.delta_speed = 0.0;
    }

    Boolean fct_BrakeActive(double speed){
        delta_speed = speed-old_speed;
        old_speed = speed;

        if(delta_speed<-brake_level){
            return true;
        }
        return false;
    }
}
