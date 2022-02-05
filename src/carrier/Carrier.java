/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrier;

import Choco.ChocoCarCube;
import excel.Excbuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author itism
 */
public class Carrier {
    public List <int[]> depend;
    public List <ChocoCarCube> cubes;
    public List <ChocoCarCube> orecubes;
    public ChocoCarCube[][][] arrcar;
    public boolean[][] deptable;
    private double dx,dy,dz;
    private TreeSet<Double> OX;
    private TreeSet<Double> OY;
    private TreeSet<Double> OZ;
    public Excbuilder exc;

    public Carrier(Excbuilder exc) {
        this.cubes = new ArrayList<>();
        this.orecubes = new ArrayList<>();
        this.depend = new ArrayList<>();
        this.OX = new TreeSet<>();
        this.OY = new TreeSet<>();
        this.OZ = new TreeSet<>();
        this.exc = exc;
        builddep();
    }
    
    private void builddep(){
        depend.add(new int[]{0,0,1});
        depend.add(new int[]{1,0,1});
        depend.add(new int[]{-1,0,1});
        depend.add(new int[]{0,1,1});
        depend.add(new int[]{0,-1,1});
        /*depend.add(new int[]{1,1,1});
        depend.add(new int[]{-1,-1,1});
        depend.add(new int[]{-1,1,1});
        depend.add(new int[]{1,-1,1});
        /*depend.add(new int[]{-1,2,1});
        depend.add(new int[]{0,2,1});
        depend.add(new int[]{1,2,1});
        depend.add(new int[]{-1,-2,1});
        depend.add(new int[]{0,-2,1});
        depend.add(new int[]{1,-2,1});
        depend.add(new int[]{2,-1,1});
        depend.add(new int[]{2,0,1});
        depend.add(new int[]{2,1,1});
        depend.add(new int[]{-2,-1,1});
        depend.add(new int[]{-2,0,1});
        depend.add(new int[]{-2,1,1});*/
        /*depend.add(new int[]{0,0,1});
        depend.add(new int[]{1,0,1});
        depend.add(new int[]{-1,0,1});*/
        
    }
    
    public void addCube(ChocoCarCube c){
        cubes.add(c);
        if(c.Cost>0)orecubes.add(c);
        OX.add(c.X);
        OY.add(c.Y);
        OZ.add(c.Z);
    }
    
    public int [] getind(double X,double Y, double Z){
        int[] ret=new int[3];
            ret[0]=(int)((X-OX.first())/dx);
            ret[1]=(int)((Y-OY.first())/dy);
            ret[2]=(int)((Z-OZ.first())/dz);
        return ret;
    }
    
    public ChocoCarCube getCube(double X, double Y, double Z){
        int[] ret=getind(X,Y,Z);
        return arrcar[ret[0]][ret[1]][ret[2]];
    }
    
    public ChocoCarCube getCube(int x,int y,int z){
        ChocoCarCube ret;
        try{ret=arrcar[x][y][z];}
        catch(Exception e){ret=null;}
        return ret;
    }
    
    private void buildtable(){
        TreeSet<ChocoCarCube> no=new TreeSet(new CubeComparator());
        no.addAll(cubes);
        cubes.clear();
        cubes.addAll(no);
        deptable=new boolean[cubes.size()][cubes.size()];
        for(int i=0; i<cubes.size();i++){
            ChocoCarCube ccc=cubes.get(i);
            ArrayList<Integer> ilist= new ArrayList();
            int [] ind=getind(ccc.X,ccc.Y,ccc.Z);
            for(int[]delt:depend){
                ChocoCarCube tmp=getCube(ind[0]+delt[0],ind[1]+delt[1],ind[2]+delt[2]);
                if(tmp!=null){
                    int j=cubes.indexOf(tmp);
                    deptable[i][j]=true;
                    ilist.add(j);
                }
            }
            for(int ii=0; ii<i; ii++){
                if(deptable[ii][i]){
                    for(int q:ilist){
                        deptable[ii][q]=true;
                    }
                }
            }
        }
    
    }
    
    public void carbuild(){
        arrcar=new ChocoCarCube[OX.size()][OY.size()][OZ.size()];
        ChocoCarCube tmp=cubes.get(0);
        dx=tmp.dX;
        dy=tmp.dY;
        dz=tmp.dZ;
        for (ChocoCarCube ccc:cubes){
            int [] ind=getind(ccc.X,ccc.Y,ccc.Z);
            arrcar[ind[0]][ind[1]][ind[2]]=ccc;
        }
        long tim=System.currentTimeMillis();
        buildtable();
        System.out.println("Table is build in "+(System.currentTimeMillis()-tim)+"ms");
    }
    
    
    public IntVar[] getintvars(){
        IntVar[] ret= new IntVar[cubes.size()];
        int i=0;
        for(ChocoCarCube ccc:cubes){
            ret[i]=ccc.chocovar;
            i++;
        }
        return ret;
    }
    public IntVar[] oregetintvars(){
        IntVar[] ret= new IntVar[orecubes.size()];
        int i=0;
        for(ChocoCarCube ccc:orecubes){
            ret[i]=ccc.chocovar;
            i++;
        }
        return ret;
    }
    
    public double[] carsummary(Model mod, boolean withopt){
        double weightcount=0;
        double orecount=0;
        for(ChocoCarCube cub:cubes){
            weightcount+=cub.getVolume();
            if(cub.Cost>0)
                orecount+=cub.getVolume();
        }
        System.out.println("Total blocks: "+cubes.size());
        System.out.println("Total weight: "+(weightcount));
        System.out.println("Total ore blocks: "+orecubes.size());
        System.out.println("Total ore: "+(orecount));
        return new double[]{cubes.size(),orecubes.size()};
    }
class CubeComparator implements Comparator<ChocoCarCube>{
    @Override
    public int compare(ChocoCarCube a, ChocoCarCube b){
        if(a.Z<b.Z)return -1;
        return 1;
    }
}
}
