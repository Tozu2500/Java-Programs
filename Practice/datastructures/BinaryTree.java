package com.tozu.datastructures;

class Node {
	int value;
	Node left, right;
	
	Node(int value) {
		this.value = value;
		left = right = null;
	}
}

public class BinaryTree {
	
	Node root;
	
	public void insert(int value) {
		root = insertRec(root, value);
	}
	
	private Node insertRec(Node root, int value) {
		if (root == null) {
			root = new Node(value);
			return root;
		}
		if (value < root.value) {
			root.left = insertRec(root.left, value);
		} else if (value > root.value) {
			root.right = insertRec(root.right, value);
		}
		return root;
	}
	
	public void inorder() {
		inorderRec(root);
		System.out.println();
	}
	
	public void inorderRec(Node root) {
		if (root != null) {
			inorderRec(root.left);
			System.out.print(root.value + " ");
			inorderRec(root.right);
		}
	}
	
	public void preorder() {
		preorderRec(root);
		System.out.println();
	}
	
	public void preorderRec(Node root) {
		if (root != null) {
			System.out.print(root.value + " ");
			preorderRec(root.left);
			preorderRec(root.right);
		}
	}
	
	public void postorder() {
		postorderRec(root);
		System.out.println();
	}
	
	public void postorderRec(Node root) {
		if (root != null) {
			postorderRec(root.left);
			postorderRec(root.right);
			System.out.print(root.value + " ");
		}
	}

	public static void main(String[] args) {
		
		BinaryTree tree = new BinaryTree();
		
		tree.insert(50);
		tree.insert(30);
		tree.insert(70);
		tree.insert(20);
		tree.insert(40);
		tree.insert(60);
		tree.insert(80);
		
		System.out.println("Inorder traversal: ");
		tree.inorder();
		
		System.out.println("Preorder traversal: ");
		tree.preorder();
		
		System.out.println("Postorder traversal: ");
		tree.postorder();
	}

}
