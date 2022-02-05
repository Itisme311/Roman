/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco;

import carrier.CarCube;
import carrier.Carrier;
import static java.lang.Math.pow;
import java.util.ArrayList;
import mygame.CubeVis;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author itism
 */
public class ChocoCarCube extends CarCube{
    
    private Model model;
    public IntVar chocovar, costvar;
    public CubeVis CV;
    public int minvol, minorevol;
    public ChocoCarCube(double x,double y,double z, double dx, double dy,double dz, double orep, double cost,Carrier c){
        super(x,y,z,dx,dy,dz,orep,cost,c);
    }
    
    public int getYear(){
        if(year!=-1) return year;
        if(chocovar.isInstantiated())return chocovar.getValue();
        return -1;
    }


    public ArrayList<ChocoCarCube> getdepcubes(){
        ArrayList <ChocoCarCube>ret=new ArrayList();
        int i=car.cubes.indexOf(this);
        for(int j=i+1; j<car.cubes.size(); j++)
            if(car.deptable[i][j])ret.add(car.cubes.get(j));
        return ret;
    }
    
    public void getminvol(){
        minvol=0;
        minorevol=0;
        int i=car.cubes.indexOf(this);
        for(ChocoCarCube ccc:this.getdepcubes()){
                double cubevol=ccc.getVolume();
                minvol+=cubevol;
                if(ccc.Cost>0)minorevol+=cubevol;
        }
    }
    
    private int precutdomain(int upweight, int uporewight){
            int cutyear=minvol/upweight;
            if(minvol%upweight>0)cutyear++;
            int cutoreyear=minorevol/uporewight;
            if(minorevol%uporewight>0)cutoreyear++;
            if(cutyear<cutoreyear)cutyear=cutoreyear;
            if(cutyear>0)return cutyear;
            return 1;
    }

    
    
    public void initChoco(Model mdl, boolean withopt,int upweight, int uporewight){
        model=mdl;
        String nam=X+";"+Y+";"+Z;
        if(Cost>0)nam="!"+nam;
        chocovar=model.intVar(nam, precutdomain(upweight, uporewight),car.exc.years);
        if(withopt){
            int [] costs=new int[car.exc.years];
            Tuples tpl=new Tuples(true);
            for(int i=0; i<car.exc.years; i++){
                int cst=(int)(pow(0.9,i)*Cost*car.exc.costmultiplier);
                if(Cost<0)costs[i]=cst;
                else costs[costs.length-1-i]=cst;
                tpl.add(new int[]{i+1,cst});
            }

            costvar=model.intVar("CST"+X+";"+Y+";"+Z,costs);
            model.table(chocovar,costvar, tpl).post();
        }
        
    }

    
}
