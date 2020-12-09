package me.Th3Gr3y8unny.RandomBlockLook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;

public class Main extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	private BukkitTask task;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0 && args[0].equals("start")) {
			ArrayList<Location> locs = new ArrayList<Location>();
			ArrayList<Material> blocks = new ArrayList<Material>();
			task = new BukkitRunnable() {
	
				@Override
				public void run() {
					//Gets a list of possible blocks
					if (blocks.size() == 0) {
						//Gets random block if looking at different block
						for (Material block : Material.values()) {
							if (block.isBlock()) {
								blocks.add(block);
							}
						}
					}
					
					int index = 0;
					for (Player p : Bukkit.getOnlinePlayers()) {
						
						BlockIterator iter = new BlockIterator(p, 100); //Get location of block with with max distance of 100
	
					    Block lastBlock = iter.next();
	
					    while (iter.hasNext()) {
					        lastBlock = iter.next();
	
					        if (lastBlock.getType() == Material.AIR) {
					            continue;
					        }
					        break;
					    }
					    
					    Location loc = lastBlock.getLocation();
					    
					    if (locs.size() == 0) {
					    	locs.add(loc);
					    }
					    else if (loc.getBlock().getType() == Material.AIR) {
					    	continue;
					    }
					    else {
					    	//Checks if player looks at different block
					    	boolean diffX = locs.get(index).getBlockX() != loc.getBlockX();
					    	boolean diffY = locs.get(index).getBlockY() != loc.getBlockY();
					    	boolean diffZ = locs.get(index).getBlockZ() != loc.getBlockZ();
					    	if (diffX || diffY || diffZ) {
								Material randomBlock = blocks.get(new Random().nextInt(blocks.size()));
								loc.getBlock().setType(randomBlock);
								locs.set(index, loc);
					    	}
					    }
					    index++;
					}
				}
			}.runTaskTimer(this, 0, 1L);
		}
		else if (args.length > 0 && args[0].equals("stop")) {
			task.cancel();
		}
		return false;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (command.getName().equalsIgnoreCase("randomblocklook")) {
            return Arrays.asList("start", "stop");
        }

        return null;
    }
}
