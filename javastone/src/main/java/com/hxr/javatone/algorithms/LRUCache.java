package com.hxr.javatone.algorithms;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <pre>
 * Design and implement a data structure for Least Recently Used (LRU) cache. It
 * should support the following operations: get and set.
 * 
 * get(key) - Get the value (will always be positive) of the key if the key
 * exists in the cache, otherwise return -1. set(key, value) - Set or insert the
 * value if the key is not already present. When the cache reached its capacity,
 * it should invalidate the least recently used item before inserting a new
 * item.
 * 
 * <pre>
 * <b>双向链表加哈希表 </b> <br>
 * <b>复杂度 </b> <br>
 * 时间 Get O(1) Set O(1) 空间 O(N) <br>
 * <b>思路</b> <br>
 * 缓存讲究的就是快，所以我们必须做到O(1)的获取速度，这样看来只有哈希表可以胜任。但是哈希表无序的，我们没办法在缓存满时，将最早更新的元素给删去。
 * 那么是否有一种数据结构，可以将先进的元素先出呢？那就是队列。所以我们将元素存在队列中，并用一个哈希表记录下键值和元素的映射，就可以做到O(1)获取速度，
 * 和先进先出的效果。然而，当我们获取一个元素时，还需要把这个元素再次放到队列头，这个元素可能在队列的任意位置，可是队列并不支持对任意位置的增删操作。
 * 而最适合对任意位置增删操作的数据结构又是什么呢？是链表。我可以用链表来实现一个队列，这样就同时拥有链表和队列的特性了。不过，如果仅用单链表的话，
 * 在任意位置删除一个节点还是很麻烦的，要么记录下该节点的上一个节点，要么遍历一遍。所以双向链表是最好的选择。我们用双向链表实现一个队列用来记录每个元素的顺序
 * ，用一个哈希表来记录键和值的关系，就行了。
 * 
 * <pre>
 * 
 * <pre>
 * 
 * @author hanxirui 一个用哈希表和双向链表实现的LRU缓存 Least Recently Used
 */
public class LRUCache {

	int size;
	int capacity;
	ListNode tail;
	ListNode head;
	Map<Integer, ListNode> map;

	public LRUCache(int capacity) {
		this.head = new ListNode(-1, -1);
		this.tail = new ListNode(-1, -1);
		head.next = tail;
		tail.prev = head;
		this.size = 0;
		this.capacity = capacity;
		this.map = new HashMap<Integer, ListNode>();
	}

	public int get(int key) {
		ListNode n = map.get(key);
		if (n != null) {
			moveToHead(n);
			return n.val;
		} else {
			return -1;
		}
	}

	public void set(int key, int value) {
		ListNode n = map.get(key);
		if (n == null) {
			n = new ListNode(value, key);
			attachToHead(n);
			size++;
		} else {
			n.val = value;
			moveToHead(n);
		}
		// 如果更新节点后超出容量，删除最后一个
		if (size > capacity) {
			removeLast();
			size--;
		}
		map.put(key, n);
	}

	// 将一个孤立节点放到头部
	private void attachToHead(ListNode n) {
		n.next = head.next;
		n.next.prev = n;
		head.next = n;
		n.prev = head;
	}

	// 将一个链表中的节点放到头部
	private void moveToHead(ListNode n) {
		n.prev.next = n.next;
		n.next.prev = n.prev;
		attachToHead(n);
	}

	// 移出链表最后一个节点
	private void removeLast() {
		ListNode last = tail.prev;
		last.prev.next = tail;
		tail.prev = last.prev;
		map.remove(last.key);
	}

	public class ListNode {
		ListNode prev;
		ListNode next;
		int val;
		int key;

		public ListNode(int v, int k) {
			this.val = v;
			this.prev = null;
			this.next = null;
			this.key = k;
		}
	}
}
