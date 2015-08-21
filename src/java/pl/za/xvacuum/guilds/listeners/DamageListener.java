package pl.za.xvacuum.guilds.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import pl.za.xvacuum.guilds.antylogout.LogoutSystem;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent e) {
		Entity entAttacker = e.getDamager();
		Entity entVictim = e.getEntity();

	    if (!(entVictim instanceof Player)) return;
	    Player attacker = null;
	    if ((entAttacker instanceof Player)) { attacker = (Player)entAttacker;
	    } else if ((entAttacker instanceof Projectile))
	    {
	      LivingEntity le = (LivingEntity) ((Projectile)entAttacker).getShooter();
	      if ((le instanceof Player)) attacker = (Player)le;
	    }

	    if (attacker == null) return;
		Player victim = (Player)entVictim;
			
		User userAttacker = User.get(attacker);
		User userVictim = User.get(victim);
			
		if(userAttacker.hasGuild() && userVictim.hasGuild())  {
			Guild guildAttacker = userAttacker.getGuild();
			Guild guildVictim = userVictim.getGuild();
			if(guildAttacker == guildVictim) {
				if(!guildAttacker.isPvp()) {
					e.setDamage(0.0);
					return;
				}else {
					if(!LogoutSystem.getTimes().contains(victim)) {
						LogoutSystem.start(victim);
					}else if(!LogoutSystem.getTimes().contains(attacker)) {
						LogoutSystem.start(attacker);
					}
					LogoutSystem.getLastAttackerMap().put(victim, attacker);
					return;
				}
			}else if(guildAttacker.getAllies().contains(guildVictim)) {
				e.setDamage(0.0);
				return;
			}else {
				if(!LogoutSystem.getTimes().contains(victim)) {
					LogoutSystem.start(victim);
				}else if(!LogoutSystem.getTimes().contains(attacker)) {
					LogoutSystem.start(attacker);
				}
				LogoutSystem.getLastAttackerMap().put(victim, attacker);
				return;
			}
		}else {
			if(!LogoutSystem.getTimes().contains(victim)) {
				LogoutSystem.start(victim);
			}else if(!LogoutSystem.getTimes().contains(attacker)) {
				LogoutSystem.start(attacker);
			}
			LogoutSystem.getLastAttackerMap().put(victim, attacker);
			return;
		}
	}

}
