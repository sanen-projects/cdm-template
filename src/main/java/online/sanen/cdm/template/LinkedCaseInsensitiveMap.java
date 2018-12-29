package online.sanen.cdm.template;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * {@link LinkedHashMap} 以不区分大小写的方式存储字符串键的变体，例如在结果表中基于键的访问。
 * <p>
 * 保留原来的顺序以及原来的键框，同时允许使用任何键的情况下包含、获取和删除调用。
 * <p>
 * 不支持 {@code null} 键.
 *
 * @author LazyToShow <br>
 *         Date: 2018年10月14日 <br>
 *         Time: 下午1:23:53
 */
@SuppressWarnings("serial")
public class LinkedCaseInsensitiveMap<V> implements Map<String, V>, Serializable, Cloneable {

	private final LinkedHashMap<String, V> targetMap;

	private final HashMap<String, String> caseInsensitiveKeys;

	private final Locale locale;

	/**
	 * Create a new LinkedCaseInsensitiveMap that stores case-insensitive keys
	 * according to the default Locale (by default in lower case).
	 * 
	 * @see #convertKey(String)
	 */
	public LinkedCaseInsensitiveMap() {
		this((Locale) null);
	}

	/**
	 * Create a new LinkedCaseInsensitiveMap that stores case-insensitive keys
	 * according to the given Locale (by default in lower case).
	 * 
	 * @param locale the Locale to use for case-insensitive key conversion
	 * @see #convertKey(String)
	 */
	public LinkedCaseInsensitiveMap(Locale locale) {
		this(16, locale);
	}

	/**
	 * Create a new LinkedCaseInsensitiveMap that wraps a {@link LinkedHashMap} with
	 * the given initial capacity and stores case-insensitive keys according to the
	 * default Locale (by default in lower case).
	 * 
	 * @param initialCapacity the initial capacity
	 * @see #convertKey(String)
	 */
	public LinkedCaseInsensitiveMap(int initialCapacity) {
		this(initialCapacity, null);
	}

	/**
	 * Create a new LinkedCaseInsensitiveMap that wraps a {@link LinkedHashMap} with
	 * the given initial capacity and stores case-insensitive keys according to the
	 * given Locale (by default in lower case).
	 * 
	 * @param initialCapacity the initial capacity
	 * @param locale          the Locale to use for case-insensitive key conversion
	 * @see #convertKey(String)
	 */
	public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
		this.targetMap = new LinkedHashMap<String, V>(initialCapacity) {
			@Override
			public boolean containsKey(Object key) {
				return LinkedCaseInsensitiveMap.this.containsKey(key);
			}

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
				boolean doRemove = LinkedCaseInsensitiveMap.this.removeEldestEntry(eldest);
				if (doRemove) {
					caseInsensitiveKeys.remove(convertKey(eldest.getKey()));
				}
				return doRemove;
			}
		};
		this.caseInsensitiveKeys = new HashMap<String, String>(initialCapacity);
		this.locale = (locale != null ? locale : Locale.getDefault());
	}

	/**
	 * Copy constructor.
	 */
	@SuppressWarnings("unchecked")
	private LinkedCaseInsensitiveMap(LinkedCaseInsensitiveMap<V> other) {
		this.targetMap = (LinkedHashMap<String, V>) other.targetMap.clone();
		this.caseInsensitiveKeys = (HashMap<String, String>) other.caseInsensitiveKeys.clone();
		this.locale = other.locale;
	}

	// Implementation of java.util.Map

	public int size() {
		return this.targetMap.size();
	}

	public boolean isEmpty() {
		return this.targetMap.isEmpty();
	}

	public boolean containsKey(Object key) {
		return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String) key)));
	}

	public boolean containsValue(Object value) {
		return this.targetMap.containsValue(value);
	}

	public V get(Object key) {
		if (key instanceof String) {
			String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String) key));
			if (caseInsensitiveKey != null) {
				return this.targetMap.get(caseInsensitiveKey);
			}
		}
		return null;
	}

	public V getOrDefault(Object key, V defaultValue) {
		if (key instanceof String) {
			String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String) key));
			if (caseInsensitiveKey != null) {
				return this.targetMap.get(caseInsensitiveKey);
			}
		}
		return defaultValue;
	}

	public V put(String key, V value) {
		String oldKey = this.caseInsensitiveKeys.put(convertKey(key), key);
		if (oldKey != null && !oldKey.equals(key)) {
			this.targetMap.remove(oldKey);
		}
		return this.targetMap.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends V> map) {
		if (map.isEmpty()) {
			return;
		}
		for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public V remove(Object key) {
		if (key instanceof String) {
			String caseInsensitiveKey = this.caseInsensitiveKeys.remove(convertKey((String) key));
			if (caseInsensitiveKey != null) {
				return this.targetMap.remove(caseInsensitiveKey);
			}
		}
		return null;
	}

	public void clear() {
		this.caseInsensitiveKeys.clear();
		this.targetMap.clear();
	}

	public Set<String> keySet() {
		return this.targetMap.keySet();
	}

	public Collection<V> values() {
		return this.targetMap.values();
	}

	public Set<Entry<String, V>> entrySet() {
		return this.targetMap.entrySet();
	}

	public LinkedCaseInsensitiveMap<V> clone() {
		return new LinkedCaseInsensitiveMap<V>(this);
	}

	public boolean equals(Object obj) {
		return this.targetMap.equals(obj);
	}

	public int hashCode() {
		return this.targetMap.hashCode();
	}

	public String toString() {
		return this.targetMap.toString();
	}

	// Specific to LinkedCaseInsensitiveMap

	/**
	 * Return the locale used by this {@code LinkedCaseInsensitiveMap}. Used for
	 * case-insensitive key conversion.
	 * 
	 * @since 4.3.10
	 * @see #LinkedCaseInsensitiveMap(Locale)
	 * @see #convertKey(String)
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * Convert the given key to a case-insensitive key.
	 * <p>
	 * The default implementation converts the key to lower-case according to this
	 * Map's Locale.
	 * 
	 * @param key the user-specified key
	 * @return the key to use for storing
	 * @see String#toLowerCase(Locale)
	 */
	protected String convertKey(String key) {
		return key.toLowerCase(getLocale());
	}

	/**
	 * Determine whether this map should remove the given eldest entry.
	 * 
	 * @param eldest the candidate entry
	 * @return {@code true} for removing it, {@code false} for keeping it
	 * @see LinkedHashMap#removeEldestEntry
	 */
	protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
		return false;
	}

}