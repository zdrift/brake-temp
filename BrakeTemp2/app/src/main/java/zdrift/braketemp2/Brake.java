package zdrift.braketemp2;

/**
 * Created by Martin on 12.02.2017.
 */

 class Brake  {

    double temp;
    double mass;
    double c_specific;
    double D;
    double d;
    double h;
    boolean active;

    //Konstruktor
    Brake(double temp, double mass, double c_specific, double D, double d, double h, boolean active){
        this.temp = temp;
        this.mass = mass;
        this.c_specific = c_specific;
        this.D = D;
        this.d = d;
        this.h = h;
        this.active = active;
    }
    //Default Konstruktor (BMW e85 Standardbremse)
    Brake(){
        this.temp           = 20.0;
        this.mass           = 10.0;
        this.c_specific     = 0.46;
        this.D              = 0.300;
        this.d              = 0.079;
        this.h              = 0.022;
        this.active         = false;
    }

    //Berechnung Fläche Bremsscheibe
    double fct_A_Brake(double D, double d){
        return Math.PI/4*(D*D-d*d);
    }

}
