/*
 * Copyright (C) 2012-2017 Frank Baumann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.dre2n.dungeonsxl.player;

import io.github.dre2n.commons.compatibility.CompatibilityHandler;
import io.github.dre2n.commons.compatibility.Version;
import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.commons.util.playerutil.PlayerUtil;
import io.github.dre2n.dungeonsxl.DungeonsXL;
import io.github.dre2n.dungeonsxl.config.DMessages;
import io.github.dre2n.dungeonsxl.config.PlayerData;
import io.github.dre2n.dungeonsxl.event.dgroup.DGroupCreateEvent;
import io.github.dre2n.dungeonsxl.game.Game;
import io.github.dre2n.dungeonsxl.global.DPortal;
import io.github.dre2n.dungeonsxl.world.DGameWorld;
import io.github.dre2n.dungeonsxl.world.DResourceWorld;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * Represents a player in the non-DXL worlds of the server.
 *
 * @author Daniel Saukel
 */
public class DGlobalPlayer {

    static DungeonsXL plugin = DungeonsXL.getInstance();

    protected Player player;

    private PlayerData data;

    private boolean breakMode;
    private boolean chatSpyMode;
    private DPortal creatingPortal;
    private boolean announcerEnabled = true;

    private ItemStack[] respawnInventory;
    private ItemStack[] respawnArmor;
    private CopyOnWriteArrayList<ItemStack> rewardItems;

    public DGlobalPlayer(Player player) {
        this(player, false);
    }

    public DGlobalPlayer(Player player, boolean reset) {
        this.player = player;

        loadPlayerData(new File(DungeonsXL.PLAYERS, player.getUniqueId().toString() + ".yml"));
        if (reset && data.wasInGame()) {
            reset(false);
        }

        plugin.getDPlayers().addPlayer(this);
    }

    public DGlobalPlayer(DGlobalPlayer dPlayer) {
        player = dPlayer.getPlayer();
        breakMode = dPlayer.isInBreakMode();
        chatSpyMode = dPlayer.isInChatSpyMode();
        creatingPortal = dPlayer.getPortal();
        announcerEnabled = dPlayer.isAnnouncerEnabled();
        respawnInventory = dPlayer.getRespawnInventory();
        respawnArmor = dPlayer.getRespawnArmor();

        plugin.getDPlayers().addPlayer(this);
    }

    /* Getters and setters */
    /**
     * @return the player's name
     */
    public String getName() {
        return player.getName();
    }

    /**
     * @return the Bukkit player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the saved data
     */
    public PlayerData getData() {
        return data;
    }

    /**
     * Load / reload a new instance of PlayerData
     */
    public void loadPlayerData(File file) {
        data = new PlayerData(file);
    }

    /**
     * @return if the player is in break mode
     */
    public boolean isInBreakMode() {
        return breakMode;
    }

    /**
     * @param breakMode
     * sets if the player is in break mode
     */
    public void setInBreakMode(boolean breakMode) {
        this.breakMode = breakMode;
    }

    /**
     * @return if the player spies the DXL chat channels
     */
    public boolean isInChatSpyMode() {
        return chatSpyMode;
    }

    /**
     * @param chatSpyMode
     * sets if the player is in chat spy mode
     */
    public void setInChatSpyMode(boolean chatSpyMode) {
        this.chatSpyMode = chatSpyMode;
    }

    /**
     * @return if the player is creating a DPortal
     */
    public boolean isCreatingPortal() {
        return creatingPortal != null;
    }

    /**
     * @return the portal the player is creating
     */
    public DPortal getPortal() {
        return creatingPortal;
    }

    /**
     * @param dPortal
     * the portal to create
     */
    public void setCreatingPortal(DPortal dPortal) {
        creatingPortal = dPortal;
    }

    /**
     * @return if the players receives announcer messages
     */
    public boolean isAnnouncerEnabled() {
        return announcerEnabled;
    }

    /**
     * @param enabled
     * set if the players receives announcer messages
     */
    public void setAnnouncerEnabled(boolean enabled) {
        announcerEnabled = enabled;
    }

    /**
     * @return the respawnInventory
     */
    public ItemStack[] getRespawnInventory() {
        return respawnInventory;
    }

    /**
     * @param respawnInventory
     * the respawnInventory to set
     */
    public void setRespawnInventory(ItemStack[] respawnInventory) {
        this.respawnInventory = respawnInventory;
    }

    /**
     * Give the saved respawn inventory to the player
     */
    public void applyRespawnInventory() {
        if (respawnInventory == null || respawnArmor == null) {
            return;
        }

        player.getInventory().setContents(respawnInventory);
        player.getInventory().setArmorContents(respawnArmor);
        respawnInventory = null;
        respawnArmor = null;
    }

