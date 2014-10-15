package rotschwarzbaum;

public class RedBlackTree<T> {

	private static final boolean RED = false;
	private static final boolean BLACK = true;
	
	public static final int INORDER = 0;
	public static final int PREORDER = 1;
	public static final int POSTORDER = 2;

	private Node<T> root;
	private final Node<T> NIL = new Node<T>(null, null, null, BLACK, null, null);

	RedBlackTree() {
		root = NIL;
	}

	// ################################################# Einfügen ################################################

	// Einfügen in Binärbaum
	public boolean insert(String key, T object) {
		Node<T> current = root;
		Node<T> parent = null;
		
		// Baum durchlaufen bis Stelle zum Einfügen gefunden ist.
		while (current != NIL){
			if (current.getKey().equals(key)){
				return false;
			} else if(current.getKey().compareTo(key) < 0){
				parent = current;
				current = current.getRightChild();
			} else {
				parent = current;
				current = current.getLeftChild();
			}
		}
		
		// neuen roten Knoten mit übergebenen Werten erzeugen.
		Node<T> newElement = new Node<T>(NIL, NIL, parent, RED, key, object);
		
		// Vater setzen, wenn neues Element nicht Wurzelknoten ist.
		if (parent != null){
			if ( parent.getKey().compareTo(key) <0){
				parent.setRightChild(newElement);
			} else {
				parent.setLeftChild(newElement);
			}
		} else {
			root = newElement;
		}
		
		// Baum balancieren.
		insert_fix1(newElement);
		return true;
	}
	
	// Fall 1:	Der eingefügte Knoten ist der erste Knoten
	//			im Baum und damit das Wurzelelement.
	private void insert_fix1(Node<T> element) {
		if (element.getParent() == null) {
			element.setColor(BLACK);
		} else {
			insert_fix2(element);
		}
	}

	// Fall 2: Der Vater des eingefügten Knotens ist schwarz.
	private void insert_fix2(Node<T> element) {
		if (element.getParent().getColor() == BLACK) {
			return;
		} else {
			insert_fix3(element);
		}
	}

	// Fall 3:	Sowohl der Onkel als auch der Vater
	// 			des eingefügten Knotens sind rot.
	private void insert_fix3(Node<T> element) {
		if (element.getParent().getColor() == RED
				&& uncle(element).getColor() == RED) {
			element.getParent().setColor(BLACK);
			uncle(element).setColor(BLACK);
			grandparent(element).setColor(RED);
			insert_fix1(grandparent(element));
		} else {
			insert_fix4(element);
		}
	}

	// Fall 4:	Der eingefügte Knoten hat einen schwarzen oder keinen
	//			Onkel und ist das rechte Kind seines roten Vaters.
	private void insert_fix4(Node<T> element) {
		// 4.1 Vaterknoten ist links am Großvater
		if (element == element.getParent().getRightChild() 
				&& element.getParent() == grandparent(element).getLeftChild()) {
			leftRotate(element.getParent());
			element = element.getLeftChild();
		// 4.2 Vaterknoten ist rechts am Großvater
		} else if (element == element.getParent().getLeftChild() 
				&& element.getParent() == grandparent(element).getRightChild()) {
			rightRotate(element.getParent());
			element = element.getRightChild();
		}
		insert_fix5(element);
	}

	// Fall 5: 	Der eingefügte Knoten hat einen schwarzen oder keinen
	//			Onkel und ist das linke Kind seines roten Vaters
	private void insert_fix5(Node<T> element) {
		element.getParent().setColor(BLACK);
		grandparent(element).setColor(RED);
		// 5.1 	Vaterknoten ist links am Großvater
		if (element == element.getParent().getLeftChild()
				&& element.getParent() == grandparent(element).getLeftChild()) {
			rightRotate(grandparent(element));
		// 5.2 	Vaterknoten ist rechts am Großvater und der
		//		eingefügte Knoten ist rechts am Vaterknoten
		} else
			leftRotate(grandparent(element));
	}

	// ###########################################################################################################

	// ############################################### Löschen ###################################################

