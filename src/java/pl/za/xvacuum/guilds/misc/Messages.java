package pl.za.xvacuum.guilds.misc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.misc.files.LangFile;
import pl.za.xvacuum.guilds.utils.ListUtils;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class Messages {
	
	public static String DefaultLanguage = "en  # Polskie tlumaczenie: http://xvacuum.za.pl/qGuilds/spolszczenie.txt (ps: zmien tez en na pl)";
	public static String CommandNoPerm = "&cAccess denied! &7(%permission%)";
	public static String CommandNoPlayer = "&cAccess denied! &7(You are console)";
	public static String CommandBadUsage = "&cCorrect usage: &7%usage%";
	public static String TeleportSuccess = "&aYou have been teleported!";
	public static String TeleportCancelled = "&cTeleport cancelled...";
	public static String TeleportProcess = "&aPlease wait %time% seconds to teleport!";
	public static String EventBlockBreak = "&cYou cant destroy blocks at this region!";
	public static String EventBlockPlace = "&cYou cant place blocks at this region!";
	public static String EventBlockPlaceTNT = "&cYou cant place blocks for %time% due to TNT explode!";
	public static String EventPlayerJoinNotify = "&9Player %player% from your guild joined to server!";
	public static String EventPlayerQuitNotify = "&9Player %player% from your guild left the server!";
	public static String EventDeathFormat = "&c%victimTag%%victimName% has been murdered by %killerTag%%killerName% (+%points%)";
	public static String PlayerDontHasPerm = "&cYou must be a master or guild leader to do that!";
	public static String PlayerMustBeLeader = "&cYou must be a guild leader!";
	public static String PlayerNotPlayedBefore = "&cThat player doesnt exists!";
	public static String PlayerIsntOnline = "&cThat player isnt online!";
	public static String PlayerHasGuild = "&cYou actually have a guild!";
	public static String PlayerDontHasGuild = "&cYou actually dont have a guild!";
	public static String PlayerHasGuildOther = "&cThat player actually have a guild!";
	public static String PlayerDontHasGuildOther = "&cThat player actually dont have a guild!";
	public static String PlayerNotInGuild = "&aThat player is not in your guild!";
	public static String PlayerMustBeInGuild = "&cYou must be in your guild region!";
	public static String GuildNotExist = "&cThat guild not exists!";
	public static String CreateBadWorld = "&cYou cant create guild at this world!";
	public static String CreateBadLengthTagMin = "&cYour tag must have min. %length% letters!";
	public static String CreateBadLengthTagMax = "&cYour tag must have max. %length% letters!";
	public static String CreateBadLengthNameMin = "&cYour name must have min. %length% letters!";
	public static String CreateBadLengthNameMax = "&cYour name must have max. %length% letters!";
	public static String CreateGuildExist = "&cThat guild already exists!";
	public static String CreateGuildSpawnProtect = "&cYou are too close to the spawn!";
	public static String CreateNoItems = "&cYou dont have all items for the guild!";
	public static String CreateGuildNear = "&cThere are guild near you!";
	public static String CreateBroadcast = "&aGuild %name% [%tag%] has been created by %player%!";
	public static String DeleteBroadcast = "&cGuild %name% [%tag%] has been deleted by %player%!";
	public static String InfoGuildDoesntExists = "&cThat guild doesnt exists!";
	public static String InviteSuccessInvited = "&aHey, you have been invited to guild %name% [%tag%] by %player%. \n&aJoin by command /join <tag>!";
	public static String InviteSuccessPlayer = "&aSuccessfully invited %player% to guild!";
	public static String InviteUserHasGuild = "&cThe player you want to invite has guild!";
	public static String InviteCooldownStops = "&aYour invitation to guild %tag% is no longer valid.";
	public static String InviteCancelledPlayer = "&aSuccessfuly cancelled invite for player %player%!";
	public static String InviteCancelledInvited = "&cYour invite to guild %tag% has been cancelled!";
	public static String JoinNotInvited = "&cYou are not invited to that guild.";
	public static String JoinDontHaveItems = "&cYou dont have all items for join!";
	public static String JoinBroadcast = "&aPlayer %player% has joined to guild %tag%";
	public static String LeaveIsLeader = "&cYou cant leave own guild!";
	public static String LeaveBroadcast = "&aPlayer %player% has left from guild %tag%";
	public static String KickNotInGuild = "&cThat player is not member of your guild!";
	public static String KickCantKick = "&cYou cant kick that person!";
	public static String KickBroadcast = "&aPlayer %player% has been kicked from guild %tag%";
	public static String PvpSuccess = "&aSuccessfully toggled pvp to %status%!";
	public static String LeaderNoPerm = "&cYou must be guild leader to do that!";
	public static String LeaderBroadcast = "&aPlayer %player% became new leader of guild %tag%";
	public static String MasterFullOfMasters = "&cThis guild has too many masters!";
	public static String MasterBroadcastUpgrade = "&aPlayer %player% became new master of guild %tag%";
	public static String MasterBroadcastDowngrade = "&aPlayer %player% is no longer master in %tag%";
	public static String SethomeSuccess = "&aSuccessfully generated new home location!";
	public static String TreasureTitle = "&aTreasure of guild:";
	public static String TitleSuccess = "&aSuccessfuly assigned title to player!";
	public static String BroadcastDefault = "Default broadcast, change this by /broadcast [text]";
	public static String BroadcastPlayerNotify = "~ %player%";
	public static String BroadcastJoinNotify = "&2Last broadcast: &a%broadcast%";
	public static String BroadcastNotify = "&2Guild: &a%broadcast% &2~ %player%";
	public static String EnlargeNoItems = "&cYou dont have all items for the enlarge! (%amount% x %item%)";
	public static String EnlargeMaxSize = "&cYou cant enlarge, that region is too big!";
	public static String EnlargeSuccess = "&aSuccessfuly enlarged your guild region! (%actualSize% x %actualSize%)";
	public static String TopPrefixNone = "&7%pos%. ";
	public static String TopPrefixGuild = "&7%pos%. &a%tag% &7(&a%points%&7, K: &a%kills%&7, D: &a%deaths%&7)";
	public static String TopPrefixOwnGuild = "&7%pos%. &2%tag% &7(&a%points%&7, K: &a%kills%&7, D: &a%deaths%&7)";
	public static String TopLine = "&8&l&m===============================";
	public static String AllySameGuild = "&cYou cant ally with own guild!";
	public static String AllyBroadcastBreak = "&cBreak ally: %tag1% with %tag2%";
	public static String AllyRequestCancelled = "&cYour request has been cancelled!";
	public static String AllyRequestCancelledToGuild = "&cInvite from %tag% has been cancelled!";
	public static String AllyBroadcast = "&aNew ally: %tag1% with %tag2%";
	public static String AllyRequestSuccess = "&aYou invited guild to ally!";
	public static String AllyRequestToGuild = "&aGuild %tag% want to ally with your guild!";
	public static String EffectDontHaveItems = "&cYou dont have all items for effect!";
	public static String EffectBadTime = "&cYou can use this at %date%";
	public static String EffectSuccess = "&aPerfectly used effect.";
	public static List<String> InfoContent = Arrays.asList(
			"&8&l&m=================================="
			,"&8* &aGuild: &7%name% [%tag%]"
			,"&8* &aLeader: &7%player%"
			,"&8* &aMasters: &7%masters%"
			,"&8* &aPoints: &7%points%"
			,"&8* &aKills: &7%kills%"
			,"&8* &aDeaths: &7%deaths%"
			,"&8* &aBorn: &7%born%"
			,"&8* &aPVP: &7%pvp%"
			,"&8* &aMembers: &7%members%"
			,"&8* &aAllies: &7%allies%"
			,"&8&l&m==================================");
	public static List<String> MainContent = Arrays.asList(
			"&8&l&m=================================="
			,"&8* &a/create [tag] [name] &8- &7%desc:create%"
			,"&8* &a/delete &8- &7%desc:delete%"
			,"&8* &a/home &8- &7%desc:home%"
			,"&8* &a/info &8- &7%desc:info%"
			,"&8* &a/invite [player] &8- &7%desc:invite%"
			,"&8* &a/join [tag] &8- &7%desc:join%"
			,"&8* &a/kick [player] &8- &7%desc:kick%"
			,"&8* &a/leader [player] &8- &7%desc:leader%"
			,"&8* &a/leave &8- &7%desc:leave%"
			,"&8* &a/master [player] &8- &7%desc:master%"
			,"&8* &a/pvp &8- &7%desc:pvp%"
			,"&8* &a/sethome &8- &7%desc:sethome%"
			,"&8* &a/title [player] [title] &8- &7%desc:title%"
			,"&8* &a/treasure &8- &7%desc:treasure%"
			,"&8* &a/broadcast [text] &8- &7%desc:broadcast%"
			,"&8* &a/enlarge &8- &7%desc:enlarge%"
			,"&8&l&m==================================");
	public static String UnknownValue = "None";
	public static String BooleanValueFalse = "No";
	public static String BooleanValueTrue = "Yes";
	public static String BooleanValueEnabled = "Enabled";
	public static String BooleanValueDisabled = "Disabled";
	public static String RegionEnter = "&a** Entering %tag% **";
	public static String RegionLeave = "&a** Leaving %tag% **";
	public static String RegionIntruder = "&c** Intruder on guild! (%intruder%) **";
	public static String WarBadTime = "&cYou cant beat this guild!";
	public static String WarBroadcast = "&cGuild %name% [%tag%] has been beated by %attacker%!";
	public static String WarCantBeatOwnGuild = "&cYou cant beat own guild!";
	public static String WarCantBeatOwnAlly = "&cYou cant beat own ally!";
	public static String AntyLogoutInCombat = "&9You are in combat, please dont logout!";
	public static String AntyLogoutNotInCombat = "&9You are not in combat now, you can logout safely!";
	public static String AntyLogoutBroadcast = "&cPlayer %playerTag%%player% (-50) leaves the server in combat!";
	public static String AntyLogoutBroadcastLastAttacker = "&cLast attacker: %playerTag%%player% (+25)";
	
	public static void save() {
		FileConfiguration data = LangFile.get();
		for(Field f : Messages.class.getFields()) {
			if(!data.isSet(f.getName())) {
				try {
					data.set(f.getName(), f.get(f.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			data.save(LangFile.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void load()
	{
		try{
			final FileConfiguration data = LangFile.get();
			for(final Field f : Messages.class.getFields())
			{
				if(data.isSet(f.getName())) {
					if(ListUtils.isList(f)) f.set(null, data.getStringList(f.getName().replace("\\n", "\n")));
					else{
						f.set(null, data.get(f.getName()));
					}
				}
			}
		}catch(Exception e) {
			Logger.log(LogType.ERROR, " [Lang] An error occured with loading messages, disabling...");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
			e.printStackTrace();
		}
	}

}
