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
		if (node == null || node.parent == root)
			return;
		// 仅当父子节点都为红色时需要修复，否则不需要修复
		if (!(node.parent.color == Color.RED && node.color == Color.RED))
			return;

		TreeNode parent = node.parent;
		TreeNode grandParent = parent.parent;

		// 获取叔父节点
		TreeNode uncle = parent == grandParent.right ? grandParent.left : grandParent.right;

		// case1 叔父节点为红节点（等价于叔父节点不为空）
		// 将父节点和叔父节点的颜色和父节点的父节点对调
		if (uncle != null && uncle.color == Color.RED) {
			uncle.color = Color.BLACK;
			parent.color = Color.BLACK;
			if (grandParent != root)
				grandParent.color = Color.RED;
			repairInsert(parent);
		}

		// case2 叔父节点为空，子节点，父节点，爷爷节点连成一线
		// node在爷爷节点的右子树的情况下
		// 父节点左旋，父节点和爷爷节点互换颜色
		if (uncle == null && node == parent.right && parent == grandParent.right) {
			// 父节点代替爷爷节点位置
			parent.parent = grandParent.parent;
			if (grandParent.parent != null)  //一般的
				if (grandParent == grandParent.left) {
					grandParent.parent.left = parent;
				} else {
					grandParent.parent.right = parent;
				}
			else this.root = parent; //如果爷爷节点为空，说明父节点成为了根节点
			
			// 父节点的左节点赋给爷爷节点的右节点
			grandParent.right = parent.left;

			// 父节点的左节点变为爷爷节点
			parent.left = grandParent;
			grandParent.parent = parent;

			// 互换父节点和爷爷节点的颜色
			parent.color = Color.BLACK;
			grandParent.color = Color.RED;

		}
		// node在爷爷节点的左子树的情况下
		// 父节点右旋，父节点和爷爷节点互换颜色
		else if (uncle == null && node == parent.left && parent == grandParent.left) {
			// 父节点代替爷爷节点位置
			parent.parent = grandParent.parent;
			if (grandParent.parent != null) //一般的
				if (grandParent == grandParent.left) {
					grandParent.parent.left = parent;
				} else {
					grandParent.parent.right = parent;
				}
			else this.root = parent; //如果爷爷节点为空，说明父节点成为了根节点
			
			// 父节点的右节点赋给爷爷节点的左节点
			grandParent.left = parent.right;

			// 父节点的右节点变为爷爷节点
			parent.right = grandParent;
			grandParent.parent = parent;

			// 互换父节点和爷爷节点的颜色
			parent.color = Color.BLACK;
			grandParent.color = Color.RED;
		}

		// case3 叔父节点为空，子节点，父节点，爷爷节点不在一条直线上
		// 当父节点是爷爷节点的右节点，且子节点为父节点的左节点
		// 子节点右旋，变成case2
		if (uncle == null && parent == grandParent.right && node == parent.left) {
			// 子节点代替父节点的位置
			node.parent = grandParent;
			grandParent.right = node;
			// 父节点作为子节点的右节点
			node.right = parent;
			parent.parent = node;
			parent.left = null;

			repairInsert(parent);
		}
		// 当父节点是爷爷节点的左节点，且子节点为父节点的右节点
		// 子节点左旋，变成case2
		else if (uncle == null && parent == grandParent.left && node == parent.right) {
			// 子节点代替父节点的位置
			node.parent = grandParent;
			grandParent.left = node;
			// 父节点作为子节点的右节点
			node.left = parent;
			parent.parent = node;
			parent.right = null;

			repairInsert(parent);
		}
	}
	//测试用，先序遍历打印红黑树
	public void printTree(TreeNode root) {
		System.out.println(root.data + " : " + root.color);
		if (root.left != null) {
			printTree(root.left);
		}
		if (root.right != null) {
			printTree(root.right);
		}
	}
	
	//测试
	public static void main(String[] args) {
		IRBTree<Integer> tree = new IRBTree<>(1);
		for (int i = 2; i != 10; i++) {
			tree.insert(tree.getRoot(), tree.new TreeNode(i));
			tree.printTree(tree.getRoot());
			System.out.println("");
		}
		// 计算结果
		// 1 : BLACK
		// 2 : RED
		//
		// 2 : BLACK
		// 1 : RED
		// 3 : RED
		//
		// 2 : BLACK
		// 1 : BLACK
		// 3 : BLACK
		// 4 : RED
		//
		// 2 : BLACK
		// 1 : BLACK
		// 4 : BLACK
		// 3 : RED
		// 5 : RED
		//
		// 2 : BLACK
		// 1 : BLACK
		// 4 : RED
		// 3 : BLACK
		// 5 : BLACK
		// 6 : RED
		//
		// 2 : BLACK
		// 1 : BLACK
		// 4 : RED
		// 3 : BLACK
		// 6 : BLACK
		// 5 : RED
		// 7 : RED
		//
		// 2 : BLACK
		// 1 : BLACK
		// 4 : RED
		// 3 : BLACK
		// 6 : RED
		// 5 : BLACK
		// 7 : BLACK
		// 8 : RED
		//
		// 2 : BLACK
		// 1 : BLACK
		// 4 : RED
		// 3 : BLACK
		// 6 : RED
		// 5 : BLACK
		// 8 : BLACK
		// 7 : RED
		// 9 : RED
	}

}
