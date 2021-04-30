package space.gorogoro.gorogoroitem;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GorogoroItem extends JavaPlugin implements Listener {

  private final static String PHANTOM_GUARD_NORMAL = "space.gorogoro.phantomguard.normal";
  private final static String PHANTOM_GUARD_RARE = "space.gorogoro.phantomguard.rare";

  @Override
  public void onEnable(){
    try{
      getLogger().info("The Plugin Has Been Enabled!");

      final PluginManager pm = getServer().getPluginManager();
      pm.registerEvents(this, this);

      new GorogoroItemTask().setGorogoroItem(this).runTaskTimer(this, 0L, 10L);

    } catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * JavaPlugin method onCommand.
   *
   * @return boolean true:Success false:Display the usage dialog set in plugin.yml
   */
  public boolean onCommand( CommandSender sender, Command commandInfo, String label, String[] args) {
    try{
      if(!commandInfo.getName().equals("gitemget")) {
        return false;
      }

      if(!(sender instanceof Player)) {
        return false;
      }
      Player p = (Player) sender;

      if(args.length <= 0) {
        return false;
      }
      String subCommand = args[0];

      if(!p.isOp()) {
        p.sendMessage("権限がありません。");
        return true;
      }

      switch(subCommand) {
        case "phantom":
          if(args.length != 2) {
            return false;
          }
          String type = args[1];
          if(!type.equals("normal") && !type.equals("rare")) {
            return false;
          }

          ItemStack item = new ItemStack(Material.STONE_BUTTON, 1);
          ItemMeta meta = item.getItemMeta();
          ArrayList<String> lore = new ArrayList<String>();
          lore.add(ChatColor.DARK_PURPLE + "隕石と一緒に宇宙から落ちてきた" + ChatColor.RESET);
          lore.add(ChatColor.DARK_PURPLE + "宇宙人の防具らしい。" + ChatColor.RESET);
          lore.add(ChatColor.DARK_PURPLE + "宇宙人の文字が書いてある" + ChatColor.RESET);
          if(type.equals("rare")) {
            meta.setDisplayName(ChatColor.GOLD + "[レア]" + ChatColor.RESET + ChatColor.DARK_RED + "ファントムバリア" + ChatColor.RESET);
            lore.add(ChatColor.DARK_PURPLE + "「" + ChatColor.RESET + ChatColor.MAGIC + PHANTOM_GUARD_RARE + ChatColor.RESET + ChatColor.DARK_PURPLE + "」" + ChatColor.RESET);
          }else {
            meta.setDisplayName(ChatColor.DARK_RED + "ファントムバリア" + ChatColor.RESET);
            lore.add(ChatColor.DARK_PURPLE + "「" + ChatColor.RESET + ChatColor.MAGIC + PHANTOM_GUARD_NORMAL + ChatColor.RESET + ChatColor.DARK_PURPLE + "」" + ChatColor.RESET);
          }
          meta.setLore(lore);
          item.setItemMeta(meta);
          p.getInventory().addItem(item);
        break;

        default:
      }
      return true;

    } catch (Exception e){
      e.printStackTrace();
    }
    return false;
  }

  public boolean isPhantomGuardRare(Player p) {
    return isPhantomGuard(p, PHANTOM_GUARD_RARE);
  }

  public boolean isPhantomGuardNormal(Player p) {
    return isPhantomGuard(p, PHANTOM_GUARD_NORMAL);
  }

  private boolean isPhantomGuard(Player p, String type) {
    if(p == null) {
      return false;
    }
    for (int i = 0, size = p.getInventory().getSize(); i < size; ++i) {
      ItemStack item = p.getInventory().getItem(i);
      if (item != null && item.getType().equals(Material.STONE_BUTTON)) {
        ItemMeta meta = item.getItemMeta();
        if(meta != null && meta.getLore() != null) {
          for(String cur:meta.getLore()) {
            if(cur.contains(type)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

    Entity damager = e.getDamager();
    if(!damager.getType().equals(EntityType.PHANTOM)) {
      return;
    }

    Entity target = e.getEntity();
    if(!target.getType().equals(EntityType.PLAYER)) {
      return;
    }
    Player p = (Player)target;

    if(!isPhantomGuardNormal(p) && !isPhantomGuardRare(p)) {
      return;
    }

    e.setCancelled(true);
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {

    ItemStack item = e.getItemInHand();
    if(!(item instanceof ItemStack)) {
      return;
    }

    if(!e.getItemInHand().getType().equals(Material.STONE_BUTTON)) {
      return;
    }

    ItemMeta meta = item.getItemMeta();
    if(meta != null && meta.getLore() != null) {
      for(String cur:meta.getLore()) {
        if(cur.contains(PHANTOM_GUARD_NORMAL) || cur.contains(PHANTOM_GUARD_RARE)) {
          e.setCancelled(true);
        }
      }
    }
  }

  @Override
  public void onDisable(){
    try{
      getLogger().info("The Plugin Has Been Disabled!");
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