    /**
     * @return the respawnArmor
     */
    public ItemStack[] getRespawnArmor() {
        return respawnArmor;
    }

    /**
     * @param respawnArmor
     * the respawnArmor to set
     */
    public void setRespawnArmor(ItemStack[] respawnArmor) {
        this.respawnArmor = respawnArmor;
    }

    /**
     * @param permission
     * the permission to check
     * @return if the player has the permission
     */
    public boolean hasPermission(DPermissions permission) {
        return DPermissions.hasPermission(player, permission);
    }

    /**
     * @return the reward items
     */
    public CopyOnWriteArrayList<ItemStack> getRewardItems() {
        return rewardItems;
    }

    /**
     * @return if the player has reward items left
     */
    public boolean hasRewardItemsLeft() {
        return rewardItems != null;
    }

    /**
     * @param rewardItems
     * the reward items to set
     */
    public void setRewardItems(CopyOnWriteArrayList<ItemStack> rewardItems) {
        this.rewardItems = rewardItems;
    }

    /**
     * @param permission
     * the permission to check
     * @return if the player has the permission
     */
    public boolean hasPermission(String permission) {
        return DPermissions.hasPermission(player, permission);
    }

    /* Actions */
    /**
     * Respawns the player at his old position before he was in a dungeon
     */
    public void reset(boolean keepInventory) {
        try {
            if (!keepInventory) {
                while (data.getOldInventory().size() > 36) {
                    data.getOldInventory().remove(36);
                }
                player.getInventory().setContents(data.getOldInventory().toArray(new ItemStack[36]));
                player.getInventory().setArmorContents(data.getOldArmor().toArray(new ItemStack[4]));
                if (Version.andHigher(Version.MC1_9).contains(CompatibilityHandler.getInstance().getVersion())) {
                    player.getInventory().setItemInOffHand(data.getOldOffHand());
                }
                player.setLevel(data.getOldLevel());
                player.setExp(data.getOldExp());
                player.setMaxHealth(data.getOldMaxHealth());
                player.setHealth(data.getOldHealth());
                player.setFoodLevel(data.getOldFoodLevel());
                player.setGameMode(data.getOldGameMode());
                player.setFireTicks(data.getOldFireTicks());
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }

                player.addPotionEffects(data.getOldPotionEffects());
            }

            if (data.getOldLocation().getWorld() != null) {
                PlayerUtil.secureTeleport(player, data.getOldLocation());
            } else {
                PlayerUtil.secureTeleport(player, Bukkit.getWorlds().get(0).getSpawnLocation());
            }

        } catch (NullPointerException exception) {
            exception.printStackTrace();
            player.setHealth(0);
            MessageUtil.log(plugin, DMessages.LOG_KILLED_CORRUPTED_PLAYER.getMessage(player.getName()));
        }

        data.clearPlayerState();
    }

    /**
     * Starts the tutorial
     */
    public void startTutorial() {
        if (plugin.getPermissionProvider() == null || !plugin.getPermissionProvider().hasGroupSupport()) {
            return;
        }

        final String startGroup = plugin.getMainConfig().getTutorialStartGroup();
        if ((plugin.getMainConfig().getTutorialDungeon() == null || startGroup == null)) {
            return;
        }

        if (plugin.isGroupEnabled(startGroup)) {
            plugin.getPermissionProvider().playerAddGroup(player, startGroup);
        }

        DGroup dGroup = new DGroup(player, plugin.getMainConfig().getTutorialDungeon(), false);

        DGroupCreateEvent createEvent = new DGroupCreateEvent(dGroup, player, DGroupCreateEvent.Cause.GROUP_SIGN);
        plugin.getServer().getPluginManager().callEvent(createEvent);

        if (createEvent.isCancelled()) {
            dGroup = null;
        }

        if (dGroup == null) {
            return;
        }

        DGameWorld gameWorld = null;

        if (dGroup.getGameWorld() == null) {
            DResourceWorld resource = plugin.getDWorlds().getResourceByName(dGroup.getMapName());
            if (resource == null) {
                MessageUtil.sendMessage(player, DMessages.ERROR_TUTORIAL_NOT_EXIST.getMessage());
                return;
            }

            gameWorld = resource.instantiateAsGameWorld();
            dGroup.setGameWorld(gameWorld);
        }

        new Game(dGroup, gameWorld).setTutorial(true);
        DGamePlayer.create(player, gameWorld);
    }

}
