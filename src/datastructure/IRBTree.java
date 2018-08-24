package datastructure;

public class IRBTree<T extends Comparable<T>> {
	// 红黑两色
	enum Color {
		RED, BLACK
	}

	// 树节点
	class TreeNode {
		T data;
		TreeNode left;
		TreeNode right;
		TreeNode parent;
		Color color;

		TreeNode(T data) {
			this.data = data;
			this.left = null;
			this.right = null;
			this.parent = null;
			this.color = Color.RED;
		}
	}

	// 红黑树的根节点
	private TreeNode root;

	public IRBTree(T data) {
		this.root = new TreeNode(data);
		this.root.color = Color.BLACK;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	// 调用时，应当从根节点插入
	public void insert(TreeNode treeRoot, TreeNode node) {
		// 插入的节点必须是红节点
		if (node.color != Color.RED)
			node.color = Color.RED;
		if (treeRoot == null)
			return;

		// 遍历，插入的节点大于等于父节点时
		if (treeRoot.data.compareTo(node.data) <= 0) {
			// 把node的父节点暂时设为treeNode
			node.parent = treeRoot;
			// 插入右子树
			// 当treeRoot右节点为空，直接设置此节点
			if (treeRoot.right == null) {
				treeRoot.right = node;
				// 当插入节点的父节点是红节点，即父子节点颜色一样时，需要调整树
				if (node.parent.color == Color.RED)
					// 修复父节点，使他成为合法的红黑树
					repairInsert(node);
				return;
			}

			//// 当treeRoot节点不为空，递归插入此节点
			insert(treeRoot.right, node);
		}
		// 遍历，插入的节点小于父节点时
		else {
			// 把node的父节点暂时设为treeNode
			node.parent = treeRoot;
			// 插入左子树
			// 当treeRoot左节点为空，直接设置此节点
			if (treeRoot.left == null) {
				treeRoot.left = node;
				// 当插入节点的父节点是红节点，即父子节点颜色一样时，需要调整树
				if (node.parent.color == Color.RED)
					// 修复父节点，使他成为合法的红黑树
					repairInsert(node);
				return;
			}
			//// 当treeRoot节点不为空，递归插入此节点
			insert(treeRoot.left, node);
		}
	}

	// 插入节点时进行修复
	private void repairInsert(TreeNode node) {
		// 当node父节点是根节点，必然是不需要修复的
		if (node == null || node.parent == root || node.parent == null || node == root)
			return;
		// 仅当父子节点都为红色时需要修复，否则不需要修复
		if (!(node.parent.color == Color.RED && node.color == Color.RED))
			return;

		TreeNode parent = node.parent;
		TreeNode grandParent = parent.parent;

		// 获取叔父节点
		TreeNode uncle = parent == grandParent.right ? grandParent.left : grandParent.right;

		// case1 叔父节点为红节点
		// 将父节点和叔父节点的颜色和父节点的父节点对调
		if (uncle != null && uncle.color == Color.RED) {
			uncle.color = Color.BLACK;
			parent.color = Color.BLACK;
			if (grandParent != root)
				grandParent.color = Color.RED;
			repairInsert(grandParent);
			return;
		}

		// case2 叔父节点为空，子节点，父节点，爷爷节点连成一线
		// node在爷爷节点的右子树的情况下
		// 父节点左旋，父节点和爷爷节点互换颜色
		if ((uncle == null || (uncle != null && uncle.color == Color.BLACK)) && node == parent.right
				&& parent == grandParent.right) {
			// 父节点代替爷爷节点位置
			parent.parent = grandParent.parent;
			if (grandParent.parent != null) // 一般的
				if (grandParent == grandParent.left) {
					grandParent.parent.left = parent;
				} else {
					grandParent.parent.right = parent;
				}
			else
				this.root = parent; // 如果爷爷节点为空，说明父节点成为了根节点

			// 父节点的左节点赋给爷爷节点的右节点
			grandParent.right = parent.left;
			if (parent.left != null)
				grandParent.right.parent = grandParent;

			// 父节点的左节点变为爷爷节点
			parent.left = grandParent;
			grandParent.parent = parent;

			// 互换父节点和爷爷节点的颜色
			parent.color = Color.BLACK;
			grandParent.color = Color.RED;

			return;
		}
		// node在爷爷节点的左子树的情况下
		// 父节点右旋，父节点和爷爷节点互换颜色
		else if ((uncle == null || (uncle != null && uncle.color == Color.BLACK)) && node == parent.left
				&& parent == grandParent.left) {
			// 父节点代替爷爷节点位置
			parent.parent = grandParent.parent;
			if (grandParent.parent != null) // 一般的
				if (grandParent == grandParent.left) {
					grandParent.parent.left = parent;
				} else {
					grandParent.parent.right = parent;
				}
			else
				this.root = parent; // 如果爷爷节点为空，说明父节点成为了根节点

			// 父节点的右节点赋给爷爷节点的左节点
			grandParent.left = parent.right;
			if (parent.left != null)
				grandParent.left.parent = grandParent; // 别忘了更新父节点

			// 父节点的右节点变为爷爷节点
			parent.right = grandParent;
			grandParent.parent = parent;

			// 互换父节点和爷爷节点的颜色
			parent.color = Color.BLACK;
			grandParent.color = Color.RED;

			return;
		}

		// case3 叔父节点为空，子节点，父节点，爷爷节点不在一条直线上
		// 当父节点是爷爷节点的右节点，且子节点为父节点的左节点
		// 子节点右旋，变成case2
		if ((uncle == null || (uncle != null && uncle.color == Color.BLACK)) && parent == grandParent.right
				&& node == parent.left) {
			// 子节点代替父节点的位置
			node.parent = grandParent;
			grandParent.right = node;
			// 父节点作为子节点的右节点
			node.right = parent;
			parent.parent = node;

			repairInsert(parent);
			return;
		}
		// 当父节点是爷爷节点的左节点，且子节点为父节点的右节点
		// 子节点左旋，变成case2
		else if ((uncle == null || (uncle != null && uncle.color == Color.BLACK)) && parent == grandParent.left
				&& node == parent.right) {
			// 子节点代替父节点的位置
			node.parent = grandParent;
			grandParent.left = node;
			// 父节点作为子节点的右节点
			node.left = parent;
			parent.parent = node;

			repairInsert(parent);
			return;
		}
	}

	// 测试用，先序遍历打印红黑树
	public void printTree(TreeNode root) {
		if (root.parent == null)
			System.out.println(root.data + " : " + root.color);
		else
			System.out.println(root.data + " : " + root.color + " from: " + root.parent.data);
		if (root.left != null) {
			printTree(root.left);
		}
		if (root.right != null) {
			printTree(root.right);
		}
	}

	// 测试
	public static void main(String[] args) {
		IRBTree<Integer> tree = new IRBTree<>(1);
		for (int i = 2; i != 10; i++) {
			System.out.println("插入数字" + i + ":");
			tree.insert(tree.getRoot(), tree.new TreeNode(i));
			tree.printTree(tree.getRoot());

		}
		// 计算结果
		// 插入数字2:
		// 1 : BLACK
		// 2 : RED from: 1
		// 插入数字3:
		// 2 : BLACK
		// 1 : RED from: 2
		// 3 : RED from: 2
		// 插入数字4:
		// 2 : BLACK
		// 1 : BLACK from: 2
		// 3 : BLACK from: 2
		// 4 : RED from: 3
		// 插入数字5:
		// 2 : BLACK
		// 1 : BLACK from: 2
		// 4 : BLACK from: 2
		// 3 : RED from: 4
		// 5 : RED from: 4
		// 插入数字6:
		// 2 : BLACK
		// 1 : BLACK from: 2
		// 4 : RED from: 2
		// 3 : BLACK from: 4
		// 5 : BLACK from: 4
		// 6 : RED from: 5
		// 插入数字7:
		// 2 : BLACK
		// 1 : BLACK from: 2
		// 4 : RED from: 2
		// 3 : BLACK from: 4
		// 6 : BLACK from: 4
		// 5 : RED from: 6
		// 7 : RED from: 6
		// 插入数字8:
		// 4 : BLACK
		// 2 : RED from: 4
		// 1 : BLACK from: 2
		// 3 : BLACK from: 2
		// 6 : RED from: 4
		// 5 : BLACK from: 6
		// 7 : BLACK from: 6
		// 8 : RED from: 7
		// 插入数字9:
		// 4 : BLACK
		// 2 : RED from: 4
		// 1 : BLACK from: 2
		// 3 : BLACK from: 2
		// 6 : RED from: 4
		// 5 : BLACK from: 6
		// 8 : BLACK from: 6
		// 7 : RED from: 8
		// 9 : RED from: 8
	}

}
