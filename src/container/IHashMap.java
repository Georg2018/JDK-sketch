package container;


import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class IHashMap<K, V> implements Map<K, V> {
	private static final float initialLoadFactory = 0.75F;
	private static final int initialCapacity = 1 << 4;
	private float loadFactory = initialLoadFactory;
	private int capacity = initialCapacity; //表容量
	private int size; //表大小
	@SuppressWarnings("unchecked")
	private Node<K,V>[] table = (Node<K,V>[])new Node[capacity];
	
	class IEntry<K, V> implements Map.Entry<K, V>{
		private K key;
		private V value;
		@Override
		public K getKey() {		
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			this.value = value;
			return this.value;
		}
		
	}
	
	class Node<K, V>{
		IEntry<K, V> thisEntry;
		IEntry<K, V> next;
	
	}
	
	//关键设计
	//对大于256的hashcode，采用前16位和后16位异或的方法来适应数组大小，减少冲突
	private int hash(Object key) {
		if(key == null) return 0;
		int hashcode = Objects.hashCode(key);
		if(hashcode < (1 << 16))
			return hashcode;
		else
			return hashcode ^ (hashcode >>> 16); //前16位和后16位异或
		
	}
	
	private void resize() {
		
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
