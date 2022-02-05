/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author itism
 */
public class ChocoTest {
    Model model;
    IntVar[] vars;
    int count=5;
    public ChocoTest(){
        model = new Model();
    
    }
    
    public void build(){
        vars = new IntVar[count];
        for(int i=0; i<count;i++){
            vars[i]=model.intVar(1, 5);
        }
        model.sort(vars, vars).post();
    }
    public void run(){
        Solver solver = model.getSolver();
        while(solver.solve()){
            System.out.println("Solution:");
            String ret="";
            for(int i=0; i<count;i++)
                ret+=vars[i].getValue()+" ";
            System.out.println(ret);
        }
    
    
    }
}
