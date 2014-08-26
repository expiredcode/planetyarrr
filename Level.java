package com.expiredcode.ld.planetyarrr;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.graphics.GL10;


public class Level extends GameScreen
{
	int WORLD_WIDTH = 1280, WORLD_HEIGHT = 720;
	//int WORLD_WIDTH = 800, WORLD_HEIGHT = 480;
	
	boolean done = false;
	boolean mobile = true;
	
	MyUtils m = new MyUtils();
	
	SpriteBatch batch;
	OrthographicCamera camera;
	
	Sprite a,b,l,p; 
	
	Texture ship,laser1,laser2,sfondo,loot,boom,p1,p2,p3,p4,p5,pyarrr,su,sx,dx,fu; 
	Collider mLeft,mRight,fireUp,mForw,touch;
	Vector2 playerShip,ml,mr,fi,mf;	
	
	PlayerShip pship;
	PoliceShip fiveoh,r;
	CargoShip cargo;
	
	World w;
	
	Music song;
	Sound pew;
	
	float angolo=0;
	float speed=125;
	float dist;
	
	Iterator it,t;
	Collider c,sc,ec,py;
	
	Init i = new Init("first");
	
	BitmapFont font;
	
	Vector2 touchpos;
	

	Level()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		
		batch = new SpriteBatch();
		
		ship = new Texture(Gdx.files.internal("data/ship_2.png")); //player ship
		playerShip = new Vector2(200,100);
		a = new Sprite(ship,0, 0, 21, 23);		
		a.setPosition(playerShip.x, playerShip.y);		
		a.setOrigin(21/2, 23/2);				
		pship = new PlayerShip(a,angolo,speed,190);
				
		laser1 = new Texture(Gdx.files.internal("data/laser.png")); //laser
		l = new Sprite(laser1,0,0,1,7);
		l.setOrigin(1/2, 7/2);
		laser2 = new Texture(Gdx.files.internal("data/laser2.png"));
		p = new Sprite(laser2,0,0,1,7);
		p.setOrigin(1/2, 7/2);
		
		sfondo = new Texture(Gdx.files.internal("data/sfondo.png"));		
		
		loot = new Texture(Gdx.files.internal("data/loot.png"));
		
		boom = new Texture(Gdx.files.internal("data/boom.png"));
		
		p1 = new Texture(Gdx.files.internal("data/p1.png"));
		p2 = new Texture(Gdx.files.internal("data/p2.png"));
		p3 = new Texture(Gdx.files.internal("data/p3.png"));
		p4 = new Texture(Gdx.files.internal("data/p4.png"));
		p5 = new Texture(Gdx.files.internal("data/p5.png"));
		pyarrr = new Texture(Gdx.files.internal("data/pyarrr.png"));
		
		m.world.add(new World(p1,1340,-130,546,551,1));  
		m.world.add(new World(p2,2936,2688,137*1.5f,137*1.5f,2));
		m.world.add(new World(p3,3440,820,603,603,3));
		m.world.add(new World(p4,0,770,473,473,4));
		m.world.add(new World(p5,0,2300,343,343,5));
		//m.world.add(new World(pyarrr,1352,1700,289,289));
		py = new Collider("pyarr",1352,1700,289,289);
		
		pship.ship.setPosition(py.x, py.y);
		
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.scale(1.5f);
		
		song = Gdx.audio.newMusic(Gdx.files.internal("data/song.mp3"));
		song.setLooping(true);		
		song.setVolume(0.7f);			
		song.play();
		
		pew = Gdx.audio.newSound(Gdx.files.internal("data/laser.ogg"));		
		
		su = new Texture(Gdx.files.internal("data/g.png"));
		mForw = new Collider("su",1100,50,70,70);
		mf = new Vector2(1100,50);
		sx = new Texture(Gdx.files.internal("data/l.png")); 
		mLeft = new Collider("sx",70,50,70,70);
		ml = new Vector2(70,50);
		dx = new Texture(Gdx.files.internal("data/r.png")); 
		mRight = new Collider("sx",160,50,70,70);
		mr = new Vector2(160,50);
		fu = new Texture(Gdx.files.internal("data/f.png"));
		fireUp = new Collider("sx",1000,50,70,70);		
		fi = new Vector2(1000,50);
		
