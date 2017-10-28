
import robocode.*;
import java.awt.*;
//import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * Sabra Bot
 */
public class SabraRonins extends AlphaBot
{

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	
	boolean wall = true;
	
	// tracker
	int count = 0; // Keeps track of how long we've
	// been searching for our target
	double gunTurnAmt; // How much to turn our gun when searching
	String trackName; // Name of the robot we're currently tracking
	
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
		turnGunRight(90);
		turnRight(90);

		while (true) {
			if (wall) {
				// Look before we turn when ahead() completes.
				peek = true;
				// Move up the wall
				ahead(moveAmount);
				// Don't look now
				peek = false;
				// Turn to the next wall
				turnRight(90);
			} else {
			
				setColors(Color.green,Color.green,Color.green);
					// turn the Gun (looks for enemy)
				turnGunRight(gunTurnAmt);
				// Keep track of how long we've been looking
				count++;
				// If we've haven't seen our target for 2 turns, look left
				if (count > 2) {
					gunTurnAmt = -10;
				}
				// If we still haven't seen our target for 5 turns, look right
				if (count > 5) {
					gunTurnAmt = 10;
				}
				// If we *still* haven't seen our target after 10 turns, find another target
				if (count > 11) {
					trackName = null;
				}
			}
			
						
		//if (getNumSentries() < 5) wall = false; 
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		if (wall) {		
			double distance = e.getDistance(); //get the distance of the scanned robot
		    if(distance > 800) //this conditions adjust the fire force according the distance of the scanned robot.
		        fire(5);
		    else if(distance > 600 && distance <= 800)
		        fire(4);
		    else if(distance > 400 && distance <= 600)
		        fire(3);
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
		} else {
		
		// If we have a target, and this isn't it, return immediately
		// so we can get more ScannedRobotEvents.
		if (trackName != null && !e.getName().equals(trackName)) {
			return;
		}

		// If we don't have a target, well, now we do!
		if (trackName == null) {
			trackName = e.getName();
			out.println("Tracking " + trackName);
		}
		// This is our target.  Reset count (see the run method)
		count = 0;
		// If our target is too far away, turn and move toward it.
		if (e.getDistance() > 150) {
			gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));

			turnGunRight(gunTurnAmt); // Try changing these to setTurnGunRight,
			turnRight(e.getBearing()); // and see how much Tracker improves...
			// (you'll have to make Tracker an AdvancedRobot)
			ahead(e.getDistance() - 140);
			return;
		}

		// Our target is close.
		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);

		// Our target is too close!  Back up.
		if (e.getDistance() < 100) {
			if (e.getBearing() > -90 && e.getBearing() <= 90) {
				back(40);
			} else {
				ahead(40);
			}
		}
		scan();
		
		}
		
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
public void onHitByBullet(HitByBulletEvent e) {
	double energy = getEnergy();
		    double bearing = e.getBearing(); //Get the direction which is arrived the bullet.
		    if(energy < 100){ // if the energy is low, the robot go away from the enemy
		        turnRight(-bearing); //This isn't accurate but release your robot.
		        ahead(100); //The robot goes away from the enemy.
		    }
		    else
		        turnRight(360); // scan
}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
//	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
//		back(60);
	//}	
	
	public void onHitRobot(HitRobotEvent event) {
		if (wall) {
      		// If he's in front of us, set back up a bit.
			if (event.getBearing() > -90 && event.getBearing() < 90) {
				back(100);
			} // else he's in back of us, so set ahead a bit.
			else {
				ahead(100);
			}
			//if (event.isMyFault()) {
				//turnRight(10);
			//}
		} else {
					// Only print if he's not already our target.
			if (trackName != null && !trackName.equals(event.getName())) {
				out.println("Tracking " + event.getName() + " due to collision");
			}
			// Set the target
			trackName = event.getName();
			// Back up a bit.
			// Note:  We won't get scan events while we're doing this!
			// An AdvancedRobot might use setBack(); execute();
			gunTurnAmt = normalRelativeAngleDegrees(event.getBearing() + (getHeading() - getRadarHeading()));
			turnGunRight(gunTurnAmt);
			fire(3);
			back(50);
			}
   }
   

   
}