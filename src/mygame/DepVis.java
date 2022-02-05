/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Choco.ChocoCarCube;
import carrier.Cube;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashSet;
import java.util.TreeSet;

/**
 *
 * @author itism
 */
public class DepVis {
    Node nd;
    HashSet<ChocoCarCube> cubes;
    
        public DepVis(HashSet<ChocoCarCube> c,AssetManager Am, boolean sl, Node root){
        nd=new Node();
        cubes=c;
        for(ChocoCarCube cc:cubes){
            //CubeVis.createviscube(cc, nd, sl, Am,false);
        }
        nd.setCullHint(Spatial.CullHint.Always);
        root.attachChild(nd);
    
    }

    
}
