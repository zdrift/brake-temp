package zdrift.braketemp2;
import zdrift.braketemp2.Brake;

/**
 * Created by Martin on 12.02.2017.
 */

public class Vehicle {

    double mass;
    double balancing; //Bremskraftverteilung Vorder-/Hinterachse
    double speed;
    double cw;
    double airtemp;
    double drad;
    Brake mRaceBrake;



    Vehicle(double mass, double balancing, double speed, double cw,Brake mRaceBrake,double airtemp, double drad){
        this.mass = mass;
        this.balancing = balancing;
        this.speed = speed;
        this.mRaceBrake = mRaceBrake;
        this.cw = cw;
        this.airtemp = airtemp;
        this.drad = drad;
    }

    //Default BMW z4 E85
    Vehicle(){
        this.mass = 1290;
        this.balancing = 0.8;
        this.speed = 10.0;
        this.cw = 0.3;
        this.airtemp = 20.0;
        this.drad = 0.8; //todo überprüfen 28zoll->0,71m + 9cm proz Höhe Gummi
        mRaceBrake = new Brake();
    }




}
