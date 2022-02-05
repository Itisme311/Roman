/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Choco;

import Choco.Strategy.Cubeorder;
import Choco.Strategy.Cubeselector;
import Choco.Strategy.Cubevalselector;
import carrier.CarCube;
import carrier.CarSolution;
import carrier.Journalrecord;
import excel.Excbuilder;
import excel.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import static org.chocosolver.solver.search.strategy.Search.intVarSearch;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author User
 */
public class ChocoCarrier {
    public Excbuilder ecar;
    Model model;
    List<CarSolution> solutions;
    public double orenorm, weightnorm,Dweight,Dore;
    IntVar Optcost;
    public int maxcost;
    public int mincost;
    public IntVar[] chocovars;
    public IntVar[] costvars;
    boolean withopt;

    public ChocoCarrier() {
        solutions = new ArrayList<>();
        model = new Model("carrier");
        
    }
    
    private void chocoinitialisation(){
        for(ChocoCarCube cc:ecar.car.cubes){
            cc.getminvol();
            cc.initChoco(model, withopt, (int)(weightnorm+Dweight), (int)(orenorm+Dore));
        }
    }
    
    private void builddigdependencies(){
        int size=ecar.car.cubes.size();
        chocovars = new IntVar[size];
        costvars=new IntVar[size];
        maxcost=(int)(ecar.totalcost*ecar.costmultiplier*1);
        mincost=(int)(ecar.totalcost*ecar.costmultiplier*0.5);
        if(mincost>maxcost){
            int q = mincost;
            mincost=maxcost;
            maxcost=q;
        }
        int i=0;

        for(ChocoCarCube cc:ecar.car.cubes){
            chocovars[i]=cc.chocovar;
            costvars[i]=cc.costvar;
            i++;
        }
    }
    
    public ChocoCarCube getcube(IntVar iv){
        for(ChocoCarCube cc:ecar.car.cubes)
            if(cc.chocovar==iv)return cc;
        return null;
    }
    
    
    
    
    
    private CarSolution buildsolution(long tim){
        CarSolution ret = new CarSolution();
        ret.time=tim;
        if(withopt){
            ret.Cost=Optcost.getValue();
            ret.Cost/=ecar.costmultiplier;
        }
        Journalrecord [] lists= new Journalrecord[ecar.years];
        for(int i=0; i<ecar.years; i++){
            lists[i]=new Journalrecord(new ArrayList<CarCube>(), i+1,0,0);
            ret.addrecord(lists[i]);
        }
        for(ChocoCarCube c:ecar.car.cubes){
            int y=c.chocovar.getValue();
            if(y!=0)lists[y-1].cubes.add(c);
        }
        for(int i=0; i<ecar.years; i++)
            lists[i].recount();
        
        return ret;
    
    }
    
