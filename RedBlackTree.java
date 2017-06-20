public class RedBlackTree {

	private final int RED = 0;
	private final int BLACK = 1;

	private class Node {

		int key = -1, color = BLACK;
		Node left = nil, right = nil, parent = nil;

		Node(int key) {
			this.key = key;
		}
	}

	private final Node nil = new Node(-1);
	private Node root = nil;

	public void printTree(Node node) {
		if (node == nil) {
			return;
		}
		printTree(node.left);
		System.out.print(((node.color == RED) ? "Color: Red " : "Color: Black ") + "Key: " + node.key + " Parent: "
				+ node.parent.key + "\n");
		printTree(node.right);
	}

	private Node findNode(Node findNode, Node node) {
		if (root == nil) {
			return null;
		}

		if (findNode.key < node.key) {
			if (node.left != nil) {
				return findNode(findNode, node.left);
			}
		} else if (findNode.key > node.key) {
			if (node.right != nil) {
				return findNode(findNode, node.right);
			}
		} else if (findNode.key == node.key) {
			return node;
		}
		return null;
	}

	int insertcount = 0;

	private void insert(Node node) {
		insertcount++;
		Node temp = root;
		if (root == nil) {
			root = node;
			node.color = BLACK;
			node.parent = nil;
		} else {
			node.color = RED;
			while (true) {
				if (node.key < temp.key) {
					if (temp.left == nil) {
						temp.left = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.left;
					}
				} else if (node.key >= temp.key) {
					if (temp.right == nil) {
						temp.right = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.right;
					}
				}
			}
			fixTree(node);
		}
	}

	private void fixTree(Node node) {
		while (node.parent.color == RED) {
			Node uncle = nil;
			if (node.parent == node.parent.parent.left) {
				uncle = node.parent.parent.right;

				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.right) {
					node = node.parent;
					rotateLeft(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				rotateRight(node.parent.parent);
			} else {
				uncle = node.parent.parent.left;
				if (uncle != nil && uncle.color == RED) {
					node.parent.color = BLACK;
					uncle.color = BLACK;
					node.parent.parent.color = RED;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.left) {
					node = node.parent;
					rotateRight(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				rotateLeft(node.parent.parent);
			}
		}
		root.color = BLACK;
	}

	void rotateLeft(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.right;
			} else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent;
			node.parent = node.right;
			if (node.right.left != nil) {
				node.right.left.parent = node;
			}
			node.right = node.right.left;
			node.parent.left = node;
		} else {
			Node right = root.right;
			root.right = right.left;
			right.left.parent = root;
			root.parent = right;
			right.left = root;
			right.parent = nil;
			root = right;
		}
	}

	void rotateRight(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.left;
			} else {
				node.parent.right = node.left;
			}

			node.left.parent = node.parent;
			node.parent = node.left;
			if (node.left.right != nil) {
				node.left.right.parent = node;
			}
			node.left = node.left.right;
			node.parent.right = node;
		} else {// Need to rotate root
			Node left = root.left;
			root.left = root.left.right;
			left.right.parent = root;
			root.parent = left;
			left.right = root;
			left.parent = nil;
			root = left;
		}
	}

	void deleteTree() {
		root = nil;
	}

	void transplant(Node target, Node with) {
		if (target.parent == nil) {
			root = with;
		} else if (target == target.parent.left) {
			target.parent.left = with;
		} else
			target.parent.right = with;
		with.parent = target.parent;
	}

	int deletecount = 0;
	int deletemiss = 0;

	boolean delete(Node z) {
		if ((z = findNode(z, root)) == null) {
			deletemiss++;
			return false;
		}
		deletecount++;
		Node x;
		Node y = z; // temporary reference y
		int y_original_color = y.color;

		if (z.left == nil) {
			x = z.right;
			transplant(z, z.right);
		} else if (z.right == nil) {
			x = z.left;
			transplant(z, z.left);
		} else {
			y = treeMinimum(z.right);
			y_original_color = y.color;
			x = y.right;
			if (y.parent == z)
				x.parent = y;
			else {
				transplant(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}
			transplant(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		if (y_original_color == BLACK)
			deleteFixup(x);
		return true;
	}

	void deleteFixup(Node x) {
		while (x != root && x.color == BLACK) {
			if (x == x.parent.left) {
				Node w = x.parent.right;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateLeft(x.parent);
					w = x.parent.right;
				}
				if (w.left.color == BLACK && w.right.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.right.color == BLACK) {
					w.left.color = BLACK;
					w.color = RED;
					rotateRight(w);
					w = x.parent.right;
				}
				if (w.right.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.right.color = BLACK;
					rotateLeft(x.parent);
					x = root;
				}
			} else {
				Node w = x.parent.left;
				if (w.color == RED) {
					w.color = BLACK;
					x.parent.color = RED;
					rotateRight(x.parent);
					w = x.parent.left;
				}
				if (w.right.color == BLACK && w.left.color == BLACK) {
					w.color = RED;
					x = x.parent;
					continue;
				} else if (w.left.color == BLACK) {
					w.right.color = BLACK;
					w.color = RED;
					rotateLeft(w);
					w = x.parent.left;
				}
				if (w.left.color == RED) {
					w.color = x.parent.color;
					x.parent.color = BLACK;
					w.left.color = BLACK;
					rotateRight(x.parent);
					x = root;
				}
			}
		}
		x.color = BLACK;
	}

	Node treeMinimum(Node subTreeRoot) {
		while (subTreeRoot.left != nil) {
			subTreeRoot = subTreeRoot.left;
		}
		return subTreeRoot;
	}

	int temp, firstnum, secondnum, thirdnum, maxnum = 0;
	int mainnum = 0;

	public void findmaxval(Node r){
    	if(r != null) {
    		findmaxval(r.right);
    		thirdnum = r.key + 1;
    		maxnum = thirdnum;
    	}
    }

	public void printoutputfunc(int x) {
		mainnum = x;
		findmaxval(root);
		firstnum = 0;
		secondnum = 0;
		printoutput(root);
		if (firstnum == 0) {
			System.out.print("NIL" + "\t");
		} else {
			System.out.print(firstnum + "\t");
		}
		
		if (secondnum == 0) {
			System.out.print("NIL" + "\t");
		} else {
			System.out.print(secondnum + "\t");
		}
		
		if (thirdnum == maxnum) {
			System.out.print("NIL" + "\n");
		} else {
			System.out.print(thirdnum + "\n");
		}
	}

	public void printoutput(Node r) {
		if (r != null) {
			printoutput(r.left);
			if (r.key != -1) {
				if (r.key == mainnum) {
					secondnum = r.key;
				} else if (r.key < mainnum && firstnum < r.key) {
					firstnum = r.key;
				} else if (r.key > mainnum && thirdnum > r.key) {
					thirdnum = r.key;
				}
			}
			printoutput(r.right);

		}
	}

	// 트리 보기용
	/*
	 * public void backinorder() { backinorder(root); }
	 * 
	 * private void backinorder(Node node) { if (node != null) {
	 * backinorder(node.right); char c = 'B'; if (node.color == 0) c = 'R'; if
	 * (node.key != -1) { deap(root, node.key); System.out.print(" " +node.key
	 * +""+c+" \n");} backinorder(node.left); } }
	 * 
	 * private void deap(Node node, int val) { while ((node != null)) { int rval
	 * = node.key; if (val < rval) { System.out.print("      "); node =
	 * node.left; } else if (val > rval) { node = node.right;
	 * System.out.print("      "); } else {
	 * 
	 * break; } } }
	 */

	public void inorder() {
		inorder(root);
	}

	private void inorder(Node r) {
		if (r != null) {
			inorder(r.left);
			char c = 'B';
			if (r.color == 0)
				c = 'R';
			if (r.key != -1) {
				System.out.print(r.key + "" + c + "\n");
			}
			inorder(r.right);

		}
	}

	int countblack = 0;

	public int blackcount() {
		blackcount(root);
		return countblack;
	}

	private void blackcount(Node node) {
		if (node != null) {
			blackcount(node.right);
			char c = 'B';
			if (node.color == 0)
				c = 'R';
			if (node.key != -1 && c == 'B') {
				countblack++;
			}
			blackcount(node.left);
		}
	}

	int heightblack = 0;

	public int heightblack() {
		heightblack(root);
		return heightblack;
	}

	private void heightblack(Node node) {
		if (node != null) {
			heightblack(node.right);
			char c = 'B';
			if (node.color == 0)
				c = 'R';
			if (node.key != -1 && c == 'B') {
				heightblack++;
			}
		}
	}

	Node node;
	int nodecount = 0;

	public void testinsert(int linenum) {
		node = new Node(linenum);
		insert(node);
		nodecount++;
	}

	public void testdelete(int linenum) {
		node = new Node(linenum);
		if (delete(node)) {
			nodecount--;
		}
	}

}