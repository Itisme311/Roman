/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Choco.ChocoCarCube;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.TreeSet;

/**
 *
 * @author itism
 */
public class CubeVis{
    DepVis dvis;
    public Spatial nd;
    public static Spatial createviscube(ChocoCarCube c, Node n, boolean solved,AssetManager Am, boolean trans){
        Box boxMesh = new Box((float)c.dX*0.5f,(float)c.dZ*0.5f,(float)c.dY*0.5f);
        Geometry boxGeo = new Geometry("translucent cube", boxMesh);
        Material boxMat = new Material(Am, "Common/MatDefs/Misc/Unshaded.j3md");
        boxGeo.setLocalTranslation(new Vector3f((float)c.X,(float)c.Z,(float)c.Y));
        if (trans){
            if(c.Cost>0) boxMat.setTexture("ColorMap", Am.loadTexture("Textures/ore.png"));
            else boxMat.setTexture("ColorMap", Am.loadTexture("Textures/empty.png"));
            boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            boxGeo.setQueueBucket(Bucket.Transparent);
        }
        else 
        if (!solved){
            if(c.Cost>0) boxMat.setTexture("ColorMap", Am.loadTexture("Textures/ore00.png"));
            else boxMat.setTexture("ColorMap", Am.loadTexture("Textures/empty00.png"));
        }
        else{
            int y=c.getYear();
            if(y<1)y=0;
            if(c.Cost>0) boxMat.setTexture("ColorMap", Am.loadTexture("Textures/ore"+y+".png"));
            else boxMat.setTexture("ColorMap", Am.loadTexture("Textures/empty"+y+".png"));
        }
        boxGeo.setMaterial(boxMat);
        n.attachChild(boxGeo);
        return boxGeo;
    }
    CubeVis(ChocoCarCube c, Node n, boolean solved,AssetManager Am){
        nd=createviscube(c,n,solved,Am,true);
    }
    
    
    public void showNode(){
        nd.setCullHint(Spatial.CullHint.Dynamic);
        if(dvis!=null)
            dvis.nd.setCullHint(Spatial.CullHint.Dynamic);
    }
    public void hideNode(){
        nd.setCullHint(Spatial.CullHint.Always);
        if(dvis!=null)
            dvis.nd.setCullHint(Spatial.CullHint.Always);
    }
  
}
