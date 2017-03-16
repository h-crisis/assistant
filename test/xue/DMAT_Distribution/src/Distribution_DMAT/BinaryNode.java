package Distribution_DMAT;

import java.util.*;

/**
 * Created by jiao.xue on 2017/03/16.
 * 二分木を作る
 * 拠点病院の二分木
 * 左の部分木に入っている全てのvalueが親ノードのvalueより小さい右の部分木に入っている全てのvalueが親ノードのvalueより大きい；
 * dmatの二分木
 * 左の部分木に入っている全てのvalueが親ノードのvalueより大きい・右の部分木に入っている全てのvalueが親ノードのvalueより小さい；
 */
public class BinaryNode {

        private  String key;//current key
        private  double value;//current value
        private BinaryNode lChild;//left child
        private BinaryNode rChild;//right child

    public BinaryNode(String keys,  double values){
            this.key=keys;
            this.value = values;
            this.lChild = null;
            this.rChild = null;
        }


//一般的なmethod
        public BinaryNode getLChild() {
            return lChild;
        }
        public void setLChild(BinaryNode child) {
            lChild = child;
        }
        public BinaryNode getRChild() {
            return rChild;
        }
        public void setRChild(BinaryNode child) {
            rChild = child;
        }
        public double getValue() {
            return value;
        }

    public String getKey() {
        return key;
    }
        public void setValue(double value) {
            this.value = value;
        }

    public void setKey(String key) {
        this.key = key;
    }



        /**
         * 実行時間: ( nlog(n) )
         * **/
        public void addChild_order(String a, double b){
            if(b<value){
                if(lChild!=null){
                    lChild.addChild_order(a,b);
                }
                else{
                    lChild = new BinaryNode(a,b);
                }
            }
            else{
                if(rChild!=null){
                    rChild.addChild_order(a,b);
                }
                else{
                    rChild = new BinaryNode(a,b);
                }
            }
        }
//逆順序木
    public void addChild_reverse(String a, double b){
        if(b>value){
            if(lChild!=null){
                lChild.addChild_reverse(a,b);
            }
            else{
                lChild = new BinaryNode(a,b);
            }
        }
        else{
            if(rChild!=null){
                rChild.addChild_reverse(a,b);
            }
            else{
                rChild = new BinaryNode(a,b);
            }
        }
    }


    public static BinaryNode createTree_order(HashMap<String, Double> map){
        //mapをノードのポイントに変わる
        Iterator<HashMap.Entry<String,Double>> it=map.entrySet().iterator();
        HashMap.Entry<String,Double> entry=it.next();
        BinaryNode root=new BinaryNode(entry.getKey(),entry.getValue());
        while(it.hasNext()){
            HashMap.Entry<String,Double> entrys=it.next();
            root.addChild_order(entrys.getKey(),entrys.getValue());
        }
       //inOrderTravers(root);

        return root;

    }

    public static BinaryNode createTree_reverse(HashMap<String,Double> map){
        //mapをノードのポイントに変わる
        Iterator<HashMap.Entry<String,Double>> it=map.entrySet().iterator();
        HashMap.Entry<String,Double> entry=it.next();
        BinaryNode root=new BinaryNode(entry.getKey(),entry.getValue());
        while(it.hasNext()){
            HashMap.Entry<String,Double> entrys=it.next();
            root.addChild_reverse(entrys.getKey(),entrys.getValue());
        }
        inOrderTravers(root);
        return root;
    }

    public static void inOrderTravers(BinaryNode node){
        if(node==null){
            return;
        }
        inOrderTravers(node.lChild);
        System.out.println(node.key +" ");
        inOrderTravers(node.rChild);

    }


//木からノードを削除する　ノードのkeyはel
    public static void deleteByMerging(String el, BinaryNode root, HashMap<String, Double> map)
    {
        BinaryNode tmp,node,p=root,prev=null;
        //elはrootなら
        if(root!=null&&root.key.equals(el)){

        }
        else {
/*find the node to be deleted*/
            while (p != null && !(p.key.equals(el))) {
                prev = p;

                if (p.value <= map.get(el)) {
                    p = p.rChild;
                } else p = p.lChild;
            }
/*find end*/
            node = p;

            if (p != null && p.key.equals(el)) {
                if (node.rChild == null)
//node has no right child then its left child (if any) is attached to
                    node = node.lChild;
//its parent
                else if (node.lChild == null)
                    //node has no left child then its right child (if any) is attched to
                    node = node.rChild;
                    //its parent
                else {
                    tmp = node.lChild;
                    while (tmp.rChild != null)
                        tmp = tmp.rChild;
//find the rightmost node of the left subtree
                    tmp.rChild = node.rChild;
//establish the link between the rightmost node of the left subtree and the right subtree
                    node = node.lChild;
                }
                if (p == root) {
                    root = node;
                } else if (prev.lChild == p) {
                    prev.lChild = node;
                } else prev.rChild = node;
            } else if (root != null) {
                System.out.println("the node is not in the tree");
            } else System.out.println("The tree is empty");
        }
    }



}
