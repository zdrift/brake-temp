package zdrift.braketemp2;

import android.util.Log;

/**
 * Created by Martin on 13.02.2017.
 *
 * 2017-02-13: Basis Modell ohne Berücksichtigung Potenzielle Energie
 */

public class ThermalModel extends Physics{
    double ekin;
    double epot;
    double qkonv_out;
    double qkonv_center;
    double qrad;
    double tdelta;
    double tabsolut;
    double Qgen;
    double Qdis;

    double ekin_old;


    ThermalModel(){
        this.ekin=0.0;
        this.epot=0.0;
        this.qkonv_center=0.0;
        this.qkonv_out=0.0;
        this.qrad=0.0;
        this.tdelta=0.0;
        this.tabsolut=20.0;
        this.Qgen=0.0;
        this.Qdis=0.0;
        this.ekin_old=0.0;
    }

    double fct_Qgen(double Ekin, double Epot){
        return Ekin+Epot;
    }

    double fct_Qdis(double Qkonv_out, double Qkonv_center,double Qrad){
        return Qkonv_out+Qkonv_center+Qrad;
    }

    double fct_calc_Temp(Vehicle veh){
        try {
            try {
                ekin = this.fct_Ekin(veh.speed, veh.mass);
            }catch (Exception e){
                ekin=0;
                Log.e("ekin","exception");
                return -1;
            }

            try {
                //brake active kinetic engery must decrease
                if(ekin_old>ekin){
                    Qgen = fct_Qgen(ekin_old-ekin, 0);//toto Epot berücksichtigen
                }else{
                    Qgen=0;
                }
                ekin_old=ekin;
                Log.e("Qgen:",""+Qgen);
            }catch (Exception e){
                Qgen=0;
                Log.e("Qgen","exception");
                return -1;
            }

            Log.e("vehBrakeD",""+veh.mRaceBrake.D);

            try {
                qkonv_out = fct_free_convection(fct_alpha_air(veh.speed),
                        veh.mRaceBrake.fct_A_Brake(veh.mRaceBrake.D, veh.mRaceBrake.d)
                        , tabsolut,
                        veh.airtemp);
            }catch(NullPointerException e){
                Log.e("qkonv_out","null pointer exception");
                return -1;
            }catch(Exception e){
                Log.e("qkonv_out","exeption");
                return -1;
            }

            try {
                qkonv_center = this.fct_free_convection(fct_alpha_air(veh.speed * veh.mRaceBrake.D / (veh.drad * Math.PI)), veh.mRaceBrake.fct_A_Brake(veh.mRaceBrake.D, veh.mRaceBrake.d), tabsolut, veh.airtemp);

                Qdis = fct_Qdis(qkonv_out, qkonv_center, 0); //todo Qrad

                Log.e("Qdis:",""+Qdis);

                tdelta = (Qgen - Qdis) / (veh.mRaceBrake.c_specific * veh.mRaceBrake.mass);

                tabsolut = tabsolut + tdelta;
            }catch(Exception e){
                Log.e("qkonv_out","calc T absolut failed");
                return -1;
            }

            return tabsolut;
        }catch (NullPointerException  e){
            Log.e("fct_calc_Temp","null pointer exception");
            return -1;
        }catch (Exception e){
            Log.e("fct_calc_Temp","exception");
            return -1;
        }
    }

}
