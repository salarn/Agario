package Shapes;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class Bubbles {

	private List<Bubble> bubLst = new ArrayList<>();
	private Point limits = null;

	private boolean gearPassed = false;
	

	//Constructors 

	public Bubbles ( Bubble bub ) {
		bubLst.add(bub);
	}

	public Bubbles ( List<Bubble> bubLst ){
		this.bubLst.addAll(bubLst);
	}


	/// getter and setter 

	public List<Bubble> getBubLst() {
		return bubLst;
	}

	public void setBubLst(List<Bubble> lst) {
		this.bubLst = lst;
	}

	public boolean isGearPassed() {
		return gearPassed;
	}

	public void setGearPassed(boolean gearPassed) {
		this.gearPassed = gearPassed;
	}




	// other Method


	public Point getCenter () {
		//TODO
		Point center;
		for ( Bubble bub : bubLst ){
			center = bub.getCenter();
			return new Point( center.getX() - bub.getRadius()  ,center.getY());
		}
		return new Point();
	}

	public void normalize () {
		List<Bubble> removeLst = new ArrayList<>();
		Bubble prevBub = null;
		for ( Bubble bub : bubLst ){
			if ( bub.isDead() ){
				removeLst.add(bub);
				continue;
			}
			if ( prevBub != null ){
				if ( bub.getCenter().getX() - prevBub.getCenter().getX() < bub.getRadius() + prevBub.getRadius() + 2*Circle.borderLen ){
					bub.getCenter().setX( prevBub.getCenter().getX() + 
							bub.getRadius() + prevBub.getRadius() + 2*Circle.borderLen );
				}
			}
			prevBub = bub;
		}

		for ( Bubble bub : removeLst)
			if ( bubLst.contains(bub) )
				bubLst.remove(bub);
		
		if ( limits == null )
			return;
		
		// checking Ys
		Bubble mxRadBub = null;
		for ( Bubble bub : bubLst ){
			if ( mxRadBub == null )
				mxRadBub = bub;
			else if ( mxRadBub.getRadius() < bub.getRadius() )
				mxRadBub = bub;
		}
		if ( mxRadBub.getCenter().getY() + mxRadBub.getRadius() > limits.getY() ){
			double y = limits.getY() - mxRadBub.getRadius();
			for ( Bubble bub : bubLst )
				bub.getCenter().setY(y);
		}
		if ( mxRadBub.getCenter().getY() - mxRadBub.getRadius() < 0 ){
			double y = 0 + mxRadBub.getRadius();
			for ( Bubble bub : bubLst )
				bub.getCenter().setY(y);
		}
		
		
		// checking Xs
		Bubble lftBub = bubLst.get(0);
		if ( lftBub.getCenter().getX() - lftBub.getRadius() < 0 ){
			lftBub.getCenter().setX(lftBub.getRadius() + 0);
			for ( int i = 1 ; i < bubLst.size(); ++i ){
				Bubble bub = bubLst.get(i), pbub = bubLst.get(i-1);
				if ( bub.getCenter().getX() - pbub.getCenter().getX() < bub.getRadius() + pbub.getRadius() + 2*Circle.borderLen )
					bub.getCenter().setX(pbub.getCenter().getX() + pbub.getRadius() + bub.getRadius() + 2*Circle.borderLen);
			}
		}
		
		if ( bubLst.size() < 1 )
			return;
		Bubble rigBub = bubLst.get(bubLst.size() - 1);
		if ( rigBub.getCenter().getX() + rigBub.getRadius() > limits.getX() ){
			rigBub.getCenter().setX(limits.getX() - rigBub.getRadius());
			for ( int i = bubLst.size() - 2 ; i >= 0 ; --i ){
				Bubble bub = bubLst.get(i), pbub = bubLst.get(i+1);
				if ( pbub.getCenter().getX() - bub.getCenter().getX() < bub.getRadius() + pbub.getRadius() + 2*Circle.borderLen )
					bub.getCenter().setX(pbub.getCenter().getX() - pbub.getRadius() - bub.getRadius() - 2*Circle.borderLen); 
			}
		}
		
		
		
		
	}

	public void move ( Point pnt, double speed, Point limits ){
		//TODO

		this.limits = limits;
		for ( Bubble bub : bubLst ){
			bub.move(pnt, speed);
		}

		normalize();

	}

	public void split () {
		List<Bubble> lst = new ArrayList<>();
		for ( Bubble bub : bubLst ){
			if ( bub.getRadius() < Bubble.getMinRadConst() * 2 ) { // important
				lst.add(bub);
				continue;
			}
			Point pnt = bub.getCenter().getAddition(new Point(bub.getRadius(), 0));
			double rad = Math.sqrt(bub.getArea()/(Math.PI*2.0));
			Bubble bub1 = new Bubble ( pnt.getAddition(new Point(-(rad+Circle.borderLen),0)), rad, bub );
			Bubble bub2 = new Bubble ( pnt.getAddition(new Point(+(rad+Circle.borderLen),0)), rad, bub );

			lst.add(bub1);
			lst.add(bub2);

		}
		bubLst = lst;
		gearPassed = false;
		normalize();
	}

	public void join () {
		int t = ( gearPassed ? 4000 : 2000 );
		Timer timer = new Timer(t,null);
		timer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( bubLst.size() == 1 ){
					timer.stop();
					return;
				}
				normalize();
				binaryJoin();
			}
		});
		timer.start();
	}

	public void binaryJoin () {
		List<Bubble> lst = new ArrayList<>();
		Bubble prevBub = null;
		for ( Bubble bub : bubLst ){
			if ( prevBub == null ){
				prevBub = bub;
				continue;
			}
			Bubble newBub = new Bubble ( prevBub.getCenter(), Math.sqrt( (bub.getArea()+prevBub.getArea())/Math.PI ),
					prevBub);
			lst.add(newBub);
			prevBub = null;
		}
		if ( prevBub != null )
			lst.add(prevBub);
		bubLst = lst;
	}

	public void fastJoin () {
		if ( bubLst.size() == 1 )
			return;
		Bubble mainBub = null;
		for ( Bubble bub : bubLst ){
			if ( mainBub == null ) {
				mainBub = bub;
				continue;
			}
			Bubble newBub = new Bubble ( mainBub.getCenter(), Math.sqrt(((mainBub.getArea() + bub.getArea())/Math.PI)),
					mainBub);
			mainBub = newBub;
		}
		bubLst = new ArrayList<>();
		bubLst.add(mainBub);
	}

	public void halfingAll () {
		for ( Bubble bub : bubLst ){
			double newArea = bub.getArea() / 2;
			double newRad  = Math.sqrt( newArea/Math.PI );
			bub.setRadius(newRad);
		}
		normalize();
	}

	public void removeBubble (Bubble bub ){
		if ( bubLst.contains(bub) == false )
			return ;
		bubLst.remove(bub);
	}

	public void gearPassing ( Bubble bub ) {
		if ( bubLst.contains(bub) == false )
			return;
		gearPassed = true;
		int idx = bubLst.indexOf(bub);
		List<Bubble> lst = bub.gearDisparting();
		for ( Bubble bubble : lst ){
			bubLst.add(++idx, bubble);
		}
		bubLst.remove(bub);
		normalize();
	}

	public double getArea (){
		double sumArea = 0;
		for ( Bubble bub : bubLst )
			sumArea += bub.getArea();
		return sumArea;
	}

	public void Render(Graphics G) {
		for ( Bubble bub : bubLst ){
			bub.Render(G);
		}
	}



}








































