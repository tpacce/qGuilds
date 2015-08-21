package pl.za.xvacuum.guilds.utils.reflections;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.utils.reflections.ParticleUtils.ParticleType;

public class ParticleShape {
	
	public static void send(final Location loc) 
	{
		new BukkitRunnable(){
			double t = Math.PI/4;
			public void run(){
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					ParticleUtils part = new ParticleUtils(ParticleType.EXPLOSION_LARGE, 10, 5, 35);
					part.sendToLocation(loc);
					loc.subtract(x,y,z);
					theta = theta + Math.PI/64;
					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					ParticleUtils part2 = new ParticleUtils(ParticleType.BARRIER, 5, 10, 45);
					part2.sendToLocation(loc);
					loc.subtract(x,y,z);
				}
				if (t > 20){
					this.cancel();
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
