package com.elsys;

import java.lang.Math;

public class Vector2D {
    public double x, y;

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D a)
    {
        this.x += a.x;
        this.y += a.y;
        return this;
    }

    public Vector2D subtract(Vector2D a)
    {
        this.x -= a.x;
        this.y -= a.y;
        return this;
    }

    public Vector2D(Vector2D a)
    {
        this.x = a.x;
        this.y = a.y;
    }

    public double dot(Vector2D a){
        return x * a.x + y * a.y;
    }

    public Vector2D multiply(double a)
    {
        this.x *= a;
        this.y *= a;
        return this;
    }

    public double distance(Vector2D a) { return Math.sqrt(Math.abs(this.x - a.x) * Math.abs(this.x - a.x) + Math.abs(this.y - a.y) * Math.abs(this.y - a.y)); }

    public Vector2D normalize()
    {
        double length = this.length();

        if(length != 0.0d)
        {
            this.x = x / length;
            this.y = y / length;
        }
        else
        {
            this.x = 0.0d;
            this.y = 0.0d;
        }

        return this;
    }

    public double length(){
        return Math.sqrt(this.dot(this));
    }
    public Vector2D plus(Vector2D a){
        return new Vector2D(x + a.x, y + a.y);
    }
    public Vector2D minus(Vector2D a)
    {
        return new Vector2D(x - a.x, y - a.y);
    }
    public Vector2D times(double a){
        return new Vector2D(x * a, y * a);
    }

}
