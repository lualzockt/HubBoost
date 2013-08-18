//          This program is free software: you can redistribute it and/or modify
//	    it under the terms of the GNU General Public License as published by
//	    the Free Software Foundation, either version 3 of the License, or
//	    (at your option) any later version.
//
//	    This program is distributed in the hope that it will be useful,
//	    but WITHOUT ANY WARRANTY; without even the implied warranty of
//	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	    GNU General Public License for more details.
//
//	    You should have received a copy of the GNU General Public License
//	    along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//	    Dieses Programm ist Freie Software: Sie können es unter den Bedingungen
//	    der GNU General Public License, wie von der Free Software Foundation,
//	    Version 3 der Lizenz oder (nach Ihrer Wahl) jeder neueren
//	    veröffentlichten Version, weiterverbreiten und/oder modifizieren.
//
//	    Dieses Programm wird in der Hoffnung, dass es nützlich sein wird, aber
//	    OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
//	    Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
//	    Siehe die GNU General Public License für weitere Details.
package com.lualzockt.HubBoost;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HubBoost extends JavaPlugin implements Listener{
	public static String serial(Location l) {
		return new String(l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "," + l.getWorld().getName());
	}
	public static Location deserial(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getWorld(a[3]);
		if(w == null) {
			w = Bukkit.getWorlds().get(0);
		}
		int x = Integer.parseInt(a[0]);
		int y = Integer.parseInt(a[1]);
		int z = Integer.parseInt(a[2]);
		Location  l = new Location(w,x,y,z);
		return l;
	}
	public static final String PREFIX = "�8[�aHubBoost�8] �7 ";
	float faktor = 4.0F;
	Sound sound = Sound.EXPLODE;
	float volume = 1f;
	float azimuth = 1f;
	boolean nofall;
	public List<Location> boosts;
	public List<String> creation;
	public List<String> remove;
	public static Logger log;
	@Override
	public void onDisable() {
		List<String> list = new ArrayList<String>();
		for(Location l : boosts) {
			list.add(serial(l));
		}
		getConfig().set("boosts", list);
		saveConfig();
		super.onDisable();
	}
	public void loadConfig() {
		getConfig().addDefault("boosts", new ArrayList<String>());
		getConfig().addDefault("factor", 4.0F);
		getConfig().addDefault("sound", "EXPLODE");
		getConfig().addDefault("volume", 1f);
		getConfig().addDefault("azimuth", 1f);
		getConfig().addDefault("nofall", true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		faktor = (float) getConfig().getDouble("factor");
		String sn = getConfig().getString("sound");
		if(!sn.equalsIgnoreCase("none")) {
			sound = Sound.valueOf(sn);
		}else {
			sound = null;
		}
		volume = (float) getConfig().getDouble("volume");
		azimuth = (float) getConfig().getDouble("azimuth");
		nofall = getConfig().getBoolean("nofall");
	}
	@Override
	public void onEnable() {
		boosts = new ArrayList<Location>();
		creation = new ArrayList<String>();
		remove = new ArrayList<String>();
		log = getLogger();
		Metrics m;
		try {
			m = new Metrics(this);
			m.start();
			log.finest("Started Metrics");
		} catch (IOException e) {
			log.warning("Could not start Metrics Service :(");
		}
		
		
		log.info("Loading the config...");
		loadConfig();
		log.finest("The config was loaded successfully.");
		
		List<String> locs = getConfig().getStringList("boosts");
		if(!locs.isEmpty()) {
			for(String s : locs) {
			boosts.add(deserial(s));
		}
		}
		
		
		
		getCommand("hubboost").setExecutor(new HubCommand(this));
		getServer().getPluginManager().registerEvents(new HubListener(this), this);
		super.onEnable();
		
	}
	

}
