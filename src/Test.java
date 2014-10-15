package rotschwarzbaum;

public class Test {

	public static void main(String args[]){
		RedBlackTree<String> tree = new RedBlackTree<String>();
		tree.insert("1", "erster Knoten");
		tree.insert("2", "zweiter Knoten");
		tree.insert("3", "dritter Knoten");
		tree.insert("4", "vierter Knoten");
		tree.insert("5", "fünfter Knoten");
		tree.insert("6", "sechster Knoten");
		tree.insert("7", "siebter Knoten");
		System.out.print("Erster Baum, ");
		tree.print(RedBlackTree.INORDER);
		System.out.println("\n");
		
		RedBlackTree<String> tree2 = new RedBlackTree<String>();
		tree2.insert("7", "erster Knoten");
		tree2.insert("4", "zweiter Knoten");
		tree2.insert("5", "dritter Knoten");
		tree2.insert("3", "vierter Knoten");
		tree2.insert("2", "fünfter Knoten");
		tree2.insert("1", "sechster Knotenr");
		tree2.insert("6", "siebter Knoten");
		System.out.print("Zweiter Baum, ");
		tree2.print(RedBlackTree.INORDER);
		
		System.out.println("\n \n \n");
		System.out.println("Suchen im ersten Baum:");
		System.out.println("Inhalt: " + tree.search("4") + "\n");
		System.out.println("Inhalt: " + tree.search("15") + "\n");
		
		System.out.println("\n");
		System.out.println("Lösche Knoten 4 aus erstem Baum. Zurückgegebenes Objekt: \"" + tree.delete("4") + "\"\n");
		System.out.print("Erster Baum, ");
		tree.print(RedBlackTree.INORDER);
	}
	
}
