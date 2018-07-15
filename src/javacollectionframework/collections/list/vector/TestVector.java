/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacollectionframework.collections.list.vector;

import java.util.List;
import java.util.Vector;

/**
 *
 * @author cemdirman
 */
public class TestVector {
    public static void main(String[] args) {
        List vector = new Vector();
        vector.add("Fenerbahçe");
        vector.add(23);
        
        
        Vector<String> vector1 = new Vector<>(10);
        for (int i = 0; i < vector1.size(); i++) {
            vector1.add(i + ".sayi");
        }
        vector1.setSize(20);       
        
        
        Vector<Character> vector2 = new Vector<>(30, 10);
    }
}
