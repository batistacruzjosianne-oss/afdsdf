package br.com.bedwars189;

import java.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWars extends JavaPlugin implements Listener {
    private final Map<String, Set<UUID>> teams = new HashMap<String, Set<UUID>>();
    private final Map<String, Location> beds = new HashMap<String, Location>();
    private final Map<String, Location> spawns = new HashMap<String, Location>();
    private final Set<String> alive = new HashSet<String>();
    private final Map<UUID,String> playerTeam = new HashMap<UUID,String>();
    private boolean running = false;

    public void onEnable() {
        teams.put("red", new HashSet<UUID>()); teams.put("blue", new HashSet<UUID>());
        loadConfig();
        for (String t : Arrays.asList("red","blue")) {
            if (getConfig().contains("beds."+t)) beds.put(t, getConfig().getLocation("beds."+t));
            if (getConfig().contains("spawns."+t)) spawns.put(t, getConfig().getLocation("spawns."+t));
        }
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("BedWars189 pronto. Configure cama e spawn com /bw.");
    }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 0) return false;
        String sub=a[0].toLowerCase();
        if (sub.equals("start") && s.hasPermission("bedwars.admin")) {
            if (!beds.containsKey("red") || !beds.containsKey("blue") || !spawns.containsKey("red") || !spawns.containsKey("blue")) { s.sendMessage("§cConfigure as duas camas e os dois spawns."); return true; }
            running=true; alive.clear(); alive.add("red"); alive.add("blue");
            Bukkit.broadcastMessage("§aBedWars começou!");
            return true;
        }
        if (!(s instanceof Player)) { s.sendMessage("Comando apenas para jogador."); return true; }
        Player p=(Player)s;
        if (sub.equals("join") && a.length>1) {
            String t=a[1].toLowerCase(); if (!teams.containsKey(t)) return false;
            leave(p); teams.get(t).add(p.getUniqueId()); playerTeam.put(p.getUniqueId(),t);
            if (spawns.containsKey(t)) p.teleport(spawns.get(t));
            p.sendMessage("§aVocê entrou no time "+t+"."); return true;
        }
        if (sub.equals("leave")) { leave(p); p.sendMessage("§eVocê saiu da partida."); return true; }
        if ((sub.equals("setbed") || sub.equals("setspawn")) && a.length>1 && p.hasPermission("bedwars.admin")) {
            String t=a[1].toLowerCase(); if (!teams.containsKey(t)) return false;
            Location loc = sub.equals("setbed") ? p.getTargetBlock((Set<Material>)null, 6).getLocation() : p.getLocation();
            if (sub.equals("setbed")) beds.put(t,loc); else spawns.put(t,loc);
            getConfig().set((sub.equals("setbed")?"beds.":"spawns.")+t,loc); saveConfig();
            p.sendMessage("§a"+(sub.equals("setbed")?"Cama":"Spawn")+" "+t+" salvo."); return true;
        }
        return false;
    }
    private void leave(Player p) { String t=playerTeam.remove(p.getUniqueId()); if(t!=null) teams.get(t).remove(p.getUniqueId()); }
    @EventHandler public void onBreak(BlockBreakEvent e) {
        if (!running) return;
        for (String t: Arrays.asList("red","blue")) {
            Location bed=beds.get(t); if (bed==null) continue;
            if (bed.getWorld().equals(e.getBlock().getWorld()) && bed.distanceSquared(e.getBlock().getLocation())<=2) {
                String attacker=playerTeam.get(e.getPlayer().getUniqueId());
                if (t.equals(attacker)) { e.setCancelled(true); e.getPlayer().sendMessage("§cNão quebre sua própria cama!"); return; }
                alive.remove(t); Bukkit.broadcastMessage("§cA cama "+t+" foi destruída!");
                if (alive.size()==1) { running=false; Bukkit.broadcastMessage("§6Time "+alive.iterator().next()+" venceu!"); }
            }
        }
    }
    @EventHandler public void onDeath(PlayerDeathEvent e) {
        if (!running) return;
        Player p=e.getEntity(); String t=playerTeam.get(p.getUniqueId());
        if (t!=null && !alive.contains(t)) Bukkit.getScheduler().runTask(this, new Runnable(){ public void run(){ p.kickPlayer("Sua cama foi destruída. Fim de jogo!"); }});
    }
    @EventHandler public void onRespawn(PlayerRespawnEvent e) {
        String t=playerTeam.get(e.getPlayer().getUniqueId());
        if (running && t!=null && alive.contains(t) && spawns.containsKey(t)) e.setRespawnLocation(spawns.get(t));
    }
}

