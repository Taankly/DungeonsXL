package io.github.dre2n.dungeonsxl.command;

import java.io.File;

import io.github.dre2n.dungeonsxl.DungeonsXL;
import io.github.dre2n.dungeonsxl.util.MessageUtil;
import io.github.dre2n.dungeonsxl.util.VersionUtil.Internals;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

public class MainCommand extends DCommand {
	
	public MainCommand() {
		setCommand("main");
		setHelp(plugin.getDMessages().get("Help_Cmd_main"));
		setPlayerCommand(true);
		setConsoleCommand(true);
	}
	
	@Override
	public void onExecute(String[] args, CommandSender sender) {
		PluginManager plugins = Bukkit.getServer().getPluginManager();
		
		int dungeons = new File(plugin.getDataFolder() + "/dungeons").listFiles().length;
		int loaded = plugin.getEditWorlds().size() + plugin.getGameWorlds().size();
		int players = plugin.getDPlayers().size();
		Internals internals = DungeonsXL.getPlugin().getVersion().getInternals();
		String vault = "";
		if (plugins.getPlugin("Vault") != null) {
			vault = plugins.getPlugin("Vault").getDescription().getVersion();
		}
		String mythicMobs = "";
		if (plugins.getPlugin("MythicMobs") != null) {
			mythicMobs = plugins.getPlugin("MythicMobs").getDescription().getVersion();
		}
		
		MessageUtil.sendCenteredMessage(sender, "&4" + MessageUtil.BIG_D[0] + "&f" + MessageUtil.BIG_X[0] + MessageUtil.BIG_L[0]);
		MessageUtil.sendCenteredMessage(sender, "&4" + MessageUtil.BIG_D[1] + "&f" + MessageUtil.BIG_X[1] + MessageUtil.BIG_L[1]);
		MessageUtil.sendCenteredMessage(sender, "&4" + MessageUtil.BIG_D[2] + "&f" + MessageUtil.BIG_X[2] + MessageUtil.BIG_L[2]);
		MessageUtil.sendCenteredMessage(sender, "&4" + MessageUtil.BIG_D[3] + "&f" + MessageUtil.BIG_X[3] + MessageUtil.BIG_L[3]);
		MessageUtil.sendCenteredMessage(sender, "&4" + MessageUtil.BIG_D[4] + "&f" + MessageUtil.BIG_X[4] + MessageUtil.BIG_L[4]);
		MessageUtil.sendCenteredMessage(sender, "&b&l####### " + plugin.getDMessages().get("Cmd_Main_Welcome") + "&7 v" + plugin.getDescription().getVersion() + " &b&l#######");
		MessageUtil.sendCenteredMessage(sender, plugin.getDMessages().get("Cmd_Main_Loaded", String.valueOf(dungeons), String.valueOf(loaded), String.valueOf(players)));
		MessageUtil.sendCenteredMessage(sender, plugin.getDMessages().get("Cmd_Main_Compatibility", String.valueOf(internals), vault, mythicMobs));
		MessageUtil.sendCenteredMessage(sender, plugin.getDMessages().get("Cmd_Main_Help"));
		MessageUtil.sendCenteredMessage(sender, "&7\u00a92012-2015 Frank Baumann & contributors; lcsd. under GPLv3.");
	}
	
}