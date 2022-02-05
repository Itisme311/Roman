package mygame;

import Choco.ChocoCarCube;
import Choco.ChocoCarrier;
import excel.Excbuilder;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial.CullHint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeSet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    static int ycount;
    static double dore,dwei,mult,costmult;
    static boolean simplevis;
    static ChocoCarrier CC;
    ChocoCarCube vis;
    BitmapText hudText;
    CarrierVisualisation CV;
    static boolean solved;
    


    static void showpit(String path, boolean withore) throws IOException, InvalidFormatException{
        Excbuilder ex=new Excbuilder(path,ycount,dore,dwei,mult,costmult,withore, solved);
        excel.Logger logge = new excel.Logger();
        CC=new ChocoCarrier();
        CC.ecar=ex;
        if(!simplevis)CC.buildTask(ex,false,logge);
        Main app = new Main();
        app.start();

    
    
    }
    
    
    public static void main(String[] args) throws IOException, InvalidFormatException {
        Locale.setDefault(new Locale("en", "US"));
        ycount=3;
        mult=1;
        costmult=1000;
        dore=0.1;
        dwei=0.1;
        //String [] test = {"carrier\\new\\271_4","carrier\\new\\3800_4"};
        //String [] test = {/*"carrier\\new\\271_4","carrier\\new\\3800_4"/*,*/"carrier\\new\\12000_4"/*,"carrier\\new\\28000_4"/*,"carrier\\new\\82000"*/};
        //String [] test = {"carrier\\2000"};
        //String [] test = {"carrier\\new\\271_4"};
        //String [] test = {"carrier\\new\\3800_4"};
        //String [] test = {"carrier\\new\\12000_4"};
        //String [] test = {"carrier\\new\\28000_4"};
        //String [] test = {"carrier\\ideal\\4000"};
        //String [] test = {"carrier\\ideal\\35000"};
        //String [] test = {"carrier\\ideal\\535000"};
        /*dore=0;
        dwei=0.9;
        costmult=100;
        String [] test = {"carrier\\test\\38"};*/
        //String [] test = {"carrier\\test\\64"};
        String [] test = {"carrier\\test\\216"};
        //String [] test = {"carrier\\test\\88_new"};
        testcars(test,true/*opt*/, 14*60*60*1000,false/*first*/,false/*local*/,true/*withorepart*/);
        //dore=0.3;
        //dwei=0.6;
        //testcars(test,true/*opt*/, 12*60*60*1000,false /*first*/,false/*local*/,true/*withorepart*/);
        solved=true;
        simplevis=false;
        //showpit(test[0]+"_years("+ycount+")_dore("+dore+")_dwei("+dwei+")_optimized_solution",false);
        //showpit("carrier\\ideal\\4000_years(3)_dore(0.1)_dwei(0.5)_optimized__2022.01.31-12.33.07 solution",false);
        //showpit("carrier\\new\\271_4_years(3)_dore(0.1)_dwei(0.2)_optimized__2022.01.31-01.15.34 solution",false);
        
        
        //showpit("carrier\\new\\271_4_years(3)_dore(0.1)_dwei(0.1)_optimized_solution",false);
        //showpit("carrier\\test\\64_years(4)_dore(0.1)_dwei(0.1)_optimized__2022.02.03-07.38.24 solution",false);
        //showpit("carrier\\test\\88_1",false);
        //showpit("carrier\\test\\88_new_years(3)_dore(0.1)_dwei(0.1)_optimized__2022.02.03-05.24.37 solution",false);
        //showpit("carrier\\new\\12000_4_years(3)_dore(0.4)_dwei(0.4)_optimized_solution",false);
        //showpit("carrier\\new\\28000_4_years(3)_dore(0.4)_dwei(0.4)_optimized_solution",false);
        
        /*solved=false;
        simplevis=false;
        showpit(test[0],true);*/


    }
    
    static void testcars(String [] cars,boolean withopt,long timer, boolean first, boolean local,boolean withore) throws IOException, InvalidFormatException{
        solvepit(cars[0], false, 2000, true, local,withore);
        for(String tst:cars){
            System.out.println("Testing: "+tst);
            testmax(tst,withopt,timer, first,local,withore);
        }
    }
    
    
    static void testmax(String in,boolean withopt,long timer, boolean first, boolean local, boolean withore)throws IOException, InvalidFormatException{
        Excbuilder ex=new Excbuilder(in,ycount,dore,dwei,mult,costmult, withore,solved);
        Date dat=new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd'-'hh.mm.ss");
        excel.Logger logmax = new excel.Logger(in+"("+dwei+"_"+dore+")_"+formatForDateNow.format(dat)+".txt");
        //excel.Logger log = new excel.Logger(in+"testlog_optimized("+withopt+")_local("+local+").txt");
        CC=new ChocoCarrier();
        CC.buildTask(ex,withopt,logmax);
        CC.solve(logmax, in, timer,first,local);
        
        /*CC=new ChocoCarrier();
        CC.buildTask(ex,false,withopt,log);
        CC.solve(log, in, timer,first,local);*/
    
    }
    static void solvepit(String in, boolean withopt, long timer, boolean first,boolean local,boolean withore) throws IOException, InvalidFormatException{
        Excbuilder ex=new Excbuilder(in,ycount,dore,dwei,mult,costmult,withore,solved);
        excel.Logger logge = new excel.Logger("d:\\work\\carriertestlog.txt");
        /*Logger logsort = new Logger("d:\\work\\carrier\\sortlog.txt");*/
        CC=new ChocoCarrier();
        CC.buildTask(ex,withopt,logge);
        CC.solve(logge, in, timer,first,local);
    
    
    
    }
    

    @Override
    public void simpleInitApp(){
        System.out.println("Initiation");
        CV = new CarrierVisualisation(CC,rootNode,assetManager);
        CV.build(solved, simplevis);
        //for(ChocoCarCube ccc:CC.ecar.car.cubes)
            //CV.all.attachChild(ccc.CV.nd);
        initKeys();
        BitmapText controls = new BitmapText(guiFont, false);
        controls.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        controls.setColor(ColorRGBA.Blue);                             // font color
        controls.setText("Controls:\n"
                + "show/hide cube: B\n"
                + "move to up cube: U\n"
                + "move to down cube: O\n"
                + "move to left cube: J\n"
                + "move to right cube: L\n"
                + "move to forward cube: K\n"
                + "move to back cube: I\n"
                + "show all pit transparent: V\n"
                + "show specific year: 1,2,3..\n"
                + "change background color (black/white): SPACE");             // the text
        controls.setLocalTranslation(0, 700, 0); // position
        guiNode.attachChild(controls);

        viewPort.setBackgroundColor(ColorRGBA.White);
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.Blue);                             // font color
        hudText.setText("You can write \n any string here \n dgdfgdgdg\n dfgdgdgdgd\n");             // the text
        hudText.setLocalTranslation(0, 400, 0); // position
        guiNode.attachChild(hudText);
        vis=CC.ecar.car.cubes.get(0);
        CV.all.setCullHint(CullHint.Always);
        vis.CV.showNode();
        //cam.setLocation(new Vector3f((float)vis.X,(float)vis.Z,(float)vis.Y));
        cam.setLocation(new Vector3f(0,0,0));
        flyCam.setMoveSpeed(500);
        cam.setFrustumFar(100000);
        cam.onFrameChange();
        cam.update();

        
    }

    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Year1",  new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Year2",  new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Year3",  new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Year4",  new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Year5",  new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("Year6",  new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("Year7",  new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("Year8",  new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("Year9",  new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("mvleft",  new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("mvright",  new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("mvback",  new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("mvforw",  new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("mvup",  new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("mvdown",  new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("ChangeColor",  new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Visible",  new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("VisibleCube",  new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping("TEST",  new KeyTrigger(KeyInput.KEY_M));
        // Add the names to the action listener.
        inputManager.addListener(actionListener, "Year1","Year2","Year3","Year4","Year5","Year6",
                "Year7","Year8","Year9","ChangeColor","Visible","mvleft","mvright","mvback","mvforw","mvup","mvdown","VisibleCube","TEST");

    }
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("ChangeColor") && !keyPressed){
                if(viewPort.getBackgroundColor().equals(ColorRGBA.Black))viewPort.setBackgroundColor(ColorRGBA.White);
                else viewPort.setBackgroundColor(ColorRGBA.Black);
                return;
            }
            if (name.equals("Visible") && !keyPressed){
                if(CV.all.getCullHint()!=CullHint.Always)CV.all.setCullHint(CullHint.Always);
                else CV.all.setCullHint(CullHint.Dynamic);
                return;
            }
            if (name.equals("VisibleCube") &&!simplevis&& !keyPressed){
                if(vis.CV.dvis.nd.getCullHint()!=CullHint.Always)vis.CV.hideNode();
                else vis.CV.showNode();
                return;
            }
    
            if (name.equals("mvleft") && !keyPressed &&vis.getleft()!=null){
                vis.CV.hideNode();
                vis=(ChocoCarCube)vis.getleft();
                vis.CV.showNode();
                hudText.setText(vis.X+":"+vis.Z+":"+vis.Y+"\n");
                return;
            }
            if (name.equals("mvright") && !keyPressed &&vis.getright()!=null){
                vis.CV.hideNode();
                vis=(ChocoCarCube)vis.getright();
                vis.CV.showNode();
                hudText.setText(vis.X+":"+vis.Z+":"+vis.Y+"\n");
                return;
            }
            
            
            if (name.equals("mvback") && !keyPressed &&vis.getback()!=null){
                vis.CV.hideNode();
                vis=(ChocoCarCube)vis.getback();
                vis.CV.showNode();
                hudText.setText(vis.X+":"+vis.Z+":"+vis.Y+"\n");
                return;
            }
            if (name.equals("mvforw") && !keyPressed &&vis.getforw()!=null){
                vis.CV.hideNode();
                vis=(ChocoCarCube)vis.getforw();
                vis.CV.showNode();
                hudText.setText(vis.X+":"+vis.Z+":"+vis.Y+"\n");
                return;
            }
            if (name.equals("mvup") && !keyPressed &&vis.getup()!=null){
                vis.CV.hideNode();
                vis=(ChocoCarCube)vis.getup();
                vis.CV.showNode();
                hudText.setText(vis.X+":"+vis.Z+":"+vis.Y+"\n");
                return;
            }
            if (name.equals("mvdown") && !keyPressed &&vis.getdown()!=null){
                vis.CV.hideNode();
                vis=(ChocoCarCube)vis.getdown();
                vis.CV.showNode();
                hudText.setText(vis.X+":"+vis.Z+":"+vis.Y+"\n");
                return;
            }
            if (name.equals("TEST") && !keyPressed){
                hudText.setText(vis.X+":"+vis.Z+":"+vis.Y+"\n");
                return;
            }

            for(int i=1; i<=ycount; i++)
                if (name.equals("Year"+i) && !keyPressed) {
                    if(CV.years[i-1].getCullHint()!=CullHint.Always)CV.years[i-1].setCullHint(CullHint.Always);
                    else CV.years[i-1].setCullHint(CullHint.Dynamic);
                    hudText.setText("Year "+i+" is pressed");
                    return;
                }
        }
    };
    

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
