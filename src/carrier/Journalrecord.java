/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrier;

import java.util.List;

/**
 *
 * @author User
 */
public class Journalrecord {
    public List<CarCube> cubes;
    public int year;
    public int yearweight;
    public int yearore;

    
    public Journalrecord(List<CarCube> cubes, int year, int yearore, int yearweight) {
        this.cubes = cubes;
        this.year = year;
        this.yearweight = yearweight;
        this.yearore = yearore;
    }
    
    public void recount(){
        int orc=0;
        int wei=0;
        for(CarCube c:cubes){
            if(c.Cost>0)orc+=c.getVolume();
            wei+=c.getVolume();
        }
        yearore=orc;
        yearweight=wei;
    
    }
    
    
    @Override
    public String toString(){
        String ret;
        double dwdo=yearweight;
        dwdo=dwdo/yearore;
        ret="Year:"+year+"\ndescube weight:"+yearweight+"\ndescube ore:"+yearore+"\ndw/do:"+dwdo;
        /*ret+="\nCubes:";
        for(Cube c:cubes)
            ret+="\n"+c.toString();*/
        ret+="\n";    
        return ret;
    }
   
}
