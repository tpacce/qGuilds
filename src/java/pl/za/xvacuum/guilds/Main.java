package pl.za.xvacuum.guilds;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pl.za.xvacuum.guilds.commands.MainAdminCommand;
import pl.za.xvacuum.guilds.commands.MainCommand;
import pl.za.xvacuum.guilds.commands.admin.DeleteAdminCommand;
import pl.za.xvacuum.guilds.commands.admin.PlayerAdminCommand;
import pl.za.xvacuum.guilds.commands.admin.ReloadAdminCommand;
import pl.za.xvacuum.guilds.commands.user.AllyCommand;
import pl.za.xvacuum.guilds.commands.user.BroadcastCommand;
import pl.za.xvacuum.guilds.commands.user.CreateCommand;
import pl.za.xvacuum.guilds.commands.user.DeleteCommand;
import pl.za.xvacuum.guilds.commands.user.EffectCommand;
import pl.za.xvacuum.guilds.commands.user.EnlargeCommand;
import pl.za.xvacuum.guilds.commands.user.HomeCommand;
import pl.za.xvacuum.guilds.commands.user.InfoCommand;
import pl.za.xvacuum.guilds.commands.user.InviteCommand;
import pl.za.xvacuum.guilds.commands.user.JoinCommand;
import pl.za.xvacuum.guilds.commands.user.KickCommand;
import pl.za.xvacuum.guilds.commands.user.LeaderCommand;
import pl.za.xvacuum.guilds.commands.user.LeaveCommand;
import pl.za.xvacuum.guilds.commands.user.MasterCommand;
import pl.za.xvacuum.guilds.commands.user.PvpCommand;
import pl.za.xvacuum.guilds.commands.user.SethomeCommand;
import pl.za.xvacuum.guilds.commands.user.TitleCommand;
import pl.za.xvacuum.guilds.commands.user.TopCommand;
import pl.za.xvacuum.guilds.commands.user.TreasureCommand;
import pl.za.xvacuum.guilds.listeners.BlockBreakListener;
import pl.za.xvacuum.guilds.listeners.BlockPlaceListener;
import pl.za.xvacuum.guilds.listeners.DamageListener;
import pl.za.xvacuum.guilds.listeners.EntityExplodeListener;
import pl.za.xvacuum.guilds.listeners.InventoryListener;
import pl.za.xvacuum.guilds.listeners.MoveListener;
import pl.za.xvacuum.guilds.listeners.PlayerChatListener;
import pl.za.xvacuum.guilds.listeners.PlayerDeathListener;
import pl.za.xvacuum.guilds.listeners.PlayerInteractListener;
import pl.za.xvacuum.guilds.listeners.PlayerJoinListener;
import pl.za.xvacuum.guilds.listeners.PlayerQuitListener;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.InviteManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.misc.Tablist;
import pl.za.xvacuum.guilds.misc.files.Folders;
import pl.za.xvacuum.guilds.misc.files.LangFile;
import pl.za.xvacuum.guilds.misc.files.TablistFile;
import pl.za.xvacuum.guilds.mysql.ConnectionSource;
import pl.za.xvacuum.guilds.mysql.data.Data;
import pl.za.xvacuum.guilds.mysql.data.Data.DataType;
import pl.za.xvacuum.guilds.objects.Update;
import pl.za.xvacuum.guilds.utils.EffectUtils;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;
import pl.za.xvacuum.guilds.utils.Util;
import tk.northpl.tab.JoinListener;
import tk.northpl.tab.LeaveListener;
import tk.northpl.tab.TabListHandler;

public class Main extends JavaPlugin{
	
	private static Main instance;
    private        TabListHandler tabListHandler;
    public int UPDATE_TIME;
    public String DEFAULT_HEAD;
    public int TABLIST_PING;
    public boolean DEBUG;
	
	public static final String LINK_DEF = "http://kavz.za.pl/qGuilds/update.txt";
	public static final String AUTHOR = "KaVz";
	public static final String WEBSITE = "http://kavz.za.pl/qGuilds";
	public static final String NAME = "qGuilds";
	public static final String VERSION = "2.50.9";
	
	protected static Update update;
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public void onLoad()
	{
		instance = this;
	}
	
	public Update getUpdate() {
		return update;
	}
	