	// Löschen. Ersetze zu löschendes Element mit größtem Element aus linkem Teilbaum und lösche dieses.
	public T delete(String key) {
		if (searchNode(key) != null) {
			Node<T> maxValue, child, element;
			element = searchNode(key);
			T object = element.getObject();
			
			// Suche größtes Element im linken Teilbaum.
			if ((element.getLeftChild() == NIL) || (element.getRightChild() == NIL)){
				maxValue = element;
			} else {
				maxValue = element.getLeftChild();
				while (maxValue.getRightChild() != NIL) {
					maxValue = maxValue.getRightChild();
				}
			}
			// Ersetze element mit maxValue.
			if(element != maxValue){
				element.setKey(maxValue.getKey());
				element.setObject(maxValue.getObject());
			}
			// Setze Kindknoten.
			if (maxValue.getRightChild() == NIL) {
				child = maxValue.getLeftChild();
			} else {
				child = maxValue.getRightChild();
			}
			// Wenn Kind nicht NIL, setzte den Verweis auf den Vater eine Ebene hoch.
			if (child != NIL) {
				child.setParent(maxValue.getParent());
			} 
			// Setze den Verweis vom Vater richtig, falls Element nicht die Wurzel ist.
			if(maxValue.getParent() != null){
				if (maxValue.getParent().getLeftChild() == maxValue){
					maxValue.getParent().setLeftChild(child);
				} else {
					maxValue.getParent().setRightChild(child);
				}
			} else {
				root = child;
			}
			if (maxValue.getColor() == BLACK) { // Fall 0.1 -> kein Ausgleichen nötig.
				if (child.getColor() == RED) { 	// Fall 0.2 -> Kind schwarz färben
					child.setColor(BLACK);		//			-> Ausgleichen beendet.
				} else {
					delete_fix1(child); // ansonsten erste Ausgleichsfunktion aufrufen
				}
			}
			return object;
		} else {
			System.out.println("Element nicht vorhanden!");
			return null;
		}
	}
	
	// Fall 1: Der Konfliktknoten ist die neue Wurzel.
	private void delete_fix1(Node<T> element) {
		if (element.getParent() == null) {
			return;
		} else {
			delete_fix2(element);
		}
	}

	// Fall 2: Der Bruder des Konfliktknotens ist rot.
	private void delete_fix2(Node<T> element) {
		if (sibling(element).getColor() == RED) {
			element.getParent().setColor(RED);
			sibling(element).setColor(BLACK);
			if (element == element.getParent().getLeftChild()) {
				leftRotate(element.getParent());
			} else {
				rightRotate(element.getParent());
			}
		}
		delete_fix3(element);
	}

	// Fall 3: Der Vater des Konfliktknotens, sein Bruder und dessen Kinder sind schwarz.
	private void delete_fix3(Node<T> element) {
		if (element.getParent().getColor() == BLACK
				&& sibling(element).getColor() == BLACK
				&& sibling(element).getLeftChild().getColor() == BLACK
				&& sibling(element).getRightChild().getColor() == BLACK)
		{
			sibling(element).setColor(RED);
			delete_fix1(element.getParent());
		} else {
			delete_fix4(element);
		}
	}
	
	// Fall 4: 	Der Vater des Konfliktknotens ist rot,
	//			sein Bruder und dessen Kinder sind schwarz.
	private void delete_fix4(Node<T> element){
		if (element.getParent().getColor() == RED
				&& sibling(element).getColor() == BLACK
				&& sibling(element).getLeftChild().getColor() == BLACK
				&& sibling(element).getRightChild().getColor() == BLACK)
		{
			sibling(element).setColor(RED);
			element.getParent().setColor(BLACK);
		} else {
			delete_fix5(element);
		}
	}
	
	// Fall 5: 	Das linke Kind des Bruders ist rot, das rechte Kind
	//			und der Bruder des Konfliktknotens sind schwarz.
	private void delete_fix5(Node<T> element){
		// Der Konfliktknoten ist das linke Kind des Vaterknotens
		if (element == element.getParent().getLeftChild()
				&& sibling(element).getColor() == BLACK
				&& sibling(element).getLeftChild().getColor() == RED
				&& sibling(element).getRightChild().getColor() == BLACK)
		{
			sibling(element).setColor(RED);
			sibling(element).getLeftChild().setColor(BLACK);
			rightRotate(sibling(element));
		}
		// Der Konfliktknoten ist das rechte Kind des Vaterknotens
		else if (element == element.getParent().getRightChild()
				&& sibling(element).getColor() == BLACK
				&& sibling(element).getLeftChild().getColor() == RED
				&& sibling(element).getRightChild().getColor() == BLACK)
		{
			sibling(element).setColor(RED);
			sibling(element).getLeftChild().setColor(BLACK);
			leftRotate(sibling(element));
		}
		delete_fix6(element);
	}

	// Fall 6: Der Bruder des Konfliktknotens ist schwarz
	//		   und das rechte/linke Kind des Bruders ist rot.
	private void delete_fix6(Node<T> element){
		sibling(element).setColor(element.getParent().getColor());
		element.getParent().setColor(BLACK);
		// Das rechte Kind des Bruders ist rot.
		if (element == element.getParent().getLeftChild()){ 
			sibling(element).getRightChild().setColor(BLACK);
			leftRotate(element.getParent());
		} else { // Das linke Kind des Bruders ist rot.
			sibling(element).getLeftChild().setColor(BLACK);
			rightRotate(element.getParent());
		}
	}
	
	// ###########################################################################################################
	
	// ######################################### Hilfs-funktionen ################################################

