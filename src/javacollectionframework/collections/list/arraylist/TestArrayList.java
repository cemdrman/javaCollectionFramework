/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacollectionframework.collections.list.arraylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author cemdirman
 */
public class TestArrayList {
    
    public static void main(String[] args) {
        List sayilar = new ArrayList();
        sayilar.add(1907);
        sayilar.add(1994);
        sayilar.add(23);
        sayilar.add(3);
        sayilar.add(47);
        sayilar.add(57);
        
        Iterator iterator = sayilar.iterator();
        iterator.forEachRemaining(System.out::println);
        
        System.out.println("--------Sıralı----------");
        Collections.sort(sayilar);
        for (Iterator it = sayilar.iterator(); it.hasNext();) {
            System.out.println(it.next());
        }  
        
        System.out.println("--------Örnek 2----------");
        
        List<Character> harfler = new ArrayList();
        harfler.add('A');
        harfler.add('B');
        harfler.add('C');
        harfler.add('D');
        harfler.add('E');
        harfler.add('F');
        harfler.add('G');
        
        for (Iterator<Character> it = harfler.iterator(); iterator.hasNext();) {
            System.out.println(it.next());            
        }
        System.out.println("-------------------------------");
        Iterator<Character> iterator1 = harfler.iterator();
        iterator1.forEachRemaining(System.out::println);
    }
}
