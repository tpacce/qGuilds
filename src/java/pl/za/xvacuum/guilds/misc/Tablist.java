package pl.za.xvacuum.guilds.misc;

import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;

import pl.za.xvacuum.guilds.misc.files.TablistFile;
import pl.za.xvacuum.guilds.utils.ListUtils;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class Tablist {
	
	public static boolean tablistEnabled = true;
	public static int tablistRefreshTime = 20;
	public static String tablistHeader = "&8&l&m---------------------[&2 qGuilds &8&l&m]---------------------";
	public static String tablistFooter = "&8&l&m---------------------------------------------------";
	public static String tablistHeadNickname = "KaVzoo";
	public static int tablistPing = 100;
	public static boolean tablistDebug = false;
	public static String tablistGuildTop = "&c[%points%]";
	
	public static String tabSlot_1 = "&7Guild: &a%tag%";
	public static String tabSlot_2 = "&7Points: &a%points%";
	public static String tabSlot_3 = "&7Kills: &a%kills%";
	public static String tabSlot_4 = "&7Deaths: &a%deaths%";
	public static String tabSlot_5 = "&7Name: &a%name%";
	public static String tabSlot_6 = "&7Born: &a%born%";
	public static String tabSlot_7 = "&7PVP: &a%pvp%";
	public static String tabSlot_8 = "";
	public static String tabSlot_9 = "";
	public static String tabSlot_10 = "";
	public static String tabSlot_11 = "";
	public static String tabSlot_12 = "";
	public static String tabSlot_13 = "";
	public static String tabSlot_14 = "";
	public static String tabSlot_15 = "&cwww.yourpage.com";
	public static String tabSlot_16 = "&cts3.yourpage.com";
	public static String tabSlot_17 = "";
	public static String tabSlot_18 = "&2&lYour Server Name";
	public static String tabSlot_19 = "";
	public static String tabSlot_20 = "";
	
	public static String tabSlot_21 = "";
	public static String tabSlot_22 = "&e&lTOP 15 GUILDS";
	public static String tabSlot_23 = "&81. &7[gtop>1]";
	public static String tabSlot_24 = "&82. &7[gtop>2]";
	public static String tabSlot_25 = "&83. &7[gtop>3]";
	public static String tabSlot_26 = "&84. &7[gtop>4]";
	public static String tabSlot_27 = "&85. &7[gtop>5]";
	public static String tabSlot_28 = "&86. &7[gtop>6]";
	public static String tabSlot_29 = "&87. &7[gtop>7]";
	public static String tabSlot_30 = "&88. &7[gtop>8]";
	public static String tabSlot_31 = "&89. &7[gtop>9]";
	public static String tabSlot_32 = "&810. &7[gtop>10]";
	public static String tabSlot_33 = "&811. &7[gtop>11]";
	public static String tabSlot_34 = "&812. &7[gtop>12]";
	public static String tabSlot_35 = "&813. &7[gtop>13]";
	public static String tabSlot_36 = "&814. &7[gtop>14]";
	public static String tabSlot_37 = "&815. &7[gtop>15]";
	public static String tabSlot_38 = "";
	public static String tabSlot_39 = "";
	public static String tabSlot_40 = "";
	
	public static String tabSlot_41 = "";
	public static String tabSlot_42 = "&e&lTOP 15 PLAYERS";
	public static String tabSlot_43 = "&81. &7[ptop>1]";
	public static String tabSlot_44 = "&82. &7[ptop>2]";
	public static String tabSlot_45 = "&83. &7[ptop>3]";
	public static String tabSlot_46 = "&84. &7[ptop>4]";
	public static String tabSlot_47 = "&85. &7[ptop>5]";
	public static String tabSlot_48 = "&86. &7[ptop>6]";
	public static String tabSlot_49 = "&87. &7[ptop>7]";
	public static String tabSlot_50 = "&88. &7[ptop>8]";
	public static String tabSlot_51 = "&89. &7[ptop>9]";
	public static String tabSlot_52 = "&810. &7[ptop>10]";
	public static String tabSlot_53 = "&811. &7[ptop>11]";
	public static String tabSlot_54 = "&812. &7[ptop>12]";
	public static String tabSlot_55 = "&813. &7[ptop>13]";
	public static String tabSlot_56 = "&814. &7[ptop>14]";
	public static String tabSlot_57 = "&815. &7[ptop>15]";
	public static String tabSlot_58 = "";
	public static String tabSlot_59 = "";
	public static String tabSlot_60 = "";
	
	public static String tabSlot_61 = "";
	public static String tabSlot_62 = "&e&lUSERS FROM GUILD";
	public static String tabSlot_63 = "&7[online>0]";
	public static String tabSlot_64 = "&7[online>1]";
	public static String tabSlot_65 = "&7[online>2]";
	public static String tabSlot_66 = "&7[online>3]";
	public static String tabSlot_67 = "&7[online>4]";
	public static String tabSlot_68 = "&7[online>5]";
	public static String tabSlot_69 = "&7[online>6]";
	public static String tabSlot_70 = "&7[online>7]";
	public static String tabSlot_71 = "&7[online>8]";
	public static String tabSlot_72 = "&7[online>9]";
	public static String tabSlot_73 = "&7[online>10]";
	public static String tabSlot_74 = "&7[online>11]";
	public static String tabSlot_75 = "&7[online>12]";
	public static String tabSlot_76 = "&7[online>13]";
	public static String tabSlot_77 = "&7[online>14]";
	public static String tabSlot_78 = "&7[online>15]";
	public static String tabSlot_79 = "&7[online>16]";
	public static String tabSlot_80 = "";
	
	public static void save() {
		FileConfiguration data = TablistFile.get();
		for(Field f : Tablist.class.getFields()) {
			if(!data.isSet(f.getName())) {
				try {
					data.set(f.getName(), f.get(f.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			data.save(TablistFile.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void load()
	{
		try{
			final FileConfiguration data = TablistFile.get();
			for(final Field f : Tablist.class.getFields())
			{
				if(data.isSet(f.getName())) {
					if(ListUtils.isList(f)) f.set(null, data.getStringList(f.getName().replace("\\n", "\n")));
					else{
						f.set(null, data.get(f.getName()));
					}
				}
			}
		}catch(Exception e) {
			Logger.log(LogType.ERROR, " [Tablist] An error occured with loading tablist file...");
			e.printStackTrace();
		}
	}

}