		touchpos = new Vector2();
		
	}
	
	
	@Override
	public void render(float deltaTime) 
	{
		updateScene(deltaTime);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(pship.ship.getX(),pship.ship.getY(),0);
		
		camera.viewportHeight = this.WORLD_HEIGHT;
		camera.viewportWidth = this.WORLD_WIDTH; 
		//camera.zoom = 2f;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		batch.draw(sfondo, -3500, -3500);
				
				
		batch.draw(pyarrr, py.x, py.y);
		
		font.draw(batch, "Money stolen: "+pship.money, py.x, py.y+350);
		font.draw(batch, "Healt "+pship.healt,pship.ship.getX()-280,pship.ship.getY()-280);
		
		it = m.police.iterator();		//nemici draw + update
		while(it.hasNext())
		{
			r = (PoliceShip)it.next();
			r.draw(batch);
			r.checkpersue(pship.ship.getX(), pship.ship.getY());
			r.update(deltaTime);
		}
		it = m.cargo.iterator();     //  cargo draw
		while(it.hasNext())
		{
			cargo = (CargoShip)it.next();
			cargo.draw(batch);			
			cargo.update(deltaTime);
		//	Gdx.app.log("cargo", "draw"+cargo.ship.getX()+" "+cargo.ship.getY());
		}
		
		it = m.loot.iterator();     //  loot draw
		while(it.hasNext())
		{
			ec = (Collider)it.next();
			batch.draw(loot, ec.x, ec.y);
		}
		
		it = m.boom.iterator();     //  boom draw
		while(it.hasNext())
		{
			ec = (Collider)it.next();
			batch.draw(boom, ec.x, ec.y);
			ec.time-=deltaTime;
			if(ec.time<0) it.remove();
		}
		
		it = m.world.iterator();     //  world draw + update
		while(it.hasNext())
		{
			w = (World)it.next();
			batch.draw(w.t, w.c.x, w.c.y);
			w.update(deltaTime);
		}
		
		
		it = m.pfire.iterator();		//proiettili player vs nemici draw + update
		while(it.hasNext())
		{
			sc = (Collider) it.next();
			l.setPosition(sc.x, sc.y);			
			l.rotate(sc.angle-90);
			l.draw(batch);
			l.rotate(-(sc.angle-90));
			sc.x += (300*deltaTime)*Math.cos(sc.angle*MathUtils.degreesToRadians);
			sc.y += (300*deltaTime)*Math.sin(sc.angle*MathUtils.degreesToRadians);
			if(sc.x<-80000 || sc.x>WORLD_WIDTH+80000 || sc.y<0-80000 || sc.y>WORLD_HEIGHT+80000)
				it.remove();
			
		//	Gdx.app.log("l", "x:"+sc.x+"  y:"+sc.y);
		}
		it = m.enfire.iterator();		//proiettili nemici vs player draw + update
		while(it.hasNext())
		{
			sc = (Collider) it.next();
			p.setPosition(sc.x, sc.y);			
			p.rotate(sc.angle-90);
			p.draw(batch);
			p.rotate(-(sc.angle-90));
			sc.x += (100*deltaTime)*Math.cos(sc.angle*MathUtils.degreesToRadians);
			sc.y += (100*deltaTime)*Math.sin(sc.angle*MathUtils.degreesToRadians);
			if(sc.x<-80000 || sc.x>WORLD_WIDTH+80000 || sc.y<0-80000 || sc.y>WORLD_HEIGHT+80000)
				it.remove();
			
		//	Gdx.app.log("l", "x:"+sc.x+"  y:"+sc.y);
		}
		pship.draw(batch);
		if(mobile){
		batch.draw(sx,(camera.position.x-WORLD_WIDTH/2)+mLeft.x,(camera.position.y-WORLD_HEIGHT/2)+mLeft.y);
		batch.draw(dx,(camera.position.x-WORLD_WIDTH/2)+mRight.x,(camera.position.y-WORLD_HEIGHT/2)+mRight.y);
		batch.draw(su,(camera.position.x-WORLD_WIDTH/2)+mForw.x,(camera.position.y-WORLD_HEIGHT/2)+mForw.y);
		batch.draw(fu,(camera.position.x-WORLD_WIDTH/2)+fireUp.x,(camera.position.y-WORLD_HEIGHT/2)+fireUp.y);				
		}
		
		batch.end();
	}
	
	void updateScene(float deltaTime)
	{
		if(mobile){
		if(Gdx.input.isTouched())	
		{
			touchpos.x = Gdx.input.getX();	touchpos.y = WORLD_HEIGHT-Gdx.input.getY()-243;
			//angolo = touchpos.sub(a.getX(), a.getY()).angle();			
			touch = new Collider("touch",touchpos.x,touchpos.y,70,70);
			
			//Gdx.app.log("a", "x"+touchpos.x+" y"+touchpos.y);
			
			if(touchpos.x>46&&touchpos.x<86&&touchpos.y>36&&touchpos.y<78)//Box2BoxCollision.B2BCollision(touch,mLeft))
			{
				pship.rotateLeft(deltaTime);
			}
			if(touchpos.x>103&&touchpos.x<136&&touchpos.y>36&&touchpos.y<78)//Box2BoxCollision.B2BCollision(touch,mRight))
			{
				pship.rotateRight(deltaTime);
			}
			if(touchpos.x>688&&touchpos.x<730&&touchpos.y>36&&touchpos.y<78)//Box2BoxCollision.B2BCollision(touch,mForw))
			{
				pship.moveForward();
			}
			if(touchpos.x>626&&touchpos.x<668&&touchpos.y>36&&touchpos.y<78)//Box2BoxCollision.B2BCollision(touch,fireUp))
			{
				if(pship.fire(deltaTime))
					pew.play(0.5f);
			}
		}
		}else{
		if(Gdx.input.isKeyPressed(Keys.A))
		{
			pship.rotateLeft(deltaTime);
		}
		if(Gdx.input.isKeyPressed(Keys.D))
		{
			pship.rotateRight(deltaTime);
		}
		if(Gdx.input.isKeyPressed(Keys.W))      
		{
			pship.moveForward();
		}
		if(Gdx.input.isKeyPressed(Keys.SPACE))      
		{
			//shoot
			if(pship.fire(deltaTime))
				pew.play();
		}
		}
		
		pship.update(deltaTime);		
		
		collisions();		
	}
	
	void collisions()
	{
		it = m.pfire.iterator();		//proiettili player vs nemici 
		while(it.hasNext())
		{
			sc = (Collider) it.next();
			
			t = m.police.iterator();
			while(t.hasNext())
			{
				r = (PoliceShip) t.next();
				ec = new Collider("50",r.ship.getX(),r.ship.getY(),r.ship.getWidth(),r.ship.getHeight());
				if (Box2BoxCollision.B2BCollision(sc,ec))
				{
					it.remove();				
					r.healt-=(int)(Math.random()*50);
					if(r.healt<=0)
					{
						t.remove();
						ec.time = 0.5f;
						m.boom.add(ec);
					}
				}
			}
		}
		
		it = m.pfire.iterator();		//proiettili player vs cargo 
		while(it.hasNext())
		{
			sc = (Collider) it.next();
			
			t = m.cargo.iterator();
			while(t.hasNext())
			{
				cargo = (CargoShip) t.next();
				ec = new Collider("50",cargo.ship.getX(),cargo.ship.getY(),cargo.ship.getWidth(),cargo.ship.getHeight());
				if (Box2BoxCollision.B2BCollision(sc,ec))
				{
					it.remove();				
					cargo.healt-=(int)(Math.random()*50);
					if(cargo.healt<=0)
					{
						ec.time = 0.5f;
						m.boom.add(ec);
						m.loot.add(ec);
						t.remove();						
					}
				}
			}
		}
		
		it = m.enfire.iterator();   	//proiettili nemici vs player  
		while(it.hasNext())
		{
			sc = (Collider) it.next();
			
			ec = new Collider("pship",pship.ship.getX(),pship.ship.getY(),pship.ship.getWidth(),pship.ship.getHeight());
			if (Box2BoxCollision.B2BCollision(sc,ec))
			{
				it.remove();				
				pship.healt-=(int)(Math.random()*10);
				if(pship.healt<=0)
				{
					m.screen = 2;
					song.stop();
					done = true;
				}
				//	Gdx.app.log("game", "over");
			}
		}
		
		it = m.loot.iterator();   	//loot vs player  
		while(it.hasNext())
		{
			sc = (Collider) it.next();
			
			ec = new Collider("pship",pship.ship.getX(),pship.ship.getY(),pship.ship.getWidth(),pship.ship.getHeight());
			if (Box2BoxCollision.B2BCollision(sc,ec))
			{
				it.remove();				
				pship.money+=(int)(Math.random()*100);
				//Gdx.app.log("m", ""+pship.money);
			}
		}
		
	
			t = m.cargo.iterator();			//cargo to destination
			while(t.hasNext())
			{
				cargo = (CargoShip) t.next();
				
				dist=((cargo.goal.x-cargo.ship.getX())*(cargo.goal.x-cargo.ship.getX()))+((cargo.goal.y-cargo.ship.getY())*(cargo.goal.y-cargo.ship.getY()));
				//Gdx.app.log("cargo", "dist"+dist);
				if(dist<500*500)
				{
					t.remove();
				//	Gdx.app.log("cargo", "arrived");
				}
			}
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return done;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		ship.dispose();
		laser1.dispose();
		laser2.dispose();
		sfondo.dispose();
		loot.dispose();
		boom.dispose();
		p1.dispose();
		p2.dispose();
		p3.dispose();
		p4.dispose();
		p5.dispose();
		pyarrr.dispose(); 
		song.dispose();
		pew.dispose();
		batch.dispose();		
	}

}
