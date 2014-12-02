// PA5
// Authors: Sadie Henry, Gabriella Fontani
// Date: 12/1/2014
// Class: CS200

public interface TermIndex {
	public void add(String filename, String newWord);
	public int size();
	public void delete(String word);
	public Term get(String word, Boolean printP);
}
