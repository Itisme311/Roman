/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Choco.ChocoCarCube;
import Choco.ChocoCarrier;
import carrier.Cube;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author itism
 */
public class CarrierVisualisation {
    ChocoCarrier cc;
    Node[]years;
    Node root;
    Node all;
    AssetManager Am;
    boolean solved;
    CarrierVisualisation(ChocoCarrier CC, Node rn, AssetManager am){
        cc=CC;
        root=rn;
        Am=am;
        all=new Node();
        root.attachChild(all);
        years = new Node[CC.ecar.years];
        for(int i=0; i<years.length; i++){
            years[i]=new Node("Year "+(i+1));
            root.attachChild(years[i]);
        }
        
        
    }
    private void getCubVis(ChocoCarCube c, boolean simple){
            //Node n;
            c.CV= new CubeVis(c, all, solved, Am);
            if(!simple)c.CV.dvis=new DepVis(new HashSet<ChocoCarCube>(c.getdepcubes()),Am,solved,root);
            if(solved) CubeVis.createviscube(c, years[c.getYear()-1], solved, Am, false);
    }
    

    
    public void build(boolean slvd, boolean simple){
        solved=slvd;
        for(ChocoCarCube ccc:cc.ecar.car.cubes)
            getCubVis(ccc, simple);
    
    
    
    }
    
}
