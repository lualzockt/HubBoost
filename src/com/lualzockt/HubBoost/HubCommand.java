package com.lualzockt.HubBoost;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {
	private HubBoost plugin;
	public HubCommand(HubBoost p) {
		this.plugin = p;
	}
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,	String[] args) {
		if(args.length == 0) {
			// Show usage
			cs.sendMessage("§c" + cmd.getUsage());
			return true;
		}
		if(args[0].equalsIgnoreCase("reload")) {
			if(cs.hasPermission("hubboost.reload")) {
				plugin.reloadConfig();
				plugin.log.info("Reloading the config...");
				plugin.loadConfig();
				plugin.log.info("Reloaded the config.");
				cs.sendMessage(plugin.PREFIX + "The config was reloaded.");
			}else {
				cs.sendMessage(plugin.PREFIX + "§cYou do not have the permission perform this command.");
			}
			return true;
			
		}else if(args[0].equalsIgnoreCase("create")) {
			if(cs.hasPermission("hubboost.create")) {
				if(!(cs instanceof Player)) return false;
				Player p = (Player)cs;
				if(plugin.creation.contains(p.getName())) {
					p.sendMessage(plugin.PREFIX + "You are now back in PLAY-MODE.");
					plugin.creation.remove(p.getName());
					return true;
				}
				p.sendMessage(plugin.PREFIX + "You enabled the CREATION-MODE. Type /hb create again to disable it.");
				p.sendMessage(plugin.PREFIX + "Right click a PressurePlate now.");
				plugin.creation.add(p.getName());
			}else {
				cs.sendMessage(plugin.PREFIX + "§cYou do not have the permission perform this command.");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("list")){
			if(cs.hasPermission("hubboost.list")) {
				cs.sendMessage(plugin.PREFIX + "Current boosts: ");
				for(Location l : plugin.boosts) {
					cs.sendMessage("X=" + l.getBlockX() + " y=" + l.getBlockY() + " z=" + l.getBlockZ());
				}
			}
		}else if(args[0].equalsIgnoreCase("remove")) {
			if(cs.hasPermission("hubbost.remove")) {
				if(!(cs instanceof Player)) return false;
				Player p = (Player) cs;
				Block b = p.getTargetBlock(null, 100);
				if(b != null) {
					if(plugin.boosts.contains(b.getLocation())) {
						plugin.boosts.remove(b.getLocation());
						p.sendMessage(plugin.PREFIX + "HubBoost removed.");
					}else {
						p.sendMessage(plugin.PREFIX + "No HubBoost forced.");
					}
				}else {
					p.sendMessage(plugin.PREFIX + "No HubBoost forced.");
				}
			}
		}
		return true;
	}

}
