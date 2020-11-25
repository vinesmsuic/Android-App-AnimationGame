package hk.polyu.eie.eie3109.asmanimationgame;

import android.graphics.Bitmap;

public class GraphicObject {

    private Bitmap bitmap;
    private Coordinates coordinates;
    private Movement movement;

    public GraphicObject(Bitmap bitmap){
        this.bitmap = bitmap;
        this.coordinates = new Coordinates(bitmap);
        this.movement = new Movement();
    }

    public Bitmap getGraphic(){
        return bitmap;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public Movement getMovement(){
        return movement;
    }

    public void setGraphic(Bitmap bitmap) { this.bitmap = bitmap;}
}
