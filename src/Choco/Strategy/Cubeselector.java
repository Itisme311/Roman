/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco.Strategy;

import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author itism
 */
public class Cubeselector implements VariableSelector<IntVar>{
    private Cubeorder CO;

    public Cubeselector(Cubeorder Co) {
        CO=Co;
    }

    @Override
    public IntVar getVariable(IntVar[] vs) {
        CO.updateorder();
        return CO.getCube().chocovar;
    }
    
}
