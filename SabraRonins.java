
import robocode.*;
import java.awt.*;
//import java.awt.Color;


/**
 * Sabra Bot
 */
public class SabraRonins extends BravoBot
{

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	
	public void run() {
	
		setColors(Color.MAGENTA,Color.ORANGE,Color.green);
		// Initialize moveAmount to the maximum possible for this battlefield.
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		// Initialize peek to false
		peek = false;

		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of
		// getHeading() divided by 90.
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		//turnGunRight(90);
		turnRight(90);

		while (true) {
			// Look before we turn when ahead() completes.
			peek = true;
			// Move up the wall
			ahead(moveAmount);
			// Don't look now
			peek = false;
			// Turn to the next wall
			turnRight(90);

		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {	
			double distance = e.getDistance(); //get the distance of the scanned robot
		    if(distance > 800) //this conditions adjust the fire force according the distance of the scanned robot.
		        fire(3);
		    else if(distance > 600 && distance <= 800)
		        fire(2);
		    else if(distance > 400 && distance <= 600)
		        fire(2);
		    else if(distance > 200 && distance <= 400)
		        fire(2);
		    else if(distance < 200)
		        fire(1);
		
			// Note that scan is called automatically when the robot is moving.
			// By calling it manually here, we make sure we generate another scan event if there's a robot on the next
			// wall, so that we do not start moving up it until it's gone.
			if (peek) {
				scan();
			}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
	/**	double energy = getEnergy();
			    double bearing = e.getBearing(); //Get the direction which is arrived the bullet.
			    if(energy < 100){ // if the energy is low, the robot go away from the enemy
			        turnRight(-bearing); //This isn't accurate but release your robot.
			        ahead(100); //The robot goes away from the enemy.

			    }
			    else
			        turnRight(360); // scan
					**/
					
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	//	public void onHitWall(HitWallEvent e) {
			// Replace the next line with any behavior you would like
	//		back(60);
		//}	
	
	public void onHitRobot(HitRobotEvent e) {
	
      		// If he's in front of us, set back up a bit.
			if (e.getBearing() > -90 && e.getBearing() < 90) {
				back(100);
				
			} // else he's in back of us, so set ahead a bit.
			else {
				ahead(100);
			}
			
			// Determine a shot that won't kill the robot...
			// We want to ram him instead for bonus points
			if (e.getEnergy() > 16) {
				fire(3);
			} else if (e.getEnergy() > 10) {
				fire(2);
			} else if (e.getEnergy() > 4) {
				fire(1);
			} else if (e.getEnergy() > 2) {
				fire(.5);
			} else if (e.getEnergy() > .4) {
				fire(.1);
			}
			//if (e.isMyFault()) {
			//	turnRight(10);
			//}
	}
   

   
}