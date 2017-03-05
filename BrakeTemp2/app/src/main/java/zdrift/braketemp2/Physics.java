package zdrift.braketemp2;
import android.util.Log;

/**
 * Created by Martin on 12.02.2017.
 */

public class Physics {
    private static double K_BOLZMANN=1.380658e-23;

    Physics(){

    }

    double fct_delta_T(double dQ, double c, double m){
        return dQ/(c*m);
    }

    double fct_alpha_air(double speed_air) {

        Log.e("speed_air:", ""+speed_air);

            return 12 * Math.sqrt(speed_air) + 2;




    }

    //Kinetische Energie des Fahrzeugs
    double fct_Ekin(double speed, double mass){
        //set Ekin to zero for speed below 1 [m/s] to avoid numeric problems
        if (speed <1) {
            return 0.0;
        }else{
            return mass * speed * speed / 2;
        }
    }

    //Freie Konvektion
    double fct_free_convection(double alpha , double A, double T1, double T2){
        Log.e("free conve A:",""+A);
        return (alpha*A*(T1-T2));
    }
    //Wärmestrahlung
    double fct_Radiation(double A, double T, double Em){
        return Em*K_BOLZMANN*A*T*T*T*T;
    }

    //Kreisumfang
    double fct_UCircle(double d){
        return d*Math.PI;
    }

    //Umdrehungen pro Sekunde
    double fct_Rps(double speed, double ucircle){
        return speed/ucircle;
    }

    //Geschwindigkeit Außenkante Kreis
    double fct_SpeedCircle(double rps, double d){
        return rps*d;
    }


}
