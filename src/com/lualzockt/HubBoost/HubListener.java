package com.lualzockt.HubBoost;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class HubListener implements Listener{
	private HubBoost plugin;
	public HubListener(HubBoost p) {
		this.plugin = p;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(plugin.creation.contains(e.getPlayer().getName())) {
			plugin.creation.remove(e.getPlayer().getName());
		}
	}
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if(plugin.creation.contains(e.getPlayer().getName())) {
			plugin.creation.remove(e.getPlayer().getName());
		}
	}
	@EventHandler
	public void onDamage1(EntityDamageEvent e) {
		if(plugin.nofall) {
			if(e.getEntityType().equals(EntityType.PLAYER)){
				if(e.getCause().equals(DamageCause.FALL))  {
					e.setCancelled(true);
				}
			}
			
			
		}
		
		if(e.getEntity() instanceof Player) {
			if(plugin.creation.contains(((Player) e.getEntity()).getName())) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(plugin.boosts.contains(e.getBlock().getLocation()))  {
			e.setCancelled(true);
			e.getPlayer().sendMessage(plugin.PREFIX + "You can not destroy the HubBoost.");
		}
		
	}
	@EventHandler (priority = EventPriority.HIGH)
	public void onMove(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.PHYSICAL)) {
			
			if(plugin.boosts.contains(e.getClickedBlock().getLocation())) {
				Player p = e.getPlayer();
				
				Vector v = p.getLocation().getDirection();
				v.setX(v.getX() * plugin.faktor);
				v.setZ(v.getZ() * plugin.faktor);
				v.setY(plugin.faktor * 0.5D);
				if(plugin.sound != null) {
					p.playSound(p.getLocation(), plugin.sound, plugin.volume,plugin.azimuth);
				}
				p.setVelocity(v);
				
			}
		
	}
		}
		
	@EventHandler (priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent e) {
		
		if(plugin.creation.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
			if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
			Material m = e.getClickedBlock().getType();
			if(m.equals(Material.STONE_PLATE) || m.equals(Material.WOOD_PLATE) || m.equals(Material.IRON_PLATE) || m.equals(Material.GOLD_PLATE)){
				if(plugin.boosts.contains(e.getClickedBlock().getLocation())) {
					e.getPlayer().sendMessage(plugin.PREFIX + "That is already a HubBoost :(");
				}else {
					plugin.boosts.add(e.getClickedBlock().getLocation());
					e.getPlayer().sendMessage(plugin.PREFIX + "You created a new HubBoost successully. Type /hb create to leave the CREATION-MODE");
				}
				

			}
		}
		
	}
}
