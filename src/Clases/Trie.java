/*
 * Copyright (C) 2020 Administrador
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Clases;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrador
 */
public class Trie {
    private TrieNode root;
    
    public Trie(){
        root = new TrieNode();
    }
    
    public void insert(String word){
        HashMap<Character, TrieNode> children = root.children;
        
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            TrieNode t;
            if (children.containsKey(c)) {
                t = children.get(c);
            }
            else{
                t = new TrieNode(c);
                children.put(c, t);
            }
            children = t.children;
            if (i==word.length()-1) {
                t.isLeaf = true;
            }
        }
    }
    
    public boolean search(String word) {
        TrieNode t = searchNode(word);
        return t != null && t.isLeaf;
    }
    
    public TrieNode searchNode(String str){
        Map<Character, TrieNode> children = root.children;
        TrieNode t = null;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (children.containsKey(c)) {
                t = children.get(c);
                children = t.children;
            }
            else{
                return null;
            }
        }
        return t;
    }
}
