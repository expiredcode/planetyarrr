package com.expiredcode.ld.planetyarrr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Init 
{
	MyUtils m = new MyUtils();
	Texture fiveoh, cargoh, world;
	Sprite fiv,car,wor;
	CargoShip cargo;
	PoliceShip police;
	
	Init()
	{
		
	}
	
	Init(String str)
	{
		/*initFiveO(100,200);
		initFiveO(200,300);
		initFiveO(120,50);
		
		initCargo(200,100,45);
		initCargo(200,100,90);
		initCargo(200,100,45+90);
		initCargo(200,100,180);
		initCargo(200,100,180+45);
		initCargo(200,100,270);
		initCargo(200,100,270+45);
		initCargo(200,100,0);*/		
	}
	
	public void initWorld(float x,float y)
	{
		
	}
	
	public void initCargo(float x,float y,float angle,Vector2 goal)
	{
		cargoh = new Texture(Gdx.files.internal("data/cargo.png"));
		car = new Sprite(cargoh,0, 0, 20, 42);
		car.setPosition(x, y);
		car.setOrigin(20/2, 42/2);
		cargo = new CargoShip(car,0,50);
		cargo.angle = angle;
		cargo.goal = goal;
		m.cargo.add(cargo);
	}
	
	public void initFiveO(float x,float y)
	{
		fiveoh = new Texture(Gdx.files.internal("data/police.png"));
		fiv = new Sprite(fiveoh,0, 0, 26, 32);
		fiv.setPosition(x, y);
		fiv.setOrigin(26/2, 32/2);
		police = new PoliceShip(fiv,0,70);  //(sprite,angle,speed)
	//	police.setAngle(angle);
	//	police.speed=speed;
		m.police.add(police);
	}
	public void initFiveO(float x,float y,float angle)
	{
		fiveoh = new Texture(Gdx.files.internal("data/police.png"));
		fiv = new Sprite(fiveoh,0, 0, 26, 32);
		fiv.setPosition(x, y);
		fiv.setOrigin(26/2, 32/2);
		police = new PoliceShip(fiv,0,50,true);  //(sprite,angle,speed)
		police.setAngle(angle);
	//	police.speed=speed;
		m.police.add(police);
	}

}
