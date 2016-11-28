/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteriods.elements;

import asteriods.rockengine.LineEq;
import asteriods.rockengine.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author rafael
 */
public class Asteriod extends Element{
   
    private final double TIME_FRAME = 0.1;
    
    private Double speed;
    private Point speedVector;
    private Point origin;
    private Point endPoint;
    private Point currentPosition;
    
    public Asteriod (){
        this.getPoints().addAll(new Double []{
            -8.0, 0.0,
            -4.0, 4.0,
             0.0, 8.0,
             4.0, 4.0,
             8.0, 0.0,
             4.0,-4.0,
             0.0,-8.0,
            -4.0,-4.0
        });
    }

    public void setRandomPath(){
        int direction = ThreadLocalRandom.current().nextInt(1, 4);
        Point [] pathPoints = new Point [2];
        switch (direction){
            case 1:
                pathPoints = setPathFromTopOrigin();
                break;
            case 2:
                pathPoints = setPathFromRightOrigin();
                break;
            case 3:
                pathPoints = setPathFromBottomOrigin();
                break;
            case 4:
                pathPoints = setPathFromLeftOrigin();
                break;
        }
        
        setOrigin(pathPoints[0]);
        setEndPoint (pathPoints[1]);
        setCurrentPosition(pathPoints[0]);
        moveFromOrigin();
        int speed = ThreadLocalRandom.current().nextInt(1, 32);
        setSpeed((double)speed);
        calculateSpeedVector();
    }
    
    protected Point [] setPathFromTopOrigin (){
        int x = ThreadLocalRandom.current().nextInt(1,599);
        Point origin = new Point (x, 0);
        
        x = ThreadLocalRandom.current().nextInt(1,599);
        Point end = new Point (x, 600);
        return new Point [] {origin, end};
    }
    
    protected Point [] setPathFromRightOrigin (){
        int y = ThreadLocalRandom.current().nextInt(1,599);
        Point origin = new Point (600, y);
        
        y = ThreadLocalRandom.current().nextInt(1,599);
        Point end = new Point (0, y);
        return new Point [] {origin, end};
    }
    
    protected Point [] setPathFromBottomOrigin (){
        int x = ThreadLocalRandom.current().nextInt(1,599);
        Point origin = new Point (x, 600);
        
        x = ThreadLocalRandom.current().nextInt(1,599);
        Point end = new Point (x, 0);
        return new Point [] {origin, end};
    }
    
    protected Point [] setPathFromLeftOrigin (){
        int y = ThreadLocalRandom.current().nextInt(1,599);
        Point origin = new Point (0, y);
        
        y = ThreadLocalRandom.current().nextInt(1,599);
        Point end = new Point (600, y);
        return new Point [] {origin, end};
    }
    
    protected void calculateSpeedVector(){
        List<Point> points = new ArrayList<>();
        points.add(getEndPoint());
        List<Point> endPointList = getOrigin().changeOrigin(points);
        Point pathVector = endPointList.get(0);
        double angle = Math.atan2(pathVector.getY(),pathVector.getX());
        
        double xSpeed = speed*Math.cos(angle);
        double ySpeed = speed*Math.sin(angle);
        speedVector = new Point (xSpeed, ySpeed);
    }
    
    public Point getNextPoint(){
        double nx = (speedVector.getX()*TIME_FRAME) + currentPosition.getX();
        double ny = (speedVector.getY()*TIME_FRAME) + currentPosition.getY();
        
        return new Point(nx, ny);
    }
    
    public void updateAsteriodPosition (Point nextPoint){
        double deltaX = nextPoint.getX() - currentPosition.getX();
        double deltaY = nextPoint.getY() - currentPosition.getY();
        currentPosition = nextPoint;
        move(deltaX, deltaY);
    }
    
    protected Point getCentroid(){
        List <Point> points = Point.buildList(getPoints());
        points.add(points.get(0));
        
        double A = 0;
        double Cx = 0;
        double Cy = 0;
        
        for (int i=0; i<points.size()-1; i++){
            Point pi = points.get(i);
            Point pj = points.get(i+1);
            double secondFactor = (pi.getX()*pj.getY()) - (pj.getX()*pi.getY());
            double numX = (pi.getX() + pj.getX())*secondFactor;
            double numY = (pi.getY() + pj.getY())*secondFactor;
            
            Cx += numX;
            Cy += numY;
            A += secondFactor;
        }
        A *= 0.5;
        Cx *= 1/(6*A);
        Cy *= 1/(6*A);
        return new Point (Cx, Cy);
    }
    
    public void moveFromOrigin(){
        Point centroid = getCentroid();
        double deltaX = this.currentPosition.getX() - centroid.getX();
        double deltaY = this.currentPosition.getY() - centroid.getY();
        move (deltaX, deltaY);
    }
    
    private void move (double deltaX, double deltaY){
        List<Point> asteriodPoints = Point.buildList(getPoints());
        
        for (int i=0; i<asteriodPoints.size(); i++){
            Point p = asteriodPoints.get(i);
            p.setX(p.getX() + deltaX);
            p.setY(p.getY() + deltaY);
        }
        
        getPoints().removeAll(getPoints());
        getPoints().addAll(Point.toDoubleList(asteriodPoints));
    }
    
    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }
    
    public Point getOrigin() {
        return origin;
    }

    public Point getSpeedVector() {
        return speedVector;
    }
    
    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }
}
