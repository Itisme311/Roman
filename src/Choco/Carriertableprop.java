/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco;

import carrier.Carrier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

/**
 *
 * @author itism
 */
public class Carriertableprop extends Propagator<IntVar>{
    private Carrier car;
    private LinkedList<Integer> Q;
    public Carriertableprop(Carrier cr){
        super(cr.getintvars(),PropagatorPriority.TERNARY, true);
        Q=new LinkedList();
        car=cr;
    }
    
    
    private void inst(){
        int count=0;
        for(IntVar IV:vars){
            if(IV.isInstantiated())count++;
        }
        System.out.println(count+" of "+vars.length+" is instanced");
    }
    
    private void update(int ii) throws ContradictionException{
        ChocoCarCube ccc=car.cubes.get(ii);
        int maxlb=0;
        for(int i=ii+1; i<car.cubes.size();i++){
            if(car.deptable[ii][i]){
                ChocoCarCube tmp=car.cubes.get(i);
                int lb=tmp.chocovar.getLB();
                if(maxlb<lb)maxlb=lb;
                if(tmp.chocovar.updateUpperBound(ccc.chocovar.getUB(), this))Q.add(i);
            }
        }
        if(ccc.chocovar.updateLowerBound(maxlb, this))Q.add(ii);
        for(int i=0; i<ii;i++){
                if(car.deptable[i][ii]){
                ChocoCarCube tmp=car.cubes.get(i);
                if(tmp.chocovar.updateLowerBound(ccc.chocovar.getLB(), this))Q.add(i);
            }

        
        }
    }
    
    @Override
    public void propagate(int i) throws ContradictionException {
        for(int ii=0; ii<car.cubes.size(); ii++){
            update(ii);
        }
        while(!Q.isEmpty()){
            update(Q.pollFirst());
        }
    }

    @Override
    public void propagate(int ind, int mask) throws ContradictionException {
        update(ind);
        while(!Q.isEmpty()){
            update(Q.pollFirst());
        }
    }

    
    
    @Override
    public ESat isEntailed() {
        for(IntVar v:super.vars)
            if(!v.isInstantiated())return ESat.UNDEFINED;
        return ESat.TRUE;
    }
    
}

class PropTableComparator implements Comparator<ChocoCarCube>{
    @Override
    public int compare(ChocoCarCube a, ChocoCarCube b){
        if(a.Z<b.Z)return -1;
        return 1;
    }
}
