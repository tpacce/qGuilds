package pl.za.xvacuum.guilds.utils.reflections;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleReflection {
	
	private Class<?> packetTitle;
	private Class<?> packetActions;
	private Class<?> nmsChatSerializer;
	private Class<?> chatBaseComponent;
	private String title = "";
	private ChatColor titleColor = ChatColor.WHITE;
	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	private int fadeInTime = -1;
	private int stayTime = -1;
	private int fadeOutTime = -1;
	private boolean ticks = false;

	public TitleReflection(String title) {
		this.title = title;
		loadClasses();
	}

	public TitleReflection(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
		loadClasses();
	}

	public TitleReflection(TitleReflection title) {
		this.title = title.title;
		this.subtitle = title.subtitle;
		this.titleColor = title.titleColor;
		this.subtitleColor = title.subtitleColor;
		this.fadeInTime = title.fadeInTime;
		this.fadeOutTime = title.fadeOutTime;
		this.stayTime = title.stayTime;
		this.ticks = title.ticks;
		loadClasses();
	}

	public TitleReflection(String title, String subtitle, int fadeInTime, int stayTime,
			int fadeOutTime) {
		this.title = title;
		this.subtitle = subtitle;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTime = fadeOutTime;
		loadClasses();
	}

	private void loadClasses() {
		packetTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle");
		packetActions = ReflectionUtils.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
		chatBaseComponent = ReflectionUtils.getNMSClass("IChatBaseComponent");
		nmsChatSerializer = ReflectionUtils.getNMSClass("IChatBaseComponent$ChatSerializer");
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public void setTitleColor(ChatColor color) {
		this.titleColor = color;
	}

	public void setSubtitleColor(ChatColor color) {
		this.subtitleColor = color;
	}

	public void setFadeInTime(int time) {
		this.fadeInTime = time;
	}

	public void setFadeOutTime(int time) {
		this.fadeOutTime = time;
	}

	public void setStayTime(int time) {
		this.stayTime = time;
	}

	public void setTimingsToTicks() {
		ticks = true;
	}

	public void setTimingsToSeconds() {
		ticks = false;
	}

	public void send(Player player) {
		if (packetTitle != null) {
			resetTitle(player);
			try {
				Object handle = ReflectionUtils.getHandle(player);
				Object connection = ReflectionUtils.getField(handle.getClass(),
						"playerConnection").get(handle);
				Object[] actions = packetActions.getEnumConstants();
				Method sendPacket = ReflectionUtils.getMethod(connection.getClass(),
						"sendPacket");
				Object packet = packetTitle.getConstructor(packetActions,
						chatBaseComponent, Integer.TYPE, Integer.TYPE,
						Integer.TYPE).newInstance(actions[2], null,
						fadeInTime * (ticks ? 1 : 20),
						stayTime * (ticks ? 1 : 20),
						fadeOutTime * (ticks ? 1 : 20));
				if (fadeInTime != -1 && fadeOutTime != -1 && stayTime != -1)
					sendPacket.invoke(connection, packet);

				Object serialized = ReflectionUtils.getMethodWrapper(nmsChatSerializer, "a",
						String.class).invoke(
						null,
						"{text:\""
								+ ChatColor.translateAlternateColorCodes('&',
										title) + "\",color:"
								+ titleColor.name().toLowerCase() + "}");
				packet = packetTitle.getConstructor(packetActions,
						chatBaseComponent).newInstance(actions[0], serialized);
				sendPacket.invoke(connection, packet);
				if (subtitle != "") {
					serialized = ReflectionUtils.getMethodWrapper(nmsChatSerializer, "a", String.class)
							.invoke(null,
									"{text:\""
											+ ChatColor
													.translateAlternateColorCodes(
															'&', subtitle)
											+ "\",color:"
											+ subtitleColor.name()
													.toLowerCase() + "}");
					packet = packetTitle.getConstructor(packetActions,
							chatBaseComponent).newInstance(actions[1],
							serialized);
					sendPacket.invoke(connection, packet);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void broadcast() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			send(p);
		}
	}

	public void clearTitle(Player player) {
		try {
			Object handle = ReflectionUtils.getHandle(player);
			Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection")
					.get(handle);
			Object[] actions = packetActions.getEnumConstants();
			Method sendPacket = ReflectionUtils.getMethod(connection.getClass(), "sendPacket");
			Object packet = packetTitle.getConstructor(packetActions,
					chatBaseComponent).newInstance(actions[3], null);
			sendPacket.invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resetTitle(Player player) {
		try {
			Object handle = ReflectionUtils.getHandle(player);
			Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection")
					.get(handle);
			Object[] actions = packetActions.getEnumConstants();
			Method sendPacket = ReflectionUtils.getMethod(connection.getClass(), "sendPacket");
			Object packet = packetTitle.getConstructor(packetActions,
					chatBaseComponent).newInstance(actions[4], null);
			sendPacket.invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
