/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrier;

import Choco.ChocoCarCube;

/**
 *
 * @author itism
 */
public class CarCube extends Cube{
    public int year;
    public double dX,dY,dZ, Orepart, Cost;
    protected final Carrier car;
    
    public ChocoCarCube getup(){
        int ret[]=car.getind(X, Y, Z);
        return car.getCube(ret[0], ret[1], ret[2]+1);
    }
    public ChocoCarCube getdown(){
        int ret[]=car.getind(X, Y, Z);
        return car.getCube(ret[0], ret[1], ret[2]-1);
    }
    
    public ChocoCarCube getright(){
        int ret[]=car.getind(X, Y, Z);
        return car.getCube(ret[0]+1, ret[1], ret[2]);
    }
    public ChocoCarCube getleft(){
        int ret[]=car.getind(X, Y, Z);
        return car.getCube(ret[0]-1, ret[1], ret[2]);
    }

    public ChocoCarCube getforw(){
        int ret[]=car.getind(X, Y, Z);
        return car.getCube(ret[0], ret[1]+1, ret[2]);
    }
    public ChocoCarCube getback(){
        int ret[]=car.getind(X, Y, Z);
        return car.getCube(ret[0], ret[1]-1, ret[2]);
    }

    
    public CarCube(double x,double y,double z, double dx, double dy,double dz, double orep, double cost,Carrier c){
        super(x,y,z);
        dX=dx;
        dY=dy;
        dZ=dz;
        Orepart=orep;
        Cost=cost;
        car=c;
        year=-1;
    }
    
    public double getVolume(){
        return 1;
        //return dX*dY*dZ;
    }
    
    
}

