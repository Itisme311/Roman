/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco.Strategy;

import Choco.ChocoCarCube;
import carrier.Cube;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

/**
 *
 * @author itism
 */
public class Cubeorder {
    public TreeSet<ChocoCarCube> cubes;
    
    
    public void updateorder(){
        TreeSet<ChocoCarCube> no=new TreeSet<>(new ChocoCubeComparator());
        no.addAll(cubes);
        cubes.clear();
        cubes=no;
    }
    
    public Cubeorder(Collection <ChocoCarCube> cbs){
        cubes = new TreeSet<>(new ChocoCubeComparator());
        cubes.addAll(cbs);
    }
    
    public ChocoCarCube getCube(){
        return cubes.first();
    }
    
    
    public ChocoCarCube pollCube(){
        return cubes.pollFirst();
    }
    
    public void addCube(ChocoCarCube cb){
        cubes.add(cb);
    }
    
    @Override
    public String toString(){
        String ret="";
        for(ChocoCarCube cc:cubes)
            ret+=cc.chocovar.toString()+"\n";
        return ret;
    }
}




class ChocoCubeComparator implements Comparator<ChocoCarCube>{
    @Override
    public int compare(ChocoCarCube a, ChocoCarCube b){
        if(a.chocovar.isInstantiated()&&!b.chocovar.isInstantiated())return 1;
        if(b.chocovar.isInstantiated()&&!a.chocovar.isInstantiated())return -1;
        if(a.Cost>0&&b.Cost<0)return -1;
        if(a.Cost<0&&b.Cost>0)return 1;
        if(a.Cost>0){
            if(a.Z>b.Z)return -1;
            if(a.Z<b.Z)return 1;
        }
        else{
            if(a.Z>b.Z)return 1;
            if(a.Z<b.Z)return -1;
        }
        if(a.Cost>b.Cost)return 1;
        if(a.Cost<b.Cost)return -1;
        //if(a.chocovar.getRange()<b.chocovar.getRange())return -1;
        //if(a.chocovar.getRange()>b.chocovar.getRange())return 1;
        if(a.Y<b.Y)return -1;
        if(a.Y>b.Y)return 1;
        if(a.X<b.X)return -1;
        if(a.X>b.X)return 1;
        return 0;
    }
    /*public int compare(ChocoCarCube a, ChocoCarCube b){
        if(a.chocovar.isInstantiated()&&!b.chocovar.isInstantiated())return 1;
        if(b.chocovar.isInstantiated()&&!a.chocovar.isInstantiated())return -1;
        if(a.Cost>0&&b.Cost<0)return -1;
        if(a.Cost<0&&b.Cost>0)return 1;
        if(a.Z>b.Z)return -1;
        if(a.Z<b.Z)return 1;
        if(a.Cost>b.Cost)return -1;
        if(a.Cost<b.Cost)return 1;
        //if(a.chocovar.getRange()<b.chocovar.getRange())return -1;
        //if(a.chocovar.getRange()>b.chocovar.getRange())return 1;
        if(a.Y<b.Y)return -1;
        if(a.Y>b.Y)return 1;
        if(a.X<b.X)return -1;
        if(a.X>b.X)return 1;
        return 0;
    }*/
}