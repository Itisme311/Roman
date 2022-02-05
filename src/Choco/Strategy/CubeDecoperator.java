/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco.Strategy;

import Choco.ChocoCarCube;
import java.util.LinkedList;
import org.chocosolver.solver.ICause;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author itism
 */
public class CubeDecoperator implements DecisionOperator<IntVar>{
    
    Cubeorder CO;
    LinkedList<ChocoCarCube> qcubes;
    public CubeDecoperator(Cubeorder CO) {
        this.CO = CO;
        qcubes=new LinkedList<>();
    }

    @Override
    public void apply(IntVar v, int i, ICause icause) throws ContradictionException {
        CO.updateorder();
        ChocoCarCube cb=CO.getCube();
        qcubes.add(cb);
        v.instantiateTo(i, icause);
    }

    @Override
    public void unapply(IntVar v, int i, ICause icause) throws ContradictionException {
        //ChocoCarCube cb=qcubes.pollLast();
        v.removeValue(i, icause);
    }

    @Override
    public DecisionOperator<IntVar> opposite() {
        return null; //To change body of generated methods, choose Tools | Templates.
    }
    
}
