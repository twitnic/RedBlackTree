package rotschwarzbaum;

public class Node<T> {
	private Node<T> leftChild;
	private Node<T> rightChild;
	private Node<T> parent;
	private boolean color;

	private String key;
	private T object;

	Node(Node<T> leftChild, Node<T> rightChild, Node<T> parent, boolean color, String key, T object) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.parent = parent;
		this.color = color;
		this.key = key;
		this.object = object;
	}
	
	
	public Node<T> getLeftChild(){
		return leftChild;
	}

	public Node<T> getRightChild(){
		return rightChild;
	}
	
	public Node<T> getParent(){
		return parent;
	}

	public boolean getColor(){
		return color;
	}

	public String getKey(){
		return key;
	}
	
	public T getObject(){
		return object;
	}

	
	public void setLeftChild(Node<T> element){
		this.leftChild = element;
	}
	
	public void setRightChild(Node<T> element){
		this.rightChild = element;
	}
	
	public void setParent(Node<T> element){
		this.parent = element;
	}
	
	public void setColor(boolean color){
		this.color = color;
	}
		
	public void setKey(String key){
		this.key = key;
	}

	public void setObject(T object){
		this.object = object;
	}
	
}