	public void onEnable()
	{
		Logger.log(LogType.INFO, "Loading guilds...");
		long loadTimeNS = System.nanoTime();
		long loadTimeTick = System.currentTimeMillis();
		if(!getDescription().getAuthors().contains(AUTHOR) || !getDescription().getWebsite().equals(WEBSITE) || !getDescription().getName().equals(NAME)) 
		{
			Logger.log(LogType.ERROR, "Plugin has been disabled due to piracy!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		if(!getDescription().getVersion().equals(VERSION))
		{
			Logger.log(LogType.ERROR, "Ouch oh! You tried to upset version of the plugin! Plugin has been disabled due to falsify data");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		if(Data.globalType().equals(DataType.MYSQL)) {
			Logger.log(LogType.INFO, "  [*] Connecting to MySQL server...");
			Data.createTables();
			new ConnectionSource(
					getConfig().getString("mysql.adress"), 
					getConfig().getString("mysql.username"), 
					getConfig().getString("mysql.password"), 
					getConfig().getString("mysql.database"), 
					getConfig().getInt("mysql.port"));
		}
		Logger.log(LogType.INFO, "  [*] Loading files & lang options");
		saveDefaultConfig();
		LangFile.setup();
		TablistFile.setup();
		Messages.load();
		Messages.save();
		Tablist.load();
		Tablist.save();
		EffectUtils.init();
		Logger.log(LogType.INFO, "     Loaded!");
		
		Logger.log(LogType.INFO, "  [*] Loading commands...");
		Util.registerCommand(new CreateCommand());
		Util.registerCommand(new InfoCommand());
		Util.registerCommand(new DeleteCommand());
		Util.registerCommand(new InviteCommand());
		Util.registerCommand(new JoinCommand());
		Util.registerCommand(new PvpCommand());
		Util.registerCommand(new LeaveCommand());
		Util.registerCommand(new KickCommand());
		Util.registerCommand(new LeaderCommand());
		Util.registerCommand(new MasterCommand());
		Util.registerCommand(new HomeCommand());
		Util.registerCommand(new SethomeCommand());
		Util.registerCommand(new TreasureCommand());
		Util.registerCommand(new TitleCommand());
		Util.registerCommand(new BroadcastCommand());
		Util.registerCommand(new EnlargeCommand());
		Util.registerCommand(new MainCommand());
		Util.registerCommand(new MainAdminCommand());
		Util.registerCommand(new TopCommand());
		Util.registerCommand(new AllyCommand());
		Util.registerCommand(new EffectCommand());
		
		Util.registerCommand(new ReloadAdminCommand());
		Util.registerCommand(new PlayerAdminCommand());
		Util.registerCommand(new DeleteAdminCommand());
		Logger.log(LogType.INFO, "  [*] Loading listeners...");
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new PlayerChatListener(), this);
		pm.registerEvents(new PlayerQuitListener(), this);
		pm.registerEvents(new BlockBreakListener(), this);
		pm.registerEvents(new BlockPlaceListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new MoveListener(), this);
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new EntityExplodeListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new LeaveListener(), this);
		Logger.log(LogType.INFO, "     Loaded!");
		Logger.log(LogType.INFO, "  [*] Starting tasks, threads and tab options...");
        this.loadSettings();
        this.tabListHandler = new TabListHandler();
		Logger.log(LogType.INFO, "  [*] Loading players & guilds...");
		InviteManager.setup();
		if(Data.globalType().equals(DataType.FLAT)) {
			Folders.refresh();
		}
		DataManager.reload(false, true);
		loadTimeNS = System.nanoTime() - loadTimeNS;
		loadTimeTick = System.currentTimeMillis() - loadTimeTick;
		Logger.log(LogType.INFO, "Guilds has been successfuly loaded!");
		Logger.log(LogType.INFO, " Time: " + loadTimeTick + " millis / " + loadTimeNS + " nanoseconds");
		update = new Update(LINK_DEF);
		update.update();
		if(loadTimeTick >= 1500)
		{
			Logger.log(LogType.WARNING, "Your loading time is longest than normal!");
			return;
		}
		if(loadTimeTick >= 3000)
		{
			Logger.log(LogType.WARNING, "Your loading time is very, very long!");
			return;
		}
		if(loadTimeTick >= 13000)
		{
			Logger.log(LogType.ERROR, "System has been unloaded, your loading time is long as f*ck.");
			Logger.log(LogType.ERROR, "Check all files and datas, maybe your files are too big?");
			pm.disablePlugin(this);
			return;
		}
		return;
	}
	
	public void onDisable()
	{
		Data.saveAll();
	}
	
    private void loadSettings()
    {

        this.UPDATE_TIME = Tablist.tablistRefreshTime;
        this.DEFAULT_HEAD = Tablist.tablistHeadNickname;
        this.TABLIST_PING = Tablist.tablistPing;
        this.DEBUG = Tablist.tablistDebug;
        
        
    }

    public TabListHandler getTabListHandler()
    {
        return this.tabListHandler;
    }


}
