/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacollectionframework.collections.list.linkedlist;

import java.util.LinkedList;

/**
 *
 * @author cemdirman
 */
public class TestLinkedList {
    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList();
        linkedList.add("İstanbul");
        linkedList.add("Sinop");
        linkedList.add("Ordu");
        
        System.out.println("İlk eleman: " + linkedList.getFirst());
        
        linkedList.addFirst("Antalya");
                
        System.out.println("İlk eleman: " + linkedList.getFirst());
        
        linkedList.addLast("Çanakkale");
        System.out.println("Liste uzunluğu: " + linkedList.size());
        System.out.println("ilk eleman: " + linkedList.element());
 
        
    }
}
