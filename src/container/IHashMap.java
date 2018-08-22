package container;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class IHashMap<K, V> implements Map<K, V> {
	private static final float initialLoadFactor = 0.75F;// 默认装载系数
	private static final int initialCapacity = 1 << 4; // 初始的容量 16
	private static final int maxCapacity = 1 << 30; //
	private float loadFactor = initialLoadFactor;
	private int capacity; // 表容量
	private int size; // 表大小
	private int threshold; // 阈值,超过此阈值则需要扩容
	// 初始化表
	@SuppressWarnings("unchecked")
	private Node<K, V>[] table = (Node<K, V>[]) new Node[initialCapacity];

	class Node<K, V> implements Map.Entry<K, V> {
		K key;
		V value;
		int hashCode;
		Node<K, V> next;

		Node(K key, V value) {
			this.key = key;
			this.value = value;
			this.hashCode = hash(key);
			this.next = null;
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V value) {
			this.value = value;
			return this.value;
		}
	}

	// 关键设计
	// 对大于256的hashcode，采用前16位和后16位异或的方法来适应数组大小，减少冲突
	private int hash(Object key) {
		if (key == null)
			return 0;
		int hashcode = Objects.hashCode(key);
		if (hashcode < (1 << 16))
			return hashcode;
		else
			return hashcode ^ (hashcode >>> 16); // 前16位和后16位异或

	}

	@SuppressWarnings("unchecked")
	private Node<K, V>[] resize() {
		// size == 0，需要对table进行初始化
		if (size == 0) {
			capacity = initialCapacity;
			threshold = (int) (capacity * loadFactor);
			table = new Node[capacity];
			return table;
		}
		// size <= threshold ，不需要扩容
		if (size <= threshold) {
			return table;
		}
		// (size > threshold) 的情况下
		else {
			// 考虑极端情况，table已经扩容到极限了
			if (capacity > maxCapacity) {
				threshold = Integer.MAX_VALUE;
				return table;
			}
			// 考虑普通情况下cap值的设定
			else {
				int oldCapacity = capacity;
				// 如果cap扩大一倍之后，乘以装载系数后，小于size，则需要扩大两倍
				if (size > ((capacity << 1) * loadFactor)) {
					capacity <<= 2;
				}
				// 更一般的，扩大一倍即可
				else
					capacity <<= 1;
				threshold = (int) (capacity * loadFactor);
				// 建立新的table
				Node<K, V>[] newTab = new Node[capacity];
				// 遍历旧的table中的表项
				for (Node<K, V> node : table) {
					// 忽略空节点
					if (node == null)
						continue;
					// 对没有形成链表的节点，直接投射到新table中的相应位置
					if (node.next == null) {
						newTab[(capacity - 1) & node.hashCode] = node;
					}
					// 对于形成了链表的节点进行处理
					else {
						// 将扩容后下标是否变化作为扩容时元素分配的依据，而不是利用(capacity - 1) & node.hashCode，后者会消耗大量时间，严重影响性能
						// 对于扩容后下标不变的元素，设置一个链表unchanged
						Node<K, V> unchangedHead = null, unchangedTail = null;
						// 对于扩容后下标变化的元素，设置一个链表changed
						Node<K, V> changedHead = null, changedTail = null;

						// 对node的链表进行遍历，将其加入到unchanged或者changed链表中
						while (node != null) {
							// node & oldCapacity == 0时,表明扩容后元素位置没有发生变化
							if ((node.hashCode & oldCapacity) == 0) {
								// 需要初始化的情况下，初始化链表unchanged
								if (unchangedHead == null) {
									unchangedHead = node;
									unchangedTail = unchangedHead;
								}
								// 更一般的，在链表尾部添加node节点
								else {
									// 链表尾部续上node节点
									unchangedTail.next = node;
									// 更新链表尾部的位置，将链表尾部的next设置为空
									unchangedTail = node;
									unchangedTail.next = null;
								}

							}

							// node & oldCapacity != 0时,表明扩容后元素位置发生了变化
							else {
								// 需要初始化的情况下，初始化链表changed
								if (changedHead == null) {
									changedHead = node;
									changedTail = changedHead;
								}
								// 更一般的，在链表尾部添加node节点
								else {
									// 链表尾部续上node节点
									changedTail.next = node;
									// 更新链表尾部的位置，将链表尾部的next设置为空
									changedTail = node;
									changedTail.next = null;
								}

							}
							// 遍历链表
							node = node.next;
						}

						// 将unchangedHead链表加入新表的老位置
						if (unchangedHead != null) {
							newTab[(oldCapacity - 1) & unchangedHead.hashCode] = unchangedHead;
						}
						// 将changedHead链表加入新表的新位置
						if (changedHead != null) {
							newTab[((oldCapacity - 1) & unchangedHead.hashCode) + capacity] = changedHead;
						}

					}
				}
				return table = newTab;
			}

		}

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
		Node<K, V> node = new Node<K, V>(key, value);
		// 对table进行一些适当的修正
		table = resize();
		// 第一次向table中加入数据
		if (size == 0) {
			table[(capacity - 1) & node.hashCode] = node;
			size++;
		}
		// 更一般的，向table中加入数据时，处理冲突
		else {
			Node<K, V> oldNode = table[(capacity - 1) & node.hashCode];
			if (oldNode == null) {
				table[(capacity - 1) & node.hashCode] = node;
				size++;
			} else {
				//遍历链表
				Node<K, V> e = oldNode;
				while (e != null) {
					//判别两个节点的key是否相等并且哈希值是否相等，如果相等，视为重复节点，不予插入
					if (e.hashCode == node.hashCode
							&& ((e.key == node.key) || (node.key != null && node.key.equals(e.key))))
						return e.value;
					//如果到了链表结尾，直接把节点续上
					if(e.next == null) {
						e.next = node;
					}
					e = e.next;
				}
			}
		}
		return node.value;
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
