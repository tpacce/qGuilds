package pl.za.xvacuum.guilds.utils.reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
 
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ReflectionUtils {
	
	private static String _versionString;
	
    public static void sendPacketRadius(Location loc, int radius, Object packet) {
        for (Player p : loc.getWorld().getPlayers()) {
            if (loc.distanceSquared(p.getLocation()) < (radius * radius)) {
                sendPacket(p, packet);
            }
        }
    }
 
    public static void sendPacket(List<Player> players, Object packet) {
        for (Player p : players) {
            sendPacket(p, packet);
        }
    }
 
    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = getHandle(p);
            Field con_field = nmsPlayer.getClass().getField("playerConnection");
            Object con = con_field.get(nmsPlayer);
            Method packet_method = getMethod(con.getClass(), "sendPacket");
            packet_method.invoke(con, packet);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
 
    public static Class<?> getCraftClass(String ClassName) {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        String className = "net.minecraft.server." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
 
    public static Object getHandle(Entity entity) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(entity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return nms_entity;
    }
 
    public static Field getField(Class<?> cl, String field_name) {
        try {
            Field field = cl.getDeclaredField(field_name);
            return field;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)
                    && ClassListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }
 
    public static Method getMethod(Class<?> cl, String method, Integer args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)
                    && args.equals(Integer.valueOf(m.getParameterTypes().length))) {
                return m;
            }
        }
        return null;
    }
 
    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
 
    public static void setValue(Object instance, String fieldName, Object value)
            throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
 
    public static Object getValue(Object instance, String fieldName)
            throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
 
    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
 
        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
 
        return equal;
    }
    


	public synchronized static String getVersion() {
		if(_versionString == null){
			if(Bukkit.getServer() == null){
				return null;
			}
			String name = Bukkit.getServer().getClass().getPackage().getName();
			_versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
		}
		
		return _versionString;
	}


	private static final Map<String, Class<?>> _loadedNMSClasses = new HashMap<String, Class<?>>();

	private static final Map<String, Class<?>> _loadedOBCClasses = new HashMap<String, Class<?>>();
	

	public synchronized static Class<?> getNMSClass(String className) {
		if(_loadedNMSClasses.containsKey(className)){
			return _loadedNMSClasses.get(className);
		}
		
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
			_loadedNMSClasses.put(className, null);
			return null;
		}
		_loadedNMSClasses.put(className, clazz);
		return clazz;
	}


	public synchronized static Class<?> getOBCClass(String className) {
		if(_loadedOBCClasses.containsKey(className)){
			return _loadedOBCClasses.get(className);
		}
		
		String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
			_loadedOBCClasses.put(className, null);
			return null;
		}
		_loadedOBCClasses.put(className, clazz);
		return clazz;
	}


	public synchronized static Object getHandleWrapper(Object obj) {
		try {
			return getMethod(obj.getClass(), "getHandle").invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static final Map<Class<?>, Map<String, Field>> _loadedFields = new HashMap<Class<?>, Map<String, Field>>();
	
	public synchronized static Field getFieldWrapper(Class<?> clazz, String name) {
		Map<String, Field> loaded;
		if(!_loadedFields.containsKey(clazz)){
			loaded = new HashMap<String, Field>();
			_loadedFields.put(clazz, loaded);
		}else{
			loaded = _loadedFields.get(clazz);
		}
		if(loaded.containsKey(name)){
			return loaded.get(name);
		}
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			loaded.put(name, field);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
			loaded.put(name, null);
			return null;
		}
	}


	private static final Map<Class<?>, Map<String, Map<ArrayWrapper<Class<?>>, Method>>> _loadedMethods = new HashMap<Class<?>, Map<String, Map<ArrayWrapper<Class<?>>, Method>>>();
	

	public synchronized static Method getMethodWrapper(Class<?> clazz, String name, Class<?>... args) {
		if(!_loadedMethods.containsKey(clazz)){
			_loadedMethods.put(clazz, new HashMap<String, Map<ArrayWrapper<Class<?>>, Method>>());
		}
		
		Map<String, Map<ArrayWrapper<Class<?>>, Method>> loadedMethodNames = _loadedMethods.get(clazz);
		if(!loadedMethodNames.containsKey(name)){
			loadedMethodNames.put(name, new HashMap<ArrayWrapper<Class<?>>, Method>());
		}
		
		Map<ArrayWrapper<Class<?>>, Method> loadedSignatures = loadedMethodNames.get(name);
		ArrayWrapper<Class<?>> wrappedArg = new ArrayWrapper<Class<?>>(args);
		if(loadedSignatures.containsKey(wrappedArg)){
			return loadedSignatures.get(wrappedArg);
		}
		
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name) && Arrays.equals(args, m.getParameterTypes())) {
				m.setAccessible(true);
				loadedSignatures.put(wrappedArg, m);
				return m;
			}
		loadedSignatures.put(wrappedArg, null);
		return null;
	}

}
