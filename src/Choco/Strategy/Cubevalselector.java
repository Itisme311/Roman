/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco.Strategy;

import Choco.ChocoCarCube;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author itism
 */
public class Cubevalselector implements IntValueSelector{
    private Cubeorder CO;

    public Cubevalselector(Cubeorder Co) {
        CO=Co;
    }

    @Override
    public int selectValue(IntVar intvar) {
        /*ChocoCarCube ccc=CO.getCube();
        if(ccc.Cost>0)return ccc.chocovar.getLB();
        else return ccc.chocovar.getUB();*/
        if(intvar.getName().charAt(0)=='!')return intvar.getLB();
        else return intvar.getUB();
    }
    
}