	private void leftRotate(Node<T> rotationElement) {

		Node<T> rightElement = rotationElement.getRightChild();

		rotationElement.setRightChild(rightElement.getLeftChild());
		
		if (rightElement.getLeftChild() != NIL) {
			rightElement.getLeftChild().setParent(rotationElement);
		}
		if (rightElement != NIL) { 
			rightElement.setParent(rotationElement.getParent());
		}
		if (rotationElement.getParent() != NIL && rotationElement.getParent() != null) {
			if (rotationElement == rotationElement.getParent().getLeftChild()) {
				rotationElement.getParent().setLeftChild(rightElement);
			} else {
				rotationElement.getParent().setRightChild(rightElement);
			}
		} else {
			root = rightElement;
		}

		rightElement.setLeftChild(rotationElement);

		if (rotationElement != NIL) { 
			rotationElement.setParent(rightElement);  
		}
	}
	
	private void rightRotate(Node<T> rotationElement) {

		Node<T> leftElement = rotationElement.getLeftChild();
		rotationElement.setLeftChild(leftElement.getRightChild());

		if (leftElement.getRightChild() != NIL) {
			leftElement.getRightChild().setParent(rotationElement);
		}
		if (leftElement != NIL) {
			leftElement.setParent(rotationElement.getParent());
		}
		if (rotationElement.getParent() != NIL	&& rotationElement.getParent() != null) {
			if (rotationElement == rotationElement.getParent().getRightChild()) {
				rotationElement.getParent().setRightChild(leftElement);
			} else {
				rotationElement.getParent().setLeftChild(leftElement);
			}
		} else {
			root = leftElement;
		}

		leftElement.setRightChild(rotationElement);

		if (rotationElement != NIL) {
			rotationElement.setParent(leftElement);
		}
	}

	private Node<T> grandparent(Node<T> element) {
		return element.getParent().getParent();
	}

	private Node<T> uncle(Node<T> element) {
		if (grandparent(element).getLeftChild() == element.getParent()) {
			return grandparent(element).getRightChild();
		} else {
			return grandparent(element).getLeftChild();
		}
	}
	
	private Node<T> sibling(Node<T> element) {
		if (element.getParent().getLeftChild() == element) {
			return element.getParent().getRightChild();
		} else {
			return element.getParent().getLeftChild();
		}
	}

	// ###########################################################################################################
	
	// ############################################# Suchen ######################################################
	
	private Node<T> searchNode(String key){
		Node<T> current = root;
		while (current != NIL){
			if (current.getKey().equals(key)){
				return current;
			} else if(current.getKey().compareTo(key) < 0){
				current = current.getRightChild();
			} else {
				current = current.getLeftChild();
			}
		}
		return null;
	}
	
	public T search(String key){
		if (searchNode(key) != null){
			System.out.println("Element mit dem Key \"" + key + "\" wurde gefunden!");
			return searchNode(key).getObject();
		} else {
			System.out.println("Element mit dem Key \"" + key + "\" nicht gefunden!");
			return null;
		}
	}

	// ###########################################################################################################
	
	// ######################################### Ausgabe / Traversierung #########################################
	
	public void print(int order) {
		if (root != NIL) {
			switch (order) {
				case INORDER: 	System.out.println("inOrder:");
							  	inOrder(root, 0);
							  	break;
					
				case PREORDER:	System.out.println("preOrder:");
							   	preOrder(root, 0);
							   	break;
					
				case POSTORDER: System.out.println("postOrder:");
								postOrder(root, 0);
								break;
			}
		} else {
			System.out.println("Baum ist leer!");
		}
	}

	private void inOrder(Node<T> element, int depth){
		if (element.getLeftChild() != NIL) {
			inOrder(element.getLeftChild(), depth + 1);
		}
		
		String color = element.getColor() ? "Schwarz" : "Rot";
		System.out.print("[" + element.getKey() + "]" + color + "|" + depth + "   ");

		if (element.getRightChild() != NIL) {
			inOrder(element.getRightChild(), depth + 1);
		}
	}
	
	private void preOrder(Node<T> element, int depth){
		String color = element.getColor() ? "Schwarz" : "Rot";
		System.out.print("[" + element.getKey() + "]" + color + "|" + depth + "   ");
		if (element.getLeftChild() != NIL) {
			preOrder(element.getLeftChild(), depth + 1);
		}
		if (element.getRightChild() != NIL) {
			inOrder(element.getRightChild(), depth + 1);
		}
		
	}
	
	private void postOrder(Node<T> element, int depth) {
		if (element.getLeftChild() != NIL) {
			inOrder(element.getLeftChild(), depth + 1);
		}
		if (element.getRightChild() != NIL) {
			inOrder(element.getRightChild(), depth + 1);
		}
		String color = element.getColor() ? "Schwarz" : "Rot";
		System.out.print("[" + element.getKey() + "]" + color + "|" + depth + "   ");
	}

	// ###########################################################################################################
}
