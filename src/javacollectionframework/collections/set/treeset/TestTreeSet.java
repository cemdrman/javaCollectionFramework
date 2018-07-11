/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacollectionframework.collections.set.treeset;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author cemdirman
 */

/**
 * Set interface olduğu için somut bir nesne oluşturulamaz.
 * Set, matematikteki küme kavramına benzer. Aynı elemandan birden fazla olamaz.
 * Tree elemanların sıralanması içindir.
 * TreeSet ise hem Tree hem de Set'in özelliklerini taşır, böylece bu collection içerindeki elemanlar 
 * hem sıralı hem de benzersiz olur.
 *
 */
public class TestTreeSet {
    public static void main(String[] args) {
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("cem");
        treeSet.add("dırman");
        treeSet.add("emir");
        treeSet.add("asaf");
        treeSet.add("asya");
        treeSet.add("cem");
        treeSet.add("Cem");
        treeSet.add("Asya");
        treeSet.add("cem");
        
        Iterator iterator = treeSet.iterator();
        while(iterator.hasNext())
            System.out.println(iterator.next());
        /**
         * collection içerisinde görüldüğü üzere birden fazla "cem" stringi bulunmaktadır.
         * çıktıda ise sadece tek bir "cem" ifadesi olacaktır.
         * "cem".equals("Cem") ifade false sonucu ürettiği için ise "Cem" ifadesi de sıralamaya girmektedir ve 
         * büyük harf ile başladığı için Asya'dan sonra gelecektir.
         * 
         */
    }
}