// old move method
//Bubble bubi;
//Point p;
//boolean temp = false;
//
//// move x :)
//
//Point  pntX   = new Point(pnt.getX(), 0);
//double speedX = speed;
//bubi = bubLst.get(0);
//p = bubi.getCenter().getAddition(pntX.getScaller(speedX));
//if (p.getX() - bubi.getRadius() - Circle.borderLen < 0 ){
//	speedX = 1;
//	pntX   = new Point(0 ,0).getDifference(bubi.getCenter());
//	temp = true;
//}
//
//bubi = bubLst.get( bubLst.size() - 1 );
//p = bubi.getCenter().getAddition(pntX.getScaller(speedX));
//if (!temp && p.getX() + bubi.getRadius() + Circle.borderLen > limits.getX() ){
//	speedX = 1;
//	pntX   = new Point(limits.getX() ,0).getDifference(bubi.getCenter());
//}
//for ( Bubble bub : bubLst ){
//	bub.move( new Point(pnt.getX(), 0), speed);
//}
//
//// move y :)
//Point  pntY   = new Point(0, pnt.getY());
//double speedY = speed;
//temp = false;
//bubi = null;
//for ( Bubble b : bubLst ){
//	if ( bubi == null )
//		bubi = b;
//	else if ( bubi.getRadius() < b.getRadius() )
//		bubi = b;
//}
//
//p = bubi.getCenter().getAddition(pntY.getScaller(speedY));
//if (p.getY() - bubi.getRadius() - Circle.borderLen < 0 ){
//	speedY = 1;
//	pntY   = new Point(0 ,0).getDifference(bubi.getCenter());
//	temp = true;
//}
//
//p = bubi.getCenter().getAddition(pntY.getScaller(speedY));
//if (!temp && p.getY() + bubi.getRadius() + Circle.borderLen > limits.getY() ){
//	speedY = 1;
//	pntY   = new Point(0, limits.getY()).getDifference(bubi.getCenter());
//}
//for ( Bubble bub : bubLst ){
//	bub.move( new Point(0, pntY.getY()), speed);
//}
//