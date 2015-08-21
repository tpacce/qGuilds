package pl.za.xvacuum.guilds.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BuildUtils {
	
	/**
	 * @author Dzikoysk
	 * @param loc
	 * @param radius
	 * @param height
	 * @param hollow
	 * @param sphere
	 * @param plusY
	 * @return
	 */
	
	public static Collection<Location> sphere(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY){
		Collection<Location> circleblocks = new ArrayList<Location>();
		int cx = loc.getBlockX();
		int cy = loc.getBlockY();
		int cz = loc.getBlockZ();
 
		for(int x = cx - radius; x <= cx + radius; x++){
			for (int z = cz - radius; z <= cz + radius; z++){
				for(int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++){
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
 
					if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))){
						Location l = new Location(loc.getWorld(), x, y + plusY, z);
						circleblocks.add(l);
					}
				}
			}
		}
		
		return circleblocks;
	}
	
	public static void cylinder(Location loc, int r, Material mat) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
 
        for (int x = cx - r; x <= cx +r; x++) {
                for (int z = cz - r; z <= cz +r; z++) {
                        if ((cx - x) * (cx -x) + (cz - z) * (cz - z) <= rSquared) {
                        	w.getBlockAt(x, cy, z).setType(mat);
                        }
                }
        }
}
 

}