    public void buildTask(Excbuilder exc, boolean wopt, Logger log) throws IOException{
        withopt=wopt;
        ecar=exc;
        ecar.sttime=System.currentTimeMillis();
        
        double[] sum=ecar.car.carsummary(model,withopt);
        log.write("Years: "+ecar.years+" dore:"+ecar.dore+" dweight:"+ecar.dwei+"\n");
        log.write("Total cubes: "+exc.car.cubes.size()+"\nTotal volume: "+sum[0]+"\nOrecubes: "+exc.car.orecubes.size()+"\n Orecubes volume: "+sum[1]+"\n");
        weightnorm=sum[0]/ecar.years;
        orenorm=sum[1]/ecar.years;
        Dweight =weightnorm*ecar.dwei;
        Dore=orenorm*ecar.dore;
        //int weightdelta=ecar.weightnorm;
        chocoinitialisation();
        log.write("Chocoinit in "+msintime(System.currentTimeMillis()-ecar.sttime));
        builddigdependencies();
        log.write("Dependencies built in "+msintime(System.currentTimeMillis()-ecar.sttime));
        /*int upornorm=(int)((orenorm+Dore)*ecar.multiplier);
        int downornorm=(int)((orenorm-Dore)*ecar.multiplier);
        int upwnorm=(int)((weightnorm+Dweight)*ecar.multiplier);
        int downwnorm=(int)((weightnorm-Dweight)*ecar.multiplier);*/
        int upornorm=30;
        int downornorm=20;
        int upwnorm=85;
        int downwnorm=0;
        log.write(upornorm+" "+downornorm+":"+upwnorm+" "+downwnorm+":"+IntVar.MIN_INT_BOUND+"("+mincost+")"+" "+IntVar.MAX_INT_BOUND+"("+maxcost+")");
        IntVar [] wnorm = new IntVar[ecar.years];
        IntVar [] onorm = new IntVar[ecar.years];
        IntVar [] orecubes=ecar.car.oregetintvars();
        IntVar [] allcubes=ecar.car.getintvars();
        for(int i=0; i<ecar.years; i++){
            onorm[i]=model.intVar(downornorm,upornorm);
            wnorm[i]=model.intVar(downwnorm,upwnorm);
        }
        Constraint c = new Constraint("Carrier",new Carriertableprop(ecar.car));
        c.post();
        for(int i=0; i<ecar.years;i++){
            model.count(i+1, orecubes, onorm[i]).post();
            model.count(i+1, allcubes, wnorm[i]).post();
        }
        if(withopt){
            Optcost=model.intVar(101542, maxcost);
            model.sum(costvars, "=", Optcost).post();
            model.setObjective(true, Optcost);
        }
        log.write("Task is built in "+msintime(System.currentTimeMillis()-ecar.sttime));
    }
    
    private String msintime(long tim){
        String ret=tim+" ms or ";
            long ms=tim%1000;
            tim=tim/1000;
            long s=tim%60;
            tim=tim/60;
            long min=tim%60;
            tim=tim/60;
            long h=tim%24;
            tim=tim/24;
            long d=tim;
            if(d!=0)ret+=d+" days ";
            if(h!=0)ret+=h+" hours ";
            if(min!=0)ret+=min+" min ";
            if(s!=0)ret+=s+" sec ";
            if(ms!=0)ret+=ms+" ms ";
        return ret;
    
    }
    
    public void solve(Logger log, String path, long timer, boolean first, boolean local) throws IOException, FileNotFoundException, InvalidFormatException{
        Solver solver = model.getSolver();
        Cubeorder CO=new Cubeorder(ecar.car.cubes);
        Cubeselector Csel=new Cubeselector(CO);
        Cubevalselector Cval=new Cubevalselector(CO);
        solver.setSearch(intVarSearch(Csel,Cval,chocovars));
        solver.limitTime(timer);
        long starttime=System.currentTimeMillis();
        int count=1;
        Date dat=new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd'-'hh.mm.ss");
        String pth=path+"_years("+ecar.years+")_dore("+ecar.dore+")_dwei("+ecar.dwei+")";
        if(withopt)pth+="_optimized_";
        if(local)pth+="local_";
        pth+="_"+formatForDateNow.format(dat)+" solution.xlsx";
        while(solver.solve()){
            long tim=System.currentTimeMillis()-starttime;
            log.write("Solution #"+count+" Time:"+msintime(tim));
            CarSolution sl=buildsolution(tim);
            log.write("Total cost:"+ sl.Cost);
            ecar.exsol3d(pth, sl,count);
            log.write("\n--------------\n"+sl.getStr()+"\n--------------\n");
            solutions.add(sl);
            count++;
            if(first) break;
        }
        log.write("Total solutions "+solutions.size());
        String summary="Summary:\n";
        long tim=(System.currentTimeMillis()-starttime);
        summary+="Achieved in "+msintime(tim)+"("+msintime(timer-tim)+"left)\n";
        log.write(summary);
        for(CarSolution sol:solutions){
            summary=sol.Cost+" "+msintime(sol.time)+"\n";
            log.write(summary);

        }
        if(solutions.size()==0)log.write("No solutions");
        log.close();
    
    
    }
}
